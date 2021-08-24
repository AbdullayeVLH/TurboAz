package az.code.turboplus.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotEnoughBalance extends RuntimeException {
    public NotEnoughBalance() {
        super("Your balance does not have enough amount for this operation.");
    }
}
