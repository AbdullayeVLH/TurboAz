package az.code.turboplus.dtos;

import az.code.turboplus.models.Listing;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ListingListDTO {
    private Long id;
    private String makeName;
    private String modelName;
    private String cityName;

    @Min(value = 0, message = "Price must be between zero and one billion")
    @Max(value = 1000000000, message = "Price must be between zero and one billion")
    private Integer price;
    @Min(value = 0, message = "Price must be between zero and one billion")
    @Max(value = 1000000000, message = "Price must be between zero and one billion")
    private Integer mileage;
    @Min(value = 1900, message = "Price must be between 1900 and 2030")
    @Max(value = 2030, message = "Price must be between 1900 and 2030")
    private Integer year;

    private String thumbnailUrl;
    private LocalDateTime updatedAt;

    public ListingListDTO(Listing data) {
        this.id = data.getId();
        this.makeName = data.getBaseListing().getMake().getName();
        this.modelName = data.getBaseListing().getModel().getName();
        this.cityName = data.getBaseListing().getCity().getName();
        this.price = data.getBaseListing().getPrice();
        this.mileage = data.getBaseListing().getMileage();
        this.year = data.getBaseListing().getYear();
        this.thumbnailUrl = data.getBaseListing().getThumbnail();
        this.updatedAt = data.getBaseListing().getUpdateTime();
    }
}
