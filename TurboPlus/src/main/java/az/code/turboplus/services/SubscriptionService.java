package az.code.turboplus.services;

import az.code.turboplus.dtos.SubscriptionDTO;
import az.code.turboplus.dtos.SubscriptionListDTO;
import az.code.turboplus.models.Listing;
import az.code.turboplus.models.Subscription;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SubscriptionService {

    List<SubscriptionListDTO> getSubscriptions(String username);

    SubscriptionListDTO addSubscription(String username, SubscriptionDTO subscription);

    SubscriptionListDTO findSubscriptionsById(String username, Long id);

    SubscriptionListDTO deleteSubscriptionById(String username, Long id);

    SubscriptionListDTO updateSubscriptionById(String username, Long id, SubscriptionDTO subscription);

    void checkSubscriptions(Listing data);
}
