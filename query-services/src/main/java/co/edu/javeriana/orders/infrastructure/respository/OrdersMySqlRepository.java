package co.edu.javeriana.orders.infrastructure.respository;

import co.edu.javeriana.orders.domain.*;
import co.edu.javeriana.orders.infrastructure.respository.mappers.OrderRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
@RequiredArgsConstructor
public class OrdersMySqlRepository implements OrderRepository<Order> {
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
                                    null,
                                    0.0
                            ))
            );
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Page<Order>> getAllOpenOrder(Pageable paging) {
        String sql = "SELECT * FROM ORDERS WHERE STATUS =? LIMIT %d OFFSET %d";

        List<Order> orders = this.template.query(String.format(sql, paging.getPageSize(), paging.getOffset()), new Object[] { OrderState.ABIERTA.name() }, new OrderRowMapper());
        return Optional.of(new PageImpl<>(orders, paging, countOrders()));
    }

    @Override
    public Optional<Order> getOrderDetail(String orderId) {
        final Optional<Order> orderDetail = findOrderById(orderId);

        if (orderDetail.isPresent()) {
            final List<Product> products = findProductsByOrderId(orderId).get();
            orderDetail.get().setProducts(products);
            return orderDetail;
        }

        return Optional.empty();
    }

    @Override
    public Optional<State> getOrderState(String orderId) {
        try {
            String sql = "SELECT STATUS FROM ORDERS WHERE ORDER_ID =?";
            return template.queryForObject(sql,
                    new Object[]{orderId},
                    (rs, rowNum) ->
                            Optional.of(new State(rs.getString("STATUS")
                            ))
            );
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Order> getOrderByCode(String orderCode) {
        try {
            String sql = "SELECT * FROM ORDERS WHERE ORDER_CODE =?";
            return template.queryForObject(sql,
                    new Object[]{orderCode},
                    (rs, rowNum) ->
                            Optional.of(new Order(
                                    rs.getString("ORDER_ID"),
                                    rs.getString("ORDER_CODE"),
                                    rs.getDate("CREATION_DATE").toLocalDate(),
                                    new Customer(rs.getString("CUSTOMER_ID")),
                                    new ArrayList<>(),
                                    new Payment(rs.getString("PAYMENT_ID")),
                                    new State(rs.getString("STATUS")),
                                    null,
                                    0.0))
            );
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Page<Order>> getOrderByProductCode(Pageable paging, String productCode) {
        String sql = "SELECT ORDS.* " +
                     "FROM ORDERS ORDS " +
                     "INNER JOIN PRODUCTBYORDERS PROBYORD ON PROBYORD.ORDER_ID = ORDS.ORDER_ID " +
                     "WHERE PROBYORD.PRODUCT_CODE =? " +
                     "LIMIT %d OFFSET %d";

        List<Order> orders = this.template.query(String.format(sql, paging.getPageSize(), paging.getOffset()), new Object[] { productCode }, new OrderRowMapper());
        return Optional.of(new PageImpl<>(orders, paging, countOrdersByProduct(productCode)));
    }

    @Override
    public Optional<Page<Order>> getOrderByClient(Pageable paging, String clientId) {
        String sql = "SELECT * FROM ORDERS WHERE CUSTOMER_ID =? LIMIT %d OFFSET %d";

        List<Order> orders = this.template.query(String.format(sql, paging.getPageSize(), paging.getOffset()), new Object[] { clientId }, new OrderRowMapper());
        return Optional.of(new PageImpl<>(orders, paging, countOrdersByClient(clientId)));
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
                                                          "PRICE_PRODUCT," +
                                                          "QUANTITY) " +
                                                          "VALUES (?,?,?,?,?)";

                this.template.update(sql,
                                     orderId,
                                     productRow.getId(),
                                     productRow.getCode(),
                                     productRow.getPrice(),
                                     productRow.getQuantity());
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
    public Optional<List<Product>> findProductsByOrderId(String orderId) {
        try {
            String sql = "SELECT * FROM PRODUCTBYORDERS WHERE ORDER_ID =?";

            return Optional.of(template.query(sql, (rs, rowNum) -> new Product(rs.getString("PRODUCT_ID"),
                    rs.getString("PRODUCT_CODE"),
                    rs.getDouble("PRICE_PRODUCT"),
                    rs.getInt("QUANTITY")), orderId));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Optional.of(new ArrayList<>());
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

    private int countOrders() {
        return this.template.queryForObject("SELECT count(*) FROM ORDERS", Integer.class);
    }

    private int countOrdersByProduct(final String productCode) {
        String sql = "SELECT count(*) " +
                     "FROM ORDERS ORDS " +
                     "INNER JOIN PRODUCTBYORDERS PROBYORD ON PROBYORD.ORDER_ID = ORDS.ORDER_ID " +
                     "WHERE PROBYORD.PRODUCT_CODE =? ";

        return this.template.queryForObject(sql, new Object[] { productCode }, Integer.class);
    }

    private int countOrdersByClient(final String clientId) {
        String sql = "SELECT count(*) FROM ORDERS WHERE CUSTOMER_ID =?";
        return this.template.queryForObject(sql, new Object[] { clientId }, Integer.class);
    }
}
