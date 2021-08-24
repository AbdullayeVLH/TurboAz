package az.code.turboplus.services;

import az.code.turboplus.enums.PhotoType;
import az.code.turboplus.exceptions.ImageNotFound;
import az.code.turboplus.exceptions.ListingNotFound;
import az.code.turboplus.models.Listing;
import az.code.turboplus.models.Photo;
import az.code.turboplus.repositories.ListingRepository;
import az.code.turboplus.repositories.PhotoRepository;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${firebase.upload.url}")
    private String baseUrl;
    @Value("${firebase.bucket.url}")
    private String bucketName;
    @Value("${firebase.configuration.url}")
    private String config;
    String tempUrl = "";

    private final PhotoRepository photoRepo;
    private final ListingRepository listRepo;

    public ImageServiceImpl(PhotoRepository photoRepo, ListingRepository listRepo) {
        this.photoRepo = photoRepo;
        this.listRepo = listRepo;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public Photo upload(String username, Long id, MultipartFile multipartFile, PhotoType type) {
        listRepo.findByUsernameAndId(username, id).orElseThrow(ListingNotFound::new);
        try {
            String fileName = multipartFile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().concat(this.getExtension(Objects.requireNonNull(fileName)));
            File file = this.convertToFile(multipartFile, fileName);
            tempUrl = this.uploadFile(file, fileName);
            file.delete();
            photoRepo.save(new Photo().toBuilder()
                    .url(tempUrl)
                    .photoType(type)
                    .listing(new Listing().toBuilder().id(id).build())
                    .build());
            return Photo.builder().url(tempUrl).photoType(type).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Photo.builder().url("ERROR: 400").build();
        }
    }

    @Override
    public Photo getSingleImage(Long id, Long imageId) {
        return photoRepo.findByListingIdAndId(id, imageId).orElseThrow(ImageNotFound::new);
    }

    @Override
    public List<Photo> getImages(Long id) {
        return photoRepo.findAllByListingId(id);
    }

    @Override
    @Transactional
    public void delete(String username, Long id, Long imageId) {
        photoRepo.deleteByListingAndId(listRepo.findByUsernameAndId(username, id)
                .orElseThrow(ListingNotFound::new), imageId);
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        String type = Files.probeContentType(file.toPath());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(type).build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(config));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(baseUrl, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
