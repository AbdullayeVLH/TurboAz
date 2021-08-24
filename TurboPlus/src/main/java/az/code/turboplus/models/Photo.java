package az.code.turboplus.models;

import az.code.turboplus.enums.PhotoType;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @Enumerated(EnumType.ORDINAL)
    private PhotoType photoType;
    @ManyToOne(targetEntity = Listing.class)
    private Listing listing;
}
