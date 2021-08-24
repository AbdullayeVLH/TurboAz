package az.code.turboplus.services;

import az.code.turboplus.dtos.TransactionListDTO;
import az.code.turboplus.exceptions.ListingNotFound;
import az.code.turboplus.exceptions.NotEnoughBalance;
import az.code.turboplus.exceptions.UserNotFound;
import az.code.turboplus.models.Listing;
import az.code.turboplus.models.Transaction;
import az.code.turboplus.models.User;
import az.code.turboplus.repositories.ListingRepository;
import az.code.turboplus.repositories.TransactionRepository;
import az.code.turboplus.repositories.UserRepository;
import az.code.turboplus.utils.MailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Value("${mail.listing.url.base-url}")
    private String listingBaseUrl;
    @Value("${mail.profile.balance.increase.subject}")
    private String increaseBalanceSubject;
    @Value("${mail.profile.balance.increase.context}")
    private String increaseBalanceContext;
    @Value("${mail.profile.balance.decrease.subject}")
    private String decreaseBalanceSubject;
    @Value("${mail.profile.balance.decrease.context}")
    private String decreaseBalanceContext;
    @Value("${mail.profile.balance.auto-pay.subject}")
    private String listingPaymentSubject;
    @Value("${mail.profile.balance.auto-pay.context}")
    private String listingPaymentContext;

    private final UserRepository userRepo;
    private final TransactionRepository transRepo;
    private final ListingRepository listRepo;
    private final MailUtil mailUtil;

    public UserServiceImpl(UserRepository userRepo, TransactionRepository transRepo, ListingRepository listRepo, MailUtil mailUtil) {
        this.userRepo = userRepo;
        this.transRepo = transRepo;
        this.listRepo = listRepo;
        this.mailUtil = mailUtil;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public double getUserBalance(String username) {
        return userRepo.findBalance(username);
    }

    @Override
    public List<TransactionListDTO> getUserTransactions(String username) {
        return transRepo.findAllByUsername(username).stream()
                .map(TransactionListDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public double increaseBalance(String username, Double amount) {
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            double balance = userRepo.findBalance(username);
            balance += amount;
            transRepo.save(Transaction.builder()
                    .listingId(null)
                    .amount(amount)
                    .user(User.builder().username(username).build())
                    .time(LocalDateTime.now())
                    .build());
            userRepo.save(user.get().toBuilder().balance(balance).build());
            mailUtil.sendNotificationEmail(user.get().getEmail(), increaseBalanceSubject,
                    increaseBalanceContext.formatted(amount, balance));
            return balance;
        }
        throw new UserNotFound();
    }

    @Override
    public double decreaseBalance(String username, Double amount, Long listingId) {
        User user = userRepo.findByUsername(username).orElseThrow(UserNotFound::new);
        Listing listing;
        double balance = userRepo.findBalance(username);
        if (balance - amount < 0) {
            //TODO: Create new body for better understanding
            mailUtil.sendNotificationEmail(user.getEmail(), decreaseBalanceSubject,
                    decreaseBalanceContext.formatted(listingBaseUrl.formatted(listingId)));
            throw new NotEnoughBalance();
        } else {
            listing = listRepo.findByUsernameAndId(username, listingId).orElseThrow(ListingNotFound::new);
            listing.toBuilder().endDate(LocalDate.now().plusMonths(1));
            listRepo.save(listing);
            balance -= amount;
        }
        transRepo.save(Transaction.builder()
                .listingId(listingId)
                .amount(amount)
                .time(LocalDateTime.now())
                .user(user)
                .build());
        userRepo.save(user.toBuilder().balance(balance).build());
        //TODO: Add html to mail service.
        mailUtil.sendNotificationEmail(user.getEmail(), listingPaymentSubject,
                listingPaymentContext.formatted(amount, listingId, balance));
        return balance;
    }
}
