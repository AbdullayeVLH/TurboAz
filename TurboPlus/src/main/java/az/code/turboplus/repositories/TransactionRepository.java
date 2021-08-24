package az.code.turboplus.repositories;

import az.code.turboplus.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT trans FROM Transaction trans " +
            "WHERE trans.user.username=:username")
    List<Transaction> findAllByUsername(String username);
}
