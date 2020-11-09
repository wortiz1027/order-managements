package co.edu.javeriana.orders.application;

import co.edu.javeriana.orders.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderDeleteServiceImpl implements OrderDeleteService {
    @Value("${events.amqp.exchange}")
    String orderExchange;

    @Value("${events.amqp.routing-key}")
    String orderRoutingKey;

    private final OrderRepository repository;
    private final AmqpTemplate template;

    @Override
    public CompletableFuture<Response> deleteOrder(String orderId) {
        Response response = new Response();

        try {
            //1. Delete all associated products to order
            String status = this.repository.deleteAllProductsAssociateToOrder(orderId).get();
            response.setStatus(Status.DELETED.name());

            if (!status.equalsIgnoreCase(Status.DELETED.name())) {
                response.setDescription(String.format("The associated products to order with id: {%s} has an error", orderId));
                return CompletableFuture.completedFuture(response);
            }

            //2. Delete order
            status = this.repository.deleteOrderById(orderId).get();
            response.setStatus(Status.DELETED.name());

            if (!status.equalsIgnoreCase(Status.DELETED.name())) {
                response.setDescription(String.format("The order with id: {%s} has an error", orderId));
                return CompletableFuture.completedFuture(response);
            }

            final Order order = new Order();
            order.setId(orderId);
            order.setState(new State(OrderState.ELIMINADO.name()));
            order.setStatus(Status.DELETED.name());

            this.template.convertAndSend(orderExchange, orderRoutingKey, order);
            response.setDescription(String.format("The order with id: {%s} has been deleted", orderId));

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            response.setStatus(Status.ERROR.name());
            response.setDescription(String.format("Exception deleting row {%s} has been release: {%s}", orderId, e.getMessage()));
            return CompletableFuture.completedFuture(response);
        }
    }
}
