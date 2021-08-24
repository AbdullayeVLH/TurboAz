package az.code.turboplus.dtos;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Data
public class AmountDTO {
    @DecimalMin("0.0")
    @DecimalMax("1000000.0")
    Double amount;
}
