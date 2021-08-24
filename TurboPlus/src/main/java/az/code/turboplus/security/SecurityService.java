package az.code.turboplus.security;

import az.code.turboplus.dtos.LoginDTO;
import az.code.turboplus.dtos.LoginResponseDTO;
import az.code.turboplus.dtos.RegisterDTO;
import az.code.turboplus.dtos.RegisterResponseDTO;

public interface SecurityService {

    LoginResponseDTO login(LoginDTO user);

    RegisterResponseDTO register(RegisterDTO register);

    String verify(String token, String username);
}
