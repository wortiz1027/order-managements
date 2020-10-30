package co.edu.javeriana.orders.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Order {
    private String id;
    private String code;
    private LocalDate creationDate;
    private Customer customer;
    private Products products;
    private Payment payment;
    private State state;
    private String status;
}