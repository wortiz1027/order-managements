package co.edu.javeriana.orders.application;

import co.edu.javeriana.orders.domain.Response;
import co.edu.javeriana.orders.domain.State;

import java.util.concurrent.CompletableFuture;

public interface OrderUpdateCommandService {
    CompletableFuture<Response> cancelOrder(String orderId);
    CompletableFuture<Response> changeStateOrder(String orderId, State state);
}
