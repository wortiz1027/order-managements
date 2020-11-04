package co.edu.javeriana.orders.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderRepository {
    CompletableFuture<String> saveOrder(final Order order);
    CompletableFuture<String> saveProductsByOrder(final String orderId, final List<Product> products);

    CompletableFuture<String> updateStatusOrderById(final String orderId, final String status);

    CompletableFuture<String> deleteAllProductsAssociateToOrder(String orderId);
    CompletableFuture<String> deleteOrderById(String orderId);
}
