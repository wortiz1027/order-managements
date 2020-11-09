package co.edu.javeriana.orders.application;

import co.edu.javeriana.orders.domain.Order;
import co.edu.javeriana.orders.domain.Response;

import java.util.concurrent.CompletableFuture;

public interface OrderCreatorCommandService {
    CompletableFuture<Response> createOrder(Order order);
}
