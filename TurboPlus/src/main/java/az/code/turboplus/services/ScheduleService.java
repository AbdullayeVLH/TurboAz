package az.code.turboplus.services;

import az.code.turboplus.enums.Type;
import az.code.turboplus.exceptions.NotEnoughBalance;
import az.code.turboplus.models.Listing;
import az.code.turboplus.models.User;
import az.code.turboplus.repositories.ListingRepository;
import az.code.turboplus.repositories.UserRepository;
import az.code.turboplus.utils.MailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Value("${mail.listing.ending-reminder.subject}")
    private String subject;
    @Value("${mail.listing.ending-reminder.context.header}")
    private String text;
    @Value("${profile.listing.base-url}")
    private String profileListingBaseUrl;
    @Value("${listing.default.price}")
    private String defaultPrice;

    private final MailUtil mailUtil;
    private final ListingRepository listRepo;
    private final UserRepository userRepo;
    private final UserServiceImpl userService;

    public ScheduleService(MailUtil mailUtil, ListingRepository listRepo, UserRepository userRepo, UserServiceImpl userService) {
        this.mailUtil = mailUtil;
        this.listRepo = listRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 9 1 * ?")
    private void updateFreeListings() {
        List<User> users = userRepo.findAll();
        for (User user : users) {
            userRepo.save(user.toBuilder().freeListing(true).build());
        }
    }

    @Scheduled(cron = "0 0 6 * * ?")
    private void createNotifications() {
        checkListingDateAndSendMail();
        checkUsersForAutoPay();
    }

    private void checkListingDateAndSendMail() {
        List<User> users = userRepo.findAll();
        List<Long> listId = new LinkedList<>();
        for (User user : users) {
            List<Listing> list = listRepo.findAllByUserAndEndDate(user, LocalDate.now().plusDays(1));
            for (Listing listing : list) {
                listId.add(listing.getId());
            }
            if (listId.size() != 0) {
                mailUtil.sendNotificationEmail(user.getEmail(),
                        subject, text.formatted(user.getUsername()) +
                                profileListingBaseUrl.formatted(user.getUsername()) +
                                listId.stream().map(Object::toString)
                                        .collect(Collectors.joining("\n" +
                                                profileListingBaseUrl.formatted(user.getUsername()))));
            }
            listId.clear();
        }
    }

    private void checkUsersForAutoPay() {
        LocalDate date = LocalDate.now();
        List<Listing> list = listRepo.findAllActiveByAutoPay(date);
        for (Listing listing : list) {
            try {
                userService.decreaseBalance(listing.getUser().getUsername(),
                        Double.valueOf(defaultPrice), listing.getId());
                listing.getBaseListing().setType(Type.PAYED);
                listing.getBaseListing().setUpdateTime(LocalDateTime.now());
                listing.setEndDate(LocalDate.now().plusMonths(1));
            } catch (NotEnoughBalance exception) {
                listing.getBaseListing().setActive(false);
                listing.getBaseListing().setUpdateTime(LocalDateTime.now());
                break;
            } finally {
                listRepo.save(listing);
            }
        }
    }
}
