package co.edu.javeriana.orders.infrastructure.respository.orders;

import co.edu.javeriana.orders.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
@RequiredArgsConstructor
public final class OrdersMySqlRepository implements OrderRepository {
    private JdbcTemplate template;

    @Override
    public CompletableFuture<String> saveOrder(Order order) {
        try {
            String sql = "INSERT INTO ORDER (ORDER_ID, " +
                                            "CUSTOMER_ID, " +
                                            "PAYMENT_ID, " +
                                            "STATE" +
                                            "VALUES (?,?,?,?)";

            this.template.update(sql,
                    order.getId(),
                    order.getCustomer().getId(),
                    order.getPayment().getId(),
                    order.getState().getId());

            return CompletableFuture.completedFuture(Status.CREATED.name());
        } catch(Exception e) {
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }
    }

    @Override
    public CompletableFuture<String> saveProductsByOrder(final String orderId, final Products products) {
        try {
            for (Product productRow : products.getProducts()) {
                String sql = "INSERT INTO PRODUCTBYORDER (ORDER_ID, " +
                                                         "PRODUCT_ID" +
                                                         "PRICE_PRODUCT" +
                                                         "VALUES (?,?,?)";

                this.template.update(sql,
                                     orderId,
                                     productRow.getId(),
                                     productRow.getPrice());
            }

            return CompletableFuture.completedFuture(Status.CREATED.name());
        } catch(Exception e) {
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }

        return null;
    }
}
