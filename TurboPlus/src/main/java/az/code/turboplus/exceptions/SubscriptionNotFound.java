package az.code.turboplus.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SubscriptionNotFound extends RuntimeException {
    public SubscriptionNotFound() {
        super("This subscription does not exists.");
    }
}
