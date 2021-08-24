package az.code.turboplus.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ImageNotFound extends RuntimeException {
    public ImageNotFound() {
        super("This image does not exists.");
    }
}
