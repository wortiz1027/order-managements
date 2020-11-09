package co.edu.javeriana.orders.domain;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface OrderRepository {
    Optional<Order> findOrderById(String orderId);

    Optional<List<Order>> getAllOpenOrder();
    Optional<Order> getOrderDetail(final String orderId);
    Optional<State> getOrderState(final String orderId);
    Optional<Order> getOrderByCode(final String orderCode);
    Optional<List<Order>> getOrderByProductCode(final String productId);

    CompletableFuture<String> saveOrder(final Order order);
    CompletableFuture<String> saveProductsByOrder(final String orderId, final List<Product> products);

    CompletableFuture<String> updateStatusOrderById(final String orderId, final String status);

    CompletableFuture<String> deleteAllProductsAssociateToOrder(String orderId);
    CompletableFuture<String> deleteOrderById(String orderId);
}
