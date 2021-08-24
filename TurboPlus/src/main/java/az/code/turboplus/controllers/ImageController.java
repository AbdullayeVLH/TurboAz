package az.code.turboplus.controllers;

import az.code.turboplus.dtos.ImageDTO;
import az.code.turboplus.dtos.UserData;
import az.code.turboplus.enums.PhotoType;
import az.code.turboplus.exceptions.ImageNotFound;
import az.code.turboplus.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("api/v1/profile/listings")
public class ImageController {

    ImageService service;

    public ImageController(ImageService service) {
        this.service = service;
    }

    @ExceptionHandler(ImageNotFound.class)
    public ResponseEntity<String> handleNotFound(ImageNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("{id}/images/{imageId}")
    public ResponseEntity<ImageDTO> getListingSingleImage(@PathVariable("id") Long id, @PathVariable("imageId") Long imageId) {
        return ResponseEntity.ok(new ImageDTO(service.getSingleImage(id, imageId)));
    }

    @GetMapping("{id}/images")
    public ResponseEntity<List<ImageDTO>> getListingImages(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getImages(id).stream().map(ImageDTO::new).collect(Collectors.toList()));
    }

    @RolesAllowed("user")
    @PostMapping("{id}/image")
    public ResponseEntity<ImageDTO> uploadOtherImage(
            @RequestAttribute("user") UserData user,
            @PathVariable("id") Long id,
            @RequestBody MultipartFile file
    ) {
        return ResponseEntity.ok(new ImageDTO(service.upload(user.getUsername(), id, file, PhotoType.OTHER)));
    }

    @RolesAllowed("user")
    @PostMapping("{id}/setThumbnail")
    public ResponseEntity<ImageDTO> uploadThumbnailImage(
            @RequestAttribute("user") UserData user,
            @PathVariable("id") Long id,
            @RequestBody MultipartFile file
    ) {
        return ResponseEntity.ok(new ImageDTO(service.upload(user.getUsername(), id, file, PhotoType.FRONT)));
    }

    @RolesAllowed("user")
    @DeleteMapping("{id}/images/{imageId}")
    public ResponseEntity<String> deleteSingleImage(
            @RequestAttribute("user") UserData user,
            @PathVariable("id") Long id,
            @PathVariable("imageId") Long imageId) {
        service.delete(user.getUsername(), id, imageId);
        return ResponseEntity.ok("Deleted");
    }
}
