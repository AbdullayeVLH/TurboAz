package az.code.turboplus.services;

import az.code.turboplus.models.Listing;
import az.code.turboplus.models.Transaction;
import az.code.turboplus.models.User;
import az.code.turboplus.repositories.ListingRepository;
import az.code.turboplus.repositories.TransactionRepository;
import az.code.turboplus.repositories.UserRepository;
import az.code.turboplus.utils.MailUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserRepository userRepoReal;
    @Autowired
    private ListingRepository listRepoReal;
    @Autowired
    private UserService userService;

    private UserRepository userRepo = Mockito.mock(UserRepository.class);
    private TransactionRepository transRepo = Mockito.mock(TransactionRepository.class);
    private ListingRepository listRepo = Mockito.mock(ListingRepository.class);
    private MailUtil mailUtil = Mockito.mock(MailUtil.class);

    @Test
    @DisplayName("This test should chek if finding user by username function is working properly")
    void checkIfFindByUsernameWorks() {
        UserServiceImpl userService = new UserServiceImpl(userRepo, transRepo, listRepo, mailUtil);
        User user = new User().toBuilder().username("valeh").email("valeh@gmail.com").phoneNumber("+994709587412").name("Valeh")
                .balance(100.0).freeListing(true).registerTime(LocalDateTime.now()).build();
        Mockito.when(userRepo.findByUsername("valeh")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("valeh");
        String expectedResult = user.getUsername();
        Assertions.assertThat(result.get().getUsername()).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("This test is for checking getting user balance with username")
    void getUserBalance() {
        UserServiceImpl userService = new UserServiceImpl(userRepo, transRepo, listRepo, mailUtil);
        User user = new User().toBuilder().username("valeh").email("valeh@gmail.com").phoneNumber("+994709587412").name("Valeh")
                .balance(100.0).freeListing(true).registerTime(LocalDateTime.now()).build();
        Mockito.when(userRepo.findBalance("valeh")).thenReturn(user.getBalance());

        Double result = userService.getUserBalance("valeh");
        Double expectedResult = user.getBalance();
        Assertions.assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("Test case for checking getting user balance information")
    void getUserTransactions() {
        UserServiceImpl userService = new UserServiceImpl(userRepo, transRepo, listRepo, mailUtil);
        User user = new User().toBuilder().username("valeh").email("valeh@gmail.com").phoneNumber("+994709587412").name("Valeh")
                .balance(100.0).freeListing(true).registerTime(LocalDateTime.now()).build();
        Mockito.when(transRepo.findAllByUsername("valeh")).thenReturn(user.getTransactions());

        List<Transaction> result = transRepo.findAllByUsername("valeh");
        List<Transaction> expectedResult = user.getTransactions();
        Assertions.assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void increaseBalance() {
        User user = new User().toBuilder().username("valeh").email("valeh@gmail.com").phoneNumber("+994709587412").name("Valeh Abdullayev")
                .balance(100.0).freeListing(false).registerTime(LocalDateTime.now()).build();
        userRepoReal.save(user);
        Double result = userService.increaseBalance("valeh", 50.0);
        Double expectedResult = 150.0;
        Assertions.assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void decreaseBalance() {
        User user = new User().toBuilder().username("valeh").email("valeh@gmail.com").phoneNumber("+994709587412").name("Valeh Abdullayev")
                .balance(100.0).freeListing(false).registerTime(LocalDateTime.now()).build();
        Listing listing = new Listing().toBuilder().autoPay(true).creationDate(LocalDateTime.now()).endDate(LocalDate.now())
                .user(user).build();
        userRepoReal.save(user);
        listing = listRepoReal.save(listing);
        Double result = userService.decreaseBalance("valeh", 10.0, listing.getId());
        Double expectedResult = 90.0;
        Assertions.assertThat(result).isEqualTo(expectedResult);
    }
}