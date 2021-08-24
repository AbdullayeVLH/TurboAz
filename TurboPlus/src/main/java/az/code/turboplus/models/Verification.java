package az.code.turboplus.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "verifications")
public class Verification {
    @Id
    String token;
    @OneToOne
    @JoinColumn(name = "user_username", referencedColumnName = "username")
    private User user;
}
