package co.edu.javeriana.orders.application;

import co.edu.javeriana.orders.domain.Response;

import java.util.concurrent.CompletableFuture;

public interface OrderDeleteService {
    CompletableFuture<Response> deleteOrder(String orderId);
}
