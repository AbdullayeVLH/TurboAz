package az.code.turboplus.services;

import az.code.turboplus.dtos.*;
import az.code.turboplus.enums.BodyType;
import az.code.turboplus.enums.FuelType;
import az.code.turboplus.models.Listing;
import az.code.turboplus.models.Photo;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ListingService {

    ListingGetDTO getSingleListing(Long id);

    List<ListingListDTO> getVipListings(Integer skip, Integer limit);

    List<ListingListDTO> getListings(Integer pageNo, Integer pageSize, String sortBy);

    List<ListingListDTO> search(SearchRequest request, Integer count, Integer page);

    List<ListingListDTO> getUserListings(String username, Integer pageNo, Integer pageSize, String sortBy);

    ListingGetDTO getUserSingleListing(String username, Long id);

    ListingGetDTO addListing(String username, Listing listing);

    ListingGetDTO updateListing(String username, Listing listing);

    ListingGetDTO deleteListing(String username, Long id);

    ListingGetDTO makeVip(String username, Long id);

    List<MakeDTO> getAllMakes();

    List<ModelDTO> getAllMakeModels(Long id);

    List<CityDTO> getCities();

    List<FuelType> getFuelTypes();

    List<BodyType> getBodyTypes();
}
