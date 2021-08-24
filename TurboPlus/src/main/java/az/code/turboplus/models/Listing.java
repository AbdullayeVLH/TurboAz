package az.code.turboplus.models;

import az.code.turboplus.dtos.ListingCreationDTO;
import az.code.turboplus.dtos.UserData;
import az.code.turboplus.enums.BodyType;
import az.code.turboplus.enums.Color;
import az.code.turboplus.enums.FuelType;
import az.code.turboplus.enums.GearBox;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "listings")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "auto_pay")
    private boolean autoPay;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "base_id", referencedColumnName = "id")
    private BaseListing baseListing;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    private Color color;
    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type")
    private FuelType fuelType;
    @Enumerated(EnumType.STRING)
    @Column(name = "body_type")
    private BodyType bodyType;
    @Column(name = "gear_box")
    @Enumerated(EnumType.STRING)
    private GearBox gearBox;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "listing_id", referencedColumnName = "id")
    private Set<Photo> photos;
    @ManyToOne(targetEntity = User.class)
    private User user;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "relation_list_spec", joinColumns = @JoinColumn(name = "list_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "spec_id", referencedColumnName = "id"))
    private List<CarSpec> carSpecs;

    public Listing(ListingCreationDTO data, UserData user) {
        this.autoPay = data.getAutoPay();
        this.creationDate = LocalDateTime.now();
        this.description = data.getDescription();
        this.color = Color.values()[data.getColor()];
        this.fuelType = FuelType.values()[data.getFuelType()];
        this.bodyType = BodyType.values()[data.getBodyType()];
        this.gearBox = GearBox.values()[data.getGearBox()];
        this.user = new User(user);
        this.baseListing = new BaseListing(data);
        this.carSpecs = data.getCarSpecIds().stream().map(id -> new CarSpec()
                .toBuilder().id(Long.valueOf(id)).build())
                .collect(Collectors.toList());
    }
}
