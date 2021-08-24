package az.code.turboplus.repositories;

import az.code.turboplus.models.Subscription;
import az.code.turboplus.models.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>, JpaSpecificationExecutor<Subscription> {

    @Query("SELECT sub FROM Subscription sub " +
            "WHERE sub.user.username=:username " +
            "AND sub.isActive=true")
    List<Subscription> findByUsername(String username);

    @Query("SELECT sub FROM Subscription sub " +
            "WHERE sub.user.username=:username " +
            "AND sub.id=:id " +
            "AND sub.isActive=true")
    Optional<Subscription> findByUsernameAndIdAndActive(String username, Long id);

    @Query("SELECT sub FROM Subscription sub " +
            "WHERE sub.isActive=true")
    List<Subscription> findAllByActive(Specification<Subscription> spec);

    Integer countSubscriptionByUser(User user);
}
