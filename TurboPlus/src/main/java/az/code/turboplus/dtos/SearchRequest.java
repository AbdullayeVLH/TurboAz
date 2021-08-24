package az.code.turboplus.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchRequest {
    String make;
    String model;
    String location;
    Integer minYear;
    Integer maxYear;
    Integer minPrice;
    Integer maxPrice;
    Integer minMileage;
    Integer maxMileage;
    String fuelType;
    String bodyType;
    Boolean loanOption;
    Boolean barterOption;
    Boolean leaseOption;
    Boolean cashOption;
}
