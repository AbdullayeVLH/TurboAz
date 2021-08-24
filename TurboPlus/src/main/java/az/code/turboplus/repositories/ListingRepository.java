package az.code.turboplus.repositories;

import az.code.turboplus.models.Listing;
import az.code.turboplus.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ListingRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {

    @Query("SELECT listing FROM Listing listing " +
            "WHERE listing.baseListing.isActive=true " +
            "ORDER BY listing.baseListing.updateTime DESC")
    Page<Listing> findAllActive(Pageable pageable);

    @Query("SELECT listing FROM Listing listing " +
            "WHERE listing.baseListing.isActive=true " +
            "AND listing.baseListing.type='VIP' " +
            "ORDER BY listing.baseListing.updateTime DESC")
    List<Listing> findAllVipActive();

    @Query("SELECT listing FROM Listing listing " +
            "WHERE listing.user.username=:username " +
            "AND listing.id=:id")
    Optional<Listing> findByUsernameAndId(String username, Long id);

    @Query("SELECT listing FROM Listing listing " +
            "WHERE listing.user.username=:username " +
            "ORDER BY listing.baseListing.updateTime DESC")
    Page<Listing> findAlByUsername(String username, Pageable pageable);

    @Query("SELECT listing FROM Listing listing " +
            "WHERE listing.autoPay=true " +
            "AND listing.baseListing.isActive=true " +
            "AND listing.endDate =:endDate")
    List<Listing> findAllActiveByAutoPay(LocalDate endDate);

    List<Listing> findAllByUserAndEndDate(User user, LocalDate endDate);
}
