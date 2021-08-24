package az.code.turboplus.dtos;

import az.code.turboplus.models.CarSpec;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CarSpecDTO {
    private String name;

    public CarSpecDTO(CarSpec data) {
        this.name = data.getName();
    }
}
