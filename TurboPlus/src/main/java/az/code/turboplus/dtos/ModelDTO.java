package az.code.turboplus.dtos;

import az.code.turboplus.models.Model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ModelDTO {
    private Long id;
    private String name;

    public ModelDTO(Model model) {
        this.id = model.getId();
        this.name = model.getName();
    }
}
