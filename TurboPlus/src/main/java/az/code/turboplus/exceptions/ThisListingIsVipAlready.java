package az.code.turboplus.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ThisListingIsVipAlready extends RuntimeException {
    public ThisListingIsVipAlready() {
        super("This listing is already vip.");
    }
}
