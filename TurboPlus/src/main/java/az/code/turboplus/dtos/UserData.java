package az.code.turboplus.dtos;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class UserData {
    @NotNull
    @Size(min = 2, message = "Username must be at least 2 character long")
    private String name;
    @NotNull
    @Size(min = 5, message = "Username must be at least 5 character long")
    private String username;
    @NotNull
    @Pattern(regexp = "\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|\n" +
            "2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|\n" +
            "4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$")
    private String phoneNumber;
    @NotBlank
    @Email
    private String email;
    @FutureOrPresent
    private LocalDateTime registrationTime;
}
