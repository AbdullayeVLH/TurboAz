package az.code.turboplus.services;

import az.code.turboplus.dtos.SubscriptionDTO;
import az.code.turboplus.dtos.SubscriptionListDTO;
import az.code.turboplus.exceptions.SubscriptionNotFound;
import az.code.turboplus.exceptions.TooManySubscriptions;
import az.code.turboplus.models.Listing;
import az.code.turboplus.models.Subscription;
import az.code.turboplus.models.User;
import az.code.turboplus.repositories.SubscriptionRepository;
import az.code.turboplus.utils.MailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static az.code.turboplus.utils.SpecificationUtilSimple.*;

@Service

public class SubscriptionServiceImpl implements SubscriptionService {

    @Value("${mail.subscription.subject}")
    private String mailSubject;
    @Value("${mail.subscription.context}")
    private String mailContext;
    @Value("${mail.listing.url.base-url}")
    private String listingBaseUrl;

    private final SubscriptionRepository subRepo;
    private final MailUtil mailUtil;

    public SubscriptionServiceImpl(SubscriptionRepository subRepo, MailUtil mailUtil) {
        this.subRepo = subRepo;
        this.mailUtil = mailUtil;
    }

    @Override
    public List<SubscriptionListDTO> getSubscriptions(String username) {
        return subRepo.findByUsername(username).stream()
                .map(SubscriptionListDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionListDTO addSubscription(String username, SubscriptionDTO subscription) {
        Integer countSubs = subRepo.countSubscriptionByUser(User.builder().username(username).build());
        if (countSubs > 5) throw new TooManySubscriptions();
        Subscription inserted = subRepo.save(new Subscription(subscription).toBuilder()
                .updateTime(LocalDateTime.now())
                .creationTime(LocalDateTime.now())
                .isActive(true)
                .user(User.builder().username(username).build())
                .build());
        return new SubscriptionListDTO(subRepo.findByUsernameAndIdAndActive(username, inserted.getId())
                .orElseThrow(SubscriptionNotFound::new));
    }

    @Override
    public SubscriptionListDTO updateSubscriptionById(String username, Long id, SubscriptionDTO subscription) {
        Subscription search = subRepo.findByUsernameAndIdAndActive(username, id).orElseThrow(SubscriptionNotFound::new);
        subRepo.save(new Subscription(subscription).toBuilder()
                .id(id)
                .creationTime(search.getCreationTime())
                .updateTime(LocalDateTime.now())
                .isActive(true)
                .user(User.builder().username(username).build())
                .build());
        return new SubscriptionListDTO(search);
    }

    @Override
    public void checkSubscriptions(Listing data) {
        List<Subscription> subscriptions = subRepo.findAllByActive(sameStringForSubs(data.getBaseListing().getModel().getName(), "model")
                .and(sameStringForSubs(data.getBaseListing().getMake().getName(), "make"))
                .and(sameStringForSubs(data.getBaseListing().getCity().getName(), "city"))
                .and(sameBodyTypeForSubs(data.getBodyType().toString()))
                .and(sameColorForSubs(data.getColor().toString()))
                .and(sameFuelTypeForSubs(data.getFuelType().toString()))
                .and(sameGearBoxForSubs(data.getGearBox().toString()))
                .and(hasBooleanOptionForSubs(data.getBaseListing().getCreditOption(), "loanOption"))
                .and(hasBooleanOptionForSubs(data.getBaseListing().getBarterOption(), "barterOption"))
                .and(hasBooleanOptionForSubs(data.getBaseListing().getCashOption(), "cashOption"))
                .and(hasBooleanOptionForSubs(data.getBaseListing().getLeaseOption(), "leaseOption"))
                .and(integerBetween(data.getBaseListing().getMileage(), "mileage"))
                .and(integerBetween(data.getBaseListing().getPrice(), "price"))
                .and(integerBetween(data.getBaseListing().getYear(), "releaseDate")));
        for (Subscription subscription : subscriptions) {
            mailUtil.sendNotificationEmail(subscription.getUser().getEmail(),
                    mailSubject.formatted(subscription.getName()),
                    mailContext.formatted(listingBaseUrl.formatted(data.getId())));
        }
    }

    @Override
    public SubscriptionListDTO findSubscriptionsById(String username, Long id) {
        return new SubscriptionListDTO(subRepo.findByUsernameAndIdAndActive(username, id)
                .orElseThrow(SubscriptionNotFound::new));
    }

    @Override
    public SubscriptionListDTO deleteSubscriptionById(String username, Long id) {
        Subscription subscription = subRepo.findByUsernameAndIdAndActive(username, id).orElseThrow(SubscriptionNotFound::new);
        subscription.setIsActive(false);
        return new SubscriptionListDTO(subRepo.save(subscription));
    }
}
