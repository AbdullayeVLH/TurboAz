package az.code.turboplus.dtos;

import az.code.turboplus.models.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {
    private String fullName;
    private String userName;
    private String phone;

    public UserDTO(User data) {
        this.fullName = data.getName();
        this.userName = data.getUsername();
        this.phone = data.getPhoneNumber();
    }
}
