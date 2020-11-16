package co.edu.javeriana.orders.infrastructure.respository.mappers;

import co.edu.javeriana.orders.domain.Customer;
import co.edu.javeriana.orders.domain.Order;
import co.edu.javeriana.orders.domain.Payment;
import co.edu.javeriana.orders.domain.State;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int i) throws SQLException {
        Order order = new Order(rs.getString("ORDER_ID"),
                                rs.getString("ORDER_CODE"),
                                rs.getDate("CREATION_DATE").toLocalDate(),
                                new Customer(rs.getString("CUSTOMER_ID")),
                                new ArrayList<>(),
                                new Payment(rs.getString("PAYMENT_ID")),
                                new State(rs.getString("STATUS")),
                                null,
                                0.0);
        return order;
    }
}
