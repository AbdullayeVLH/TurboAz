package az.code.turboplus.models;

import az.code.turboplus.dtos.ListingCreationDTO;
import az.code.turboplus.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "base_listings")
public class BaseListing implements Comparable<BaseListing> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive = true;
    private Boolean creditOption;
    private Boolean barterOption;
    private Boolean leaseOption;
    private Boolean cashOption;
    private Integer price;
    private Integer year;
    private Integer mileage;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;
    @ManyToOne
    @JoinColumn(name = "make_id", referencedColumnName = "id")
    private Make make;
    @ManyToOne
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private Model model;
    private String thumbnail;
    private LocalDateTime updateTime;

    public BaseListing(ListingCreationDTO data) {
        this.creditOption = data.getCreditOption();
        this.barterOption = data.getBarterOption();
        this.leaseOption = data.getLeaseOption();
        this.cashOption = data.getCashOption();
        this.price = data.getPrice();
        this.year = data.getYear();
        this.mileage = data.getMileage();
        this.city = City.builder().id(data.getCityId()).build();
        this.make = Make.builder().id(data.getMakeId()).build();
        this.model = Model.builder().id(data.getModelId()).build();
        this.thumbnail = data.getThumbnailUrl();
        this.updateTime = LocalDateTime.now();
    }

    @Override
    public int compareTo(BaseListing second) {
        return -this.updateTime.compareTo(second.updateTime);
    }
}
