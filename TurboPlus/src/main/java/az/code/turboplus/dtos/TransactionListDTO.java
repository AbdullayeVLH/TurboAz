package az.code.turboplus.dtos;

import az.code.turboplus.models.Transaction;
import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionListDTO {
    private Long id;
    private Long listingId;
    @DecimalMin("0.0")
    @DecimalMax("1000000.0")
    private Double amount;
    @FutureOrPresent
    private LocalDateTime createdAt;

    public TransactionListDTO(Transaction data) {
        this.id = data.getId();
        this.listingId = data.getListingId();
        this.amount = data.getAmount();
        this.createdAt = data.getTime();
    }
}
