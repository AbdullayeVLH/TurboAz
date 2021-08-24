package az.code.turboplus.dtos;

import az.code.turboplus.models.Make;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MakeDTO {
    private Long id;
    private String name;

    public MakeDTO(Make make) {
        this.id = make.getId();
        this.name = make.getName();
    }
}
