package az.code.turboplus.dtos;

import az.code.turboplus.enums.BodyType;
import az.code.turboplus.enums.Color;
import az.code.turboplus.enums.FuelType;
import az.code.turboplus.enums.GearBox;
import az.code.turboplus.models.Subscription;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SubscriptionListDTO {
    private Long subId;
    private boolean isActive;
    private String name;
    private MakeDTO make;
    private ModelDTO model;
    private CityDTO city;
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
    private FuelType fuelType;
    private BodyType bodyType;
    private Color color;
    private GearBox gearBox;
    private Boolean loanOption;
    private Boolean leaseOption;
    private Boolean cashOption;
    private Boolean barterOption;
    private LocalDateTime creationDate;
    private List<CarSpecDTO> specs;

    public SubscriptionListDTO(Subscription sub) {
        this.subId = sub.getId();
        this.name = sub.getName();
        this.make = MakeDTO.builder()
                .id(sub.getMake().getId())
                .name(sub.getMake().getName()).build();
        this.model = ModelDTO.builder()
                .id(sub.getModel().getId())
                .name(sub.getModel().getName())
                .build();
        this.city = CityDTO.builder()
                .id(sub.getCity().getId())
                .name(sub.getCity().getName())
                .build();
        this.minYear = sub.getMinReleaseDate();
        this.maxYear = sub.getMaxReleaseDate();
        this.minPrice = sub.getMinPrice();
        this.maxPrice = sub.getMaxPrice();
        this.minMileage = sub.getMinMileage();
        this.maxMileage = sub.getMaxMileage();
        this.fuelType = sub.getFuelType();
        this.bodyType = sub.getBodyType();
        this.color = sub.getColor();
        this.gearBox = sub.getGearBox();
        this.creationDate = sub.getCreationTime();
        this.leaseOption = sub.getLeaseOption();
        this.loanOption = sub.getLoanOption();
        this.barterOption = sub.getBarterOption();
        this.cashOption = sub.getCashOption();
        this.specs = sub.getCarSpecs().stream()
                .map(carSpec -> CarSpecDTO.builder().name(carSpec.getName()).build())
                .collect(Collectors.toList());
    }
}
