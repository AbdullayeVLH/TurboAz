package az.code.turboplus.repositories;

import az.code.turboplus.models.Listing;
import az.code.turboplus.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query("SELECT photo FROM Photo photo " +
            "WHERE photo.id=:id " +
            "AND photo.listing.id=:listingId")
    Optional<Photo> findByListingIdAndId(Long listingId, Long id);

    @Query("SELECT photo FROM Photo photo " +
            "WHERE photo.listing.id=:listingId")
    List<Photo> findAllByListingId(Long listingId);

    void deleteByListingAndId(Listing listing, Long id);
}
