package az.code.turboplus.dtos;

import az.code.turboplus.enums.PhotoType;
import az.code.turboplus.models.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private String url;
    @Enumerated(EnumType.STRING)
    private PhotoType photoType;

    public ImageDTO(Photo data) {
        this.url = data.getUrl();
        this.photoType = data.getPhotoType();
    }
}
