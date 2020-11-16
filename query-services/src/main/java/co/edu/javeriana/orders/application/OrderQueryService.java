package co.edu.javeriana.orders.application;

import co.edu.javeriana.orders.application.dto.Response;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.CompletableFuture;

public interface OrderQueryService {
    CompletableFuture<Response> getAllOpenOrder(final Pageable paging);
    CompletableFuture<Response> getOrderDetail(final String orderId);
    CompletableFuture<Response> getOrderState(final String orderId);
    CompletableFuture<Response> getOrderByCode(final String orderCode);
    CompletableFuture<Response> getOrderByProductCode(final Pageable paging, final String productId);
    CompletableFuture<Response> getOrderByClient(final Pageable paging, final String clientId);
}
