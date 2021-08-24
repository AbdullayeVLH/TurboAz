package az.code.turboplus.dtos;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ListingCreationDTO {
    private Long makeId;
    private Long modelId;
    private Long cityId;
    @Min(value = 1900, message = "Price must be between 1900 and 2030")
    @Max(value = 2030, message = "Price must be between 1900 and 2030")
    private Integer year;
    @Min(value = 0, message = "Price must be between zero and one billion")
    @Max(value = 1000000000, message = "Price must be between zero and one billion")
    private Integer price;
    @Min(value = 0, message = "Price must be between zero and one billion")
    @Max(value = 1000000000, message = "Price must be between zero and one billion")
    private Integer mileage;
    @Min(value = 0, message = "Interval must be between 0 and 4")
    @Max(value = 4, message = "Interval must be between 0 and 4")
    private Integer color;
    @Min(value = 0, message = "Interval must be between 0 and 3")
    @Max(value = 3, message = "Interval must be between 0 and 3")
    private Integer fuelType;
    @Min(value = 0, message = "Interval must be between 0 and 1")
    @Max(value = 1, message = "Interval must be between 0 and 1")
    private Integer bodyType;
    @Min(value = 0, message = "Interval must be between 0 and 2")
    @Max(value = 2, message = "Interval must be between 0 and 2")
    private Integer gearBox;
    private Boolean autoPay;
    private Boolean creditOption;
    private Boolean barterOption;
    private Boolean leaseOption;
    private Boolean cashOption;
    private String description;
    @NotNull
    private String thumbnailUrl;
    private List<Integer> carSpecIds;
}
