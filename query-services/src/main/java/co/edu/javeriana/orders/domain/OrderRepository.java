package co.edu.javeriana.orders.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface OrderRepository<T> {
    Optional<T> findOrderById(String orderId);

    Optional<Page<T>> getAllOpenOrder(Pageable paging);
    Optional<T> getOrderDetail(final String orderId);
    Optional<State> getOrderState(final String orderId);
    Optional<T> getOrderByCode(final String orderCode);
    Optional<Page<T>> getOrderByProductCode(final Pageable paging, final String productCode);
    Optional<Page<T>> getOrderByClient(final Pageable paging, final String clientId);
    Optional<List<Product>> findProductsByOrderId(String orderId);

    CompletableFuture<String> saveOrder(final Order order);
    CompletableFuture<String> saveProductsByOrder(final String orderId, final List<Product> products);

    CompletableFuture<String> updateStatusOrderById(final String orderId, final String status);

    CompletableFuture<String> deleteAllProductsAssociateToOrder(String orderId);
    CompletableFuture<String> deleteOrderById(String orderId);
}
