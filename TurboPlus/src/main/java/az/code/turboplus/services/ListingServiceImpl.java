package az.code.turboplus.services;

import az.code.turboplus.dtos.*;
import az.code.turboplus.enums.BodyType;
import az.code.turboplus.enums.FuelType;
import az.code.turboplus.enums.Type;
import az.code.turboplus.exceptions.ListingNotFound;
import az.code.turboplus.exceptions.ThisListingIsVipAlready;
import az.code.turboplus.exceptions.UserNotFound;
import az.code.turboplus.models.Listing;
import az.code.turboplus.models.User;
import az.code.turboplus.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static az.code.turboplus.utils.SpecificationUtilSimple.*;
import static az.code.turboplus.utils.SpecificationUtilSimple.hasBooleanOption;
import static az.code.turboplus.utils.Util.getResult;
import static az.code.turboplus.utils.Util.preparePage;

@Service
public class ListingServiceImpl implements ListingService {

    @Value("${listing.default.price}")
    private String defaultPrice;
    @Value("${listing.vip.price}")
    private String vipPrice;

    private final UserService service;
    private final ListingRepository listRepo;
    private final MakeRepository makeRepo;
    private final ModelRepository modelRepo;
    private final CityRepository cityRepo;
    private final UserRepository userRepo;
    private final SubscriptionService subService;

    public ListingServiceImpl(UserService service, ListingRepository listRepo, MakeRepository makeRepo, ModelRepository modelRepo, CityRepository cityRepo, UserRepository userRepo, SubscriptionService subService) {
        this.service = service;
        this.listRepo = listRepo;
        this.makeRepo = makeRepo;
        this.modelRepo = modelRepo;
        this.cityRepo = cityRepo;
        this.userRepo = userRepo;
        this.subService = subService;
    }

    @Override
    public ListingGetDTO getSingleListing(Long id) {
        return new ListingGetDTO(listRepo.findById(id).orElseThrow(ListingNotFound::new));
    }

    @Override
    public List<ListingListDTO> getVipListings(Integer skip, Integer limit) {
        return listRepo.findAllVipActive().stream()
                .skip(skip).limit(limit)
                .map(ListingListDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListingListDTO> getListings(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = preparePage(pageNo, pageSize, sortBy);
        Page<Listing> pageResult = listRepo.findAllActive(paging);
        return getResult(pageResult).stream()
                .map(ListingListDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListingListDTO> search(SearchRequest searchRequest, Integer count, Integer page) {
        Pageable paging = preparePage(page, count);
        Specification<Listing> spec = sameString(searchRequest.getMake(), "make")
                .and(sameString(searchRequest.getModel(), "model"))
                .and(sameString(searchRequest.getLocation(), "city"))
                .and(hasRange(searchRequest.getMinYear(), searchRequest.getMaxYear(), "year"))
                .and(hasRange(searchRequest.getMinPrice(), searchRequest.getMaxPrice(), "price"))
                .and(hasRange(searchRequest.getMinMileage(), searchRequest.getMaxMileage(), "mileage"))
                .and(sameFuelType(searchRequest.getFuelType()))
                .and(sameBodyType(searchRequest.getBodyType()))
                .and(hasBooleanOption(searchRequest.getLoanOption(), "loanOption"))
                .and(hasBooleanOption(searchRequest.getBarterOption(), "barterOption"))
                .and(hasBooleanOption(searchRequest.getLeaseOption(), "leaseOption"))
                .and(hasBooleanOption(searchRequest.getCashOption(), "cashOption"));
        Page<Listing> pageResult = listRepo.findAll(spec, paging);
        return getResult(pageResult).stream()
                .map(ListingListDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListingListDTO> getUserListings(String username, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = preparePage(pageNo, pageSize, sortBy);
        Page<Listing> pageResult = listRepo.findAlByUsername(username, paging);
        return getResult(pageResult).stream()
                .map(ListingListDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ListingGetDTO getUserSingleListing(String username, Long id) {
        return new ListingGetDTO(listRepo.findByUsernameAndId(username, id).orElseThrow(ListingNotFound::new));
    }

    @Override
    public ListingGetDTO addListing(String username, Listing listing) {
        Optional<User> userSearch = userRepo.findByUsername(username);
        if (userSearch.isPresent()) {
            boolean free = true;
            User user = userSearch.get();
            if (user.isFreeListing()) {
                listing.getBaseListing().setType(Type.NEW);
                userRepo.save(user.toBuilder().freeListing(false).build());
            } else {
                listing.getBaseListing().setType(Type.PAYED);
                free = false;
            }
            listing.setEndDate(LocalDate.now().plusMonths(1));
            Optional<Listing> search = listRepo.findById(listRepo.save(listing).getId());
            if (search.isPresent()) {
                subService.checkSubscriptions(search.get());
                if (!free)
                    service.decreaseBalance(username, Double.valueOf(defaultPrice), search.get().getId());
                return new ListingGetDTO(search.get());
            }
            throw new ListingNotFound();
        }
        throw new UserNotFound();
    }

    @Override
    public ListingGetDTO updateListing(String username, Listing listing) {
        return new ListingGetDTO(listRepo.findById(listRepo.save(listing).getId()).orElseThrow(ListingNotFound::new));
    }

    @Override
    public ListingGetDTO deleteListing(String username, Long id) {
        Optional<Listing> result = listRepo.findByUsernameAndId(username, id);
        if (result.isPresent()) {
            result.get().getBaseListing().setActive(false);
            listRepo.save(result.get());
            return new ListingGetDTO(result.get());
        }
        throw new ListingNotFound();
    }

    @Override
    public ListingGetDTO makeVip(String username, Long id) {
        userRepo.findByUsername(username).orElseThrow(UserNotFound::new);
        Listing result = listRepo.findByUsernameAndId(username, id).orElseThrow(ListingNotFound::new);
        if (result.getBaseListing().getType().equals(Type.VIP))
            throw new ThisListingIsVipAlready();
        result.getBaseListing().setType(Type.VIP);
        result.setEndDate(LocalDate.now().plusMonths(1));
        service.decreaseBalance(username, Double.valueOf(vipPrice), result.getId());
        listRepo.save(result);
        return new ListingGetDTO(result);
    }

    @Override
    public List<MakeDTO> getAllMakes() {
        return makeRepo.findAll().stream().map(MakeDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<ModelDTO> getAllMakeModels(Long id) {
        return modelRepo.findByMakeId(id).stream().map(ModelDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<CityDTO> getCities() {
        return cityRepo.findAll().stream().map(CityDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<FuelType> getFuelTypes() {
        return Arrays.asList(FuelType.values());
    }

    @Override
    public List<BodyType> getBodyTypes() {
        return Arrays.asList(BodyType.values());
    }
}
