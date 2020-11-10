package co.edu.javeriana.orders.infrastructure.respository;

import co.edu.javeriana.orders.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
@RequiredArgsConstructor
public class OrdersMySqlRepository implements OrderRepository {
    private final JdbcTemplate template;

    @Override
    public Optional<Order> findOrderById(String orderId) {
        try {
            String sql = "SELECT * FROM ORDERS WHERE ORDER_ID =?";
            return template.queryForObject(sql,
                    new Object[]{orderId},
                    (rs, rowNum) ->
                            Optional.of(new Order(
                                    rs.getString("ORDER_ID"),
                                    rs.getString("ORDER_CODE"),
                                    rs.getDate("CREATION_DATE").toLocalDate(),
                                    new Customer(rs.getString("CUSTOMER_ID")),
                                    new ArrayList<>(),
                                    new Payment(rs.getString("PAYMENT_ID")),
                                    new State(rs.getString("STATUS")),
                                    ""
                            ))
            );
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Order>> getAllOpenOrder() {
        try {
            String sql = "SELECT * FROM ORDERS WHERE STATUS =?";

            return Optional.of(template.query(sql, (rs, rowNum) -> new Order(rs.getString("ORDER_ID"),
                                                                             rs.getString("ORDER_CODE"),
                                                                             rs.getDate("CREATION_DATE").toLocalDate(),
                                                                             new Customer(rs.getString("CUSTOMER_ID")),
                                                                             new ArrayList<>(),
                                                                             new Payment(rs.getString("PAYMENT_ID")),
                                                                             new State(rs.getString("STATUS")),
                                                                      ""), OrderState.ABIERTA.name()));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Optional.of(new ArrayList<>());
        }
    }

    @Override
    public Optional<Order> getOrderDetail(String orderId) {
        return Optional.empty();
    }

    @Override
    public Optional<State> getOrderState(String orderId) {
        return Optional.empty();
    }

    @Override
    public Optional<Order> getOrderByCode(String orderCode) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Order>> getOrderByProductCode(String productId) {
        return Optional.empty();
    }

    @Override
    public CompletableFuture<String> saveOrder(Order order) {
        try {
            String sql = "INSERT INTO ORDERS (ORDER_ID, " +
                                            "ORDER_CODE, " +
                                            "CUSTOMER_ID, " +
                                            "PAYMENT_ID, " +
                                            "STATUS, " +
                                            "CREATION_DATE) " +
                                            "VALUES (?,?,?,?,?,?)";

            this.template.update(sql,
                                 order.getId(),
                                 order.getCode(),
                                 order.getCustomer().getId(),
                                 order.getPayment().getId(),
                                 order.getState().getValue(),
                                 order.getCreationDate());

            return CompletableFuture.completedFuture(Status.CREATED.name());
        } catch(Exception e) {
            e.printStackTrace();e.printStackTrace();
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }
    }

    @Override
    public CompletableFuture<String> saveProductsByOrder(final String orderId, final List<Product> products) {
        try {
            for (Product productRow : products) {
                String sql = "INSERT INTO PRODUCTBYORDERS (ORDER_ID, " +
                                                         "PRODUCT_ID, " +
                                                         "PRODUCT_CODE, " +
                                                         "PRICE_PRODUCT) " +
                                                         "VALUES (?,?,?,?)";

                this.template.update(sql,
                                     orderId,
                                     productRow.getId(),
                                     productRow.getCode(),
                                     productRow.getPrice());
            }

            return CompletableFuture.completedFuture(Status.CREATED.name());
        } catch(Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }
    }

    @Override
    public CompletableFuture<String> updateStatusOrderById(final String orderId, final String status) {
        try {
            if (findOrderById(orderId).isEmpty()) return CompletableFuture.completedFuture(Status.NO_EXIST.name());

            String sql = "UPDATE ORDERS SET STATUS = ? WHERE ORDER_ID = ?";

            this.template.update(sql, status, orderId);

            return CompletableFuture.completedFuture(Status.UPDATED.name());
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }
    }

    @Override
    public CompletableFuture<String> deleteAllProductsAssociateToOrder(String orderId) {
        try {
            if (findProductsByOrderId(orderId).isEmpty()) return CompletableFuture.completedFuture(Status.NO_EXIST.name());

            String sql = "DELETE FROM PRODUCTBYORDERS WHERE ORDER_ID = ?";

            this.template.update(sql, orderId);

            return CompletableFuture.completedFuture(Status.DELETED.name());
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }
    }

    @Override
    public CompletableFuture<String> deleteOrderById(String orderId) {
        try {
            if (findOrderById(orderId).isEmpty()) return CompletableFuture.completedFuture(Status.NO_EXIST.name());

            String sql = "DELETE FROM ORDERS WHERE ORDER_ID = ?";

            this.template.update(sql, orderId);

            return CompletableFuture.completedFuture(Status.DELETED.name());
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(Status.ERROR.name());
        }
    }

    private List<Optional<Product>> findProductsByOrderId(String orderId) {
        try {
            String sql = "SELECT * FROM PRODUCTBYORDERS WHERE ORDER_ID =?";

            return template.query(sql, (rs, rowNum) ->
                    Optional.of(new Product(rs.getString("PRODUCT_ID"),
                                            rs.getString("PRODUCT_CODE"),
                                            rs.getDouble("PRICE_PRODUCT"))), orderId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
