package az.code.turboplus.services;

import az.code.turboplus.enums.PhotoType;
import az.code.turboplus.models.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    Photo upload(String username, Long id, MultipartFile multipartFile, PhotoType type);

    Photo getSingleImage(Long id, Long imageId);

    List<Photo> getImages(Long id);

    void delete(String username, Long id, Long imageId);
}
