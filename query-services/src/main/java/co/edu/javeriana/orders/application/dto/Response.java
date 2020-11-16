package co.edu.javeriana.orders.application.dto;

import co.edu.javeriana.orders.domain.Order;
import co.edu.javeriana.orders.domain.State;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Response {
    private Status status;
    private Order order;
    private Map<String, Object> data;
    private State state;
}

