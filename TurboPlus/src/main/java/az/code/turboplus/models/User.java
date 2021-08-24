package az.code.turboplus.models;

import az.code.turboplus.dtos.RegisterDTO;
import az.code.turboplus.dtos.UserData;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "users")
public class User {

    @Id
    private String username;
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Email
    private String email;
    @Column(name = "register_time")
    private LocalDateTime registerTime;
    private double balance;
    @Column(name = "free_listing")
    private boolean freeListing;

    @OneToMany
    @JoinColumn(name = "user_username", referencedColumnName = "username")
    private List<Transaction> transactions;
    @OneToMany
    @JoinColumn(name = "user_username", referencedColumnName = "username")
    private List<Listing> listing;
    @OneToMany
    @JoinColumn(name = "user_username", referencedColumnName = "username")
    private List<Subscription> subscriptions;

    public User(UserData data) {
        this.username = data.getUsername();
        this.name = data.getName();
        this.phoneNumber = data.getPhoneNumber();
        this.email = data.getEmail();
        this.registerTime = data.getRegistrationTime();
        this.balance = 0;
    }

    public User(RegisterDTO data) {
        this.username = data.getUsername();
        this.name = data.getName() + " " + data.getSurname();
        this.phoneNumber = data.getPhoneNumber();
        this.email = data.getEmail();
        this.registerTime = LocalDateTime.now();
        this.freeListing = true;
        this.balance = 0;
    }
}