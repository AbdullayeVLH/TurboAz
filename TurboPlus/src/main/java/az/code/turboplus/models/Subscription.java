package az.code.turboplus.models;

import az.code.turboplus.dtos.SubscriptionDTO;
import az.code.turboplus.enums.BodyType;
import az.code.turboplus.enums.Color;
import az.code.turboplus.enums.FuelType;
import az.code.turboplus.enums.GearBox;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    @Column(name = "min_price")
    private Integer minPrice;
    @Column(name = "max_price")
    private Integer maxPrice;
    @Column(name = "min_mileage")
    private Integer minMileage;
    @Column(name = "max_mileage")
    private Integer maxMileage;
    @Column(name = "min_release_date")
    private Integer minReleaseDate;
    @Column(name = "max_release_date")
    private Integer maxReleaseDate;
    @Column(name = "loan_option")
    private Boolean loanOption;
    @Column(name = "barter_option")
    private Boolean barterOption;
    @Column(name = "lease_option")
    private Boolean leaseOption;
    @Column(name = "cash_option")
    private Boolean cashOption;
    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    private Color color;
    @Column(name = "body_type")
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;
    @Column(name = "fuel_type")
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    @Column(name = "gear_box")
    @Enumerated(EnumType.STRING)
    private GearBox gearBox;

    @ManyToOne
    @JoinColumn(name = "make_id", referencedColumnName = "id")
    private Make make;
    @ManyToOne
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private Model model;
    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;
    @ManyToOne(targetEntity = User.class)
    private User user;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "relation_sub_spec", joinColumns = @JoinColumn(name = "sub_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "spec_id", referencedColumnName = "id"))
    List<CarSpec> carSpecs;

    public Subscription(SubscriptionDTO data) {
        this.name = data.getName();
        this.make = Make.builder().id(data.getMakeId()).build();
        this.model = Model.builder().id(data.getModelId()).build();
        this.city = City.builder().id(data.getModelId()).build();
        this.minReleaseDate = data.getMinYear();
        this.maxReleaseDate = data.getMaxYear();
        this.minPrice = data.getMinPrice();
        this.maxPrice = data.getMaxPrice();
        this.minMileage = data.getMinMileage();
        this.maxMileage = data.getMaxMileage();
        this.color = data.getColor() != null ? Color.values()[data.getColor()] : null;
        this.fuelType = data.getFuelType() != null ? FuelType.values()[data.getFuelType()] : null;
        this.bodyType = data.getBodyType() != null ? BodyType.values()[data.getBodyType()] : null;
        this.gearBox = data.getGearBox() != null ? GearBox.values()[data.getGearBox()] : null;
        this.loanOption = data.getLoanOption();
        this.leaseOption = data.getLeaseOption();
        this.cashOption = data.getCashOption();
        this.barterOption = data.getBarterOption();
        this.carSpecs = data.getSpecs().stream()
                .map(value -> CarSpec.builder().id(value).build())
                .collect(Collectors.toList());
        this.updateTime = LocalDateTime.now();
    }
}
