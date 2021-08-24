package az.code.turboplus.dtos;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SubscriptionDTO {
    private Long subId;
    @NotNull
    @Size(min = 2, message = "Name must be at least 2 character long" )
    private String name;
    @NotNull
    private Long makeId;
    @NotNull
    private Long modelId;
    @NotNull
    private Long cityId;
    @Min(value = 1900, message = "Price must be between 1900 and 2030")
    @Max(value = 2030, message = "Price must be between 1900 and 2030")
    private Integer minYear;
    @Min(value = 1900, message = "Price must be between 1900 and 2030")
    @Max(value = 2030, message = "Price must be between 1900 and 2030")
    private Integer maxYear;
    @Min(value = 0, message = "Price must be between zero and one billion")
    @Max(value = 1000000000, message = "Price must be between zero and one billion")
    private Integer minPrice;
    @Min(value = 0, message = "Price must be between zero and one billion")
    @Max(value = 1000000000, message = "Price must be between zero and one billion")
    private Integer maxPrice;
    @Min(value = 0, message = "Price must be between zero and one billion")
    @Max(value = 1000000000, message = "Price must be between zero and one billion")
    private Integer minMileage;
    @Min(value = 0, message = "Price must be between zero and one billion")
    @Max(value = 1000000000, message = "Price must be between zero and one billion")
    private Integer maxMileage;
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
    private Boolean loanOption;
    private Boolean leaseOption;
    private Boolean cashOption;
    private Boolean barterOption;
    private List<Long> specs;
}
