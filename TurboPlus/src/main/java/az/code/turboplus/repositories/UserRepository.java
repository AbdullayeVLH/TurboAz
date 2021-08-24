package az.code.turboplus.repositories;

import az.code.turboplus.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT user.balance FROM User user " +
            "WHERE user.username=:username")
    Double findBalance(String username);

    Optional<User> findByUsername(String username);
}
