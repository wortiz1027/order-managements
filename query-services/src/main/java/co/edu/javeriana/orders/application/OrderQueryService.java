package co.edu.javeriana.orders.application;

import co.edu.javeriana.orders.application.dto.Response;
import co.edu.javeriana.orders.domain.Order;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface OrderQueryService {
    CompletableFuture<Response> getAllOpenOrder();
    CompletableFuture<Response> getOrderDetail(final String orderId);
    CompletableFuture<Response> getOrderState(final String orderId);
    CompletableFuture<Response> getOrderByCode(final String orderCode);
    CompletableFuture<Response> getOrderByProductCode(final String productId);
    CompletableFuture<Response> getOrderByClient(final String clientId);
}
