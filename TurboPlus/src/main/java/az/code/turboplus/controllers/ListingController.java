package az.code.turboplus.controllers;

import az.code.turboplus.dtos.*;
import az.code.turboplus.enums.BodyType;
import az.code.turboplus.enums.FuelType;
import az.code.turboplus.exceptions.ListingNotFound;
import az.code.turboplus.exceptions.UserNotFound;
import az.code.turboplus.services.ListingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1")
public class ListingController {

    ListingService listService;

    public ListingController(ListingService listService) {
        this.listService = listService;
    }

    @ExceptionHandler(ListingNotFound.class)
    public ResponseEntity<String> handleNotFound(ListingNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> handleNotFound(UserNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/listings/{id}")
    public ResponseEntity<ListingGetDTO> getSingleListing(@PathVariable("id") Long id) {
        return ResponseEntity.ok(listService.getSingleListing(id));
    }

    @GetMapping("/listings/vip")
    public ResponseEntity<List<ListingListDTO>> getVipListings(
            @RequestParam Integer limit,
            @RequestParam Integer skip
    ) {
        return ResponseEntity.ok(listService.getVipListings(skip, limit));
    }

    @GetMapping("/listings")
    public ResponseEntity<List<ListingListDTO>> getListings(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "update_time") String sortBy
    ) {
        return ResponseEntity.ok(listService.getListings(pageNo, pageSize, sortBy));
    }

    @GetMapping(value = "/listings/search")
    public ResponseEntity<List<ListingListDTO>> get(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minMileage,
            @RequestParam(required = false) Integer maxMileage,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) String bodyType,
            @RequestParam(required = false) Boolean loanOption,
            @RequestParam(required = false) Boolean barterOption,
            @RequestParam(required = false) Boolean leaseOption,
            @RequestParam(required = false) Boolean cashOption,
            @RequestParam(required = false, defaultValue = "10") Integer count,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {
        return new ResponseEntity<>(listService.search(SearchRequest.builder()
                .make(make).model(model)
                .location(location)
                .minYear(minYear).maxYear(maxYear)
                .minPrice(minPrice).maxPrice(maxPrice)
                .minMileage(minMileage).maxMileage(maxMileage)
                .fuelType(fuelType).bodyType(bodyType)
                .loanOption(loanOption).barterOption(barterOption).leaseOption(leaseOption).cashOption(cashOption)
                .build(), count, page), HttpStatus.OK);
    }

    @GetMapping("/user/{slug}/listings")
    public ResponseEntity<List<ListingListDTO>> getUserListings(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "update_time") String sortBy,
            @PathVariable("slug") String username
    ) {
        return ResponseEntity.ok(listService.getUserListings(username, pageNo, pageSize, sortBy));
    }

    @GetMapping("/data/makes")
    public ResponseEntity<List<MakeDTO>> getMakes() {
        return ResponseEntity.ok(listService.getAllMakes());
    }

    @GetMapping("/data/makes/{id}/models")
    public ResponseEntity<List<ModelDTO>> getModels(@PathVariable("id") Long id) {
        return ResponseEntity.ok(listService.getAllMakeModels(id));
    }

    @GetMapping("/data/locations")
    public ResponseEntity<List<CityDTO>> getCities() {
        return ResponseEntity.ok(listService.getCities());
    }

    @GetMapping("/data/fuelTypes")
    public ResponseEntity<List<FuelType>> getFuelTypes() {
        return ResponseEntity.ok(listService.getFuelTypes());
    }

    @GetMapping("/data/bodyTypes")
    public ResponseEntity<List<BodyType>> getBodyTypes() {
        return ResponseEntity.ok(listService.getBodyTypes());
    }
}
