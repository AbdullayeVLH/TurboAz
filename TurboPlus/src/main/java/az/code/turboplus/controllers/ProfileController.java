package az.code.turboplus.controllers;

import az.code.turboplus.dtos.*;
import az.code.turboplus.exceptions.*;
import az.code.turboplus.models.Listing;
import az.code.turboplus.services.ListingService;
import az.code.turboplus.services.SubscriptionService;
import az.code.turboplus.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    SubscriptionService subService;
    ListingService listService;
    UserService userService;

    public ProfileController(SubscriptionService subService, ListingService listService, UserService userService) {
        this.subService = subService;
        this.listService = listService;
        this.userService = userService;
    }

    @ExceptionHandler(ListingNotFound.class)
    public ResponseEntity<String> handleNotFound(ListingNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> handleNotFound(UserNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughBalance.class)
    public ResponseEntity<String> handleGroupNotEmpty(NotEnoughBalance e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ThisListingIsVipAlready.class)
    public ResponseEntity<String> handleNotFound(ThisListingIsVipAlready e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(SubscriptionNotFound.class)
    public ResponseEntity<String> handleNotFound(SubscriptionNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TooManySubscriptions.class)
    public ResponseEntity<String> handleNotFound(TooManySubscriptions e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ValidationIsIncorrect.class)
    public ResponseEntity<String> handleNotFound(ValidationIsIncorrect e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @RolesAllowed("user")
    @PostMapping("/listings")
    public ResponseEntity<ListingGetDTO> addListing(
            @RequestAttribute("user") UserData user,
            @Valid @RequestBody ListingCreationDTO listingCreationDTO
    ) {
        return ResponseEntity.ok(listService.addListing(user.getUsername(), new Listing(listingCreationDTO, user)));
    }

    @RolesAllowed("user")
    @PutMapping("/listings/{id}")
    public ResponseEntity<ListingGetDTO> updateListing(
            @RequestAttribute("user") UserData user,
            @PathVariable("id") Long id,
            @Valid @RequestBody ListingCreationDTO listingCreationDTO
    ) {
        return ResponseEntity.ok(listService.updateListing(user.getUsername(), new Listing(listingCreationDTO, user)
                .toBuilder()
                .id(id)
                .build())
        );
    }

    @RolesAllowed("user")
    @GetMapping("/listings")
    public ResponseEntity<List<ListingListDTO>> getProfileListings(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "baseListing") String sortBy,
            @RequestAttribute("user") UserData user
    ) {
        return ResponseEntity.ok(listService.getUserListings(user.getUsername(), pageNo, pageSize, sortBy));
    }

    @RolesAllowed("user")
    @GetMapping("/listings/{id}")
    public ResponseEntity<ListingGetDTO> getProfileListings(
            @RequestAttribute("user") UserData user,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(listService.getUserSingleListing(user.getUsername(), id));
    }

    @RolesAllowed("user")
    @DeleteMapping("/listings/{id}")
    ResponseEntity<ListingGetDTO> deleteListing(@RequestAttribute("user") UserData user, @PathVariable Long id) {
        return ResponseEntity.ok(listService.deleteListing(user.getUsername(), id));
    }

    @RolesAllowed("user")
    @PutMapping("/listings/{id}/makevip")
    ResponseEntity<ListingGetDTO> makeVip(@RequestAttribute("user") UserData user, @PathVariable Long id) {
        return ResponseEntity.ok(listService.makeVip(user.getUsername(), id));
    }

    @RolesAllowed("user")
    @PostMapping("/subscriptions")
    public ResponseEntity<SubscriptionListDTO> addSubscription(
            @RequestAttribute("user") UserData user,
            @Valid @RequestBody SubscriptionDTO subscription
    ) {
        return ResponseEntity.ok(subService.addSubscription(user.getUsername(), subscription));
    }

    @RolesAllowed("user")
    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionListDTO>> getSubscriptions(@RequestAttribute("user") UserData user) {
        return ResponseEntity.ok(subService.getSubscriptions(user.getUsername()));
    }

    @RolesAllowed("user")
    @GetMapping("/subscriptions/{id}")
    public ResponseEntity<SubscriptionListDTO> getSubscriptionById(
            @RequestAttribute("user") UserData user,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(subService.findSubscriptionsById(user.getUsername(), id));
    }


    @RolesAllowed("user")
    @PutMapping("/subscriptions/{id}")
    ResponseEntity<SubscriptionListDTO> updateSubscription(
            @RequestAttribute("user") UserData user,
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionDTO subscription
    ) {
        return ResponseEntity.ok(subService.updateSubscriptionById(user.getUsername(), id, subscription));
    }

    @RolesAllowed("user")
    @DeleteMapping("/subscriptions/{id}")
    ResponseEntity<SubscriptionListDTO> deleteSubscription(@RequestAttribute("user") UserData user, @PathVariable Long id) {
        return ResponseEntity.ok(subService.deleteSubscriptionById(user.getUsername(), id));
    }

    @RolesAllowed("user")
    @GetMapping("/wallet")
    ResponseEntity<Double> getUserBalance(@RequestAttribute("user") UserData user) {
        return ResponseEntity.ok(userService.getUserBalance(user.getUsername()));
    }

    @RolesAllowed("user")
    @PutMapping("/wallet/increase")
    ResponseEntity<Double> increaseBalance(@RequestAttribute("user") UserData user, @Valid @RequestBody AmountDTO amount) {
        return ResponseEntity.ok(userService.increaseBalance(user.getUsername(), amount.getAmount()));
    }

    @RolesAllowed("user")
    @GetMapping("/wallet/transactions")
    ResponseEntity<List<TransactionListDTO>> getUserTransactions(@RequestAttribute("user") UserData user) {
        return ResponseEntity.ok(userService.getUserTransactions(user.getUsername()));
    }
}
