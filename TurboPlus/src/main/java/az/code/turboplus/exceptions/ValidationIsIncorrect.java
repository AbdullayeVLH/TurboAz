package az.code.turboplus.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationIsIncorrect extends RuntimeException{
    public ValidationIsIncorrect() {
        super("This user does not exists.");
    }
}
