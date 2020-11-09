package co.edu.javeriana.orders.application.dto;

import co.edu.javeriana.orders.domain.Order;
import lombok.Data;

import java.util.List;

@Data
public final class Response {
    private Status status;
    private Order order;
    private List<Order> orders;
}

