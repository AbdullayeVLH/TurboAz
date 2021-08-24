package az.code.turboplus.services;

import az.code.turboplus.dtos.TransactionListDTO;
import az.code.turboplus.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    void save(User user);

    double getUserBalance(String username);

    List<TransactionListDTO> getUserTransactions(String username);

    double increaseBalance(String username, Double amount);

    double decreaseBalance(String username, Double amount, Long listingId);
}
