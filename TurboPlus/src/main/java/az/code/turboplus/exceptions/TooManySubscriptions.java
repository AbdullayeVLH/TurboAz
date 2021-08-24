package az.code.turboplus.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class TooManySubscriptions extends RuntimeException {
    public TooManySubscriptions() {
        super("You can't have more than 5 subscriptions.");
    }
}
