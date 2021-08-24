package az.code.turboplus.dtos;

import az.code.turboplus.enums.*;
import az.code.turboplus.models.Listing;
import az.code.turboplus.models.Photo;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ListingGetDTO {
    private Long id;
    private Boolean isActive;
    private UserDTO user;
    private MakeDTO make;
    private ModelDTO model;
    private CityDTO city;

    @Min(value = 1900, message = "Price must be between 1900 and 2030")
    @Max(value = 2030, message = "Price must be between 1900 and 2030")
    private Integer year;
    @Min(value = 0, message = "Price must be between zero and one billion")
    @Max(value = 1000000000, message = "Price must be between zero and one billion")
    private Integer price;
    @Min(value = 0, message = "Price must be between zero and one billion")
    @Max(value = 1000000000, message = "Price must be between zero and one billion")
    private Integer mileage;

    private Type type;
    private FuelType fuelType;
    private BodyType bodyType;
    private Color color;
    private GearBox gearBox;

    private Boolean autoPay;
    private Boolean creditOption;
    private Boolean barterOption;
    private Boolean leaseOption;
    private Boolean cashOption;
    private String description;
    private String thumbnailUrl;
    private LocalDateTime updatedAt;
    private List<CarSpecDTO> carSpecs;
    private List<String> imageUrls;

    public ListingGetDTO(Listing data) {
        this.id = data.getId();
        this.isActive = data.getBaseListing().isActive();
        this.user = new UserDTO(data.getUser());
        this.make = new MakeDTO(data.getBaseListing().getMake());
        this.model = new ModelDTO(data.getBaseListing().getModel());
        this.city = new CityDTO(data.getBaseListing().getCity());
        this.year = data.getBaseListing().getYear();
        this.price = data.getBaseListing().getPrice();
        this.mileage = data.getBaseListing().getMileage();
        this.type = data.getBaseListing().getType();
        this.fuelType = data.getFuelType();
        this.bodyType = data.getBodyType();
        this.color = data.getColor();
        this.gearBox = data.getGearBox();
        this.autoPay = data.isAutoPay();
        this.creditOption = data.getBaseListing().getCreditOption();
        this.barterOption = data.getBaseListing().getBarterOption();
        this.leaseOption = data.getBaseListing().getLeaseOption();
        this.cashOption = data.getBaseListing().getCashOption();
        this.description = data.getDescription();
        this.updatedAt = LocalDateTime.now();
        this.thumbnailUrl = data.getBaseListing().getThumbnail();
        this.imageUrls = data.getPhotos().stream().map(Photo::getUrl).collect(Collectors.toList());
        this.carSpecs = data.getCarSpecs().stream().map(CarSpecDTO::new).collect(Collectors.toList());
    }
}
