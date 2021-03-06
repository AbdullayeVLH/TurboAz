package az.code.turboplus.security;

import az.code.turboplus.dtos.LoginDTO;
import az.code.turboplus.dtos.LoginResponseDTO;
import az.code.turboplus.dtos.RegisterDTO;
import az.code.turboplus.dtos.RegisterResponseDTO;
import az.code.turboplus.exceptions.InvalidVerificationToken;
import az.code.turboplus.exceptions.LoginException;
import az.code.turboplus.exceptions.UserNotFound;
import az.code.turboplus.models.User;
import az.code.turboplus.models.Verification;
import az.code.turboplus.repositories.UserRepository;
import az.code.turboplus.repositories.VerificationRepository;
import az.code.turboplus.utils.MailUtil;
import az.code.turboplus.utils.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.*;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.resource}")
    private final String role = "app-user";
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    @Value("${app.keycloak.username}")
    private String adminUsername;
    @Value("${app.keycloak.password}")
    private String adminPassword;
    @Value("${mail.auth.verification.subject}")
    private String verificationSubject;
    @Value("${mail.auth.verification.context}")
    private String verificationContext;
    @Value("${mail.auth.verification.url}")
    private String verificationUrl;

    MailUtil mail;
    VerificationRepository verfRepo;
    UserRepository userRepo;

    public SecurityServiceImpl(MailUtil mail, VerificationRepository verfRepo, UserRepository userRepo) {
        this.mail = mail;
        this.verfRepo = verfRepo;
        this.userRepo = userRepo;
    }

    @Override
    public LoginResponseDTO login(LoginDTO user) {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", clientSecret);
        clientCredentials.put("grant_type", "password");
        Configuration configuration =
                new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);
        try {
            AccessTokenResponse response =
                    authzClient.obtainAccessToken(user.getEmail(), user.getPassword());
            Util.convertToken(response.getToken());
            return LoginResponseDTO.builder().token(response.getToken()).build();
        } catch (HttpResponseException | JsonProcessingException exception) {
            throw new LoginException();
        }
    }

    @Override
    public RegisterResponseDTO register(RegisterDTO register) {
        Keycloak keycloak = getRealmCli();
        UserRepresentation user = createUser(register);
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(user);
        if (response.getStatus() == 201) {
            UserResource userResource = changeTemporaryPassword(register, usersResource, response);
            RoleRepresentation realmRoleUser = realmResource.roles().get(role).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(realmRoleUser));
            sendVerificationEmail(register);
        }
        keycloak.tokenManager().getAccessToken();
        return RegisterResponseDTO.builder().message(response.getStatusInfo().toString()).build();
    }

    private void sendVerificationEmail(RegisterDTO register) {
        String token = UUID.randomUUID().toString();
        userRepo.save(new User(register));
        verfRepo.save(Verification.builder()
                .token(token)
                .user(User.builder().username(register.getUsername()).build()).build());
        mail.sendNotificationEmail(register.getEmail(), verificationSubject,
                verificationContext.formatted(verificationUrl.formatted(token, register.getUsername())));
    }

    private UserResource changeTemporaryPassword(RegisterDTO register, UsersResource usersResource, Response response) {
        String userId = CreatedResponseUtil.getCreatedId(response);
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(register.getPassword());
        UserResource userResource = usersResource.get(userId);
        userResource.resetPassword(passwordCred);
        return userResource;
    }

    private UserRepresentation createUser(RegisterDTO register) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(register.getUsername());
        user.setFirstName(register.getName());
        user.setLastName(register.getSurname());
        user.setEmail(register.getEmail());
        user.setAttributes(Collections.singletonMap("phoneNumber",
                Collections.singletonList(register.getPhoneNumber())));
        return user;
    }

    private Keycloak getRealmCli() {
        return KeycloakBuilder.builder().serverUrl(authServerUrl)
                .grantType(OAuth2Constants.PASSWORD).realm(realm).clientId("admin-cli")
                .username(adminUsername).password(adminPassword)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
    }

    @Override
    public String verify(String token, String username) {
        Keycloak keycloak = getRealmCli();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> users = usersResource.search(username);
        UserRepresentation search;
        if (users.size() == 0)
            throw new UserNotFound();
        search = users.get(0);
        UserResource user = usersResource.get(search.getId());
        search.setEmailVerified(true);
        Optional<Verification> verf = verfRepo.findById(token);
        if (verf.isEmpty())
            throw new InvalidVerificationToken();
        verfRepo.delete(verf.get());
        user.update(search);
        return "User verified.";
    }
}
