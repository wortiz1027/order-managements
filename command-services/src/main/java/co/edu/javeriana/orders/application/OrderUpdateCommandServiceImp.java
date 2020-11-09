package co.edu.javeriana.orders.application;

import co.edu.javeriana.orders.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderUpdateCommandServiceImp implements OrderUpdateCommandService {
    @Value("${events.amqp.exchange}")
    String orderExchange;

    @Value("${events.amqp.routing-key}")
    String orderRoutingKey;

    private final OrderRepository repository;
    private final AmqpTemplate template;

    @Override
    public CompletableFuture<Response> cancelOrder(String orderId) {
        Response response = new Response();

        try {
            //Cancel of orders
            String status = this.repository.updateStatusOrderById(orderId, OrderState.CANCELADA.name()).get();
            response.setStatus(status);

            if (!status.equalsIgnoreCase(Status.UPDATED.name())) {
                response.setDescription(String.format("The order with id: {%s} has an error", orderId));
                return CompletableFuture.completedFuture(response);
            }

            final Order order = new Order();
            order.setId(orderId);
            order.setState(new State(OrderState.CANCELADA.name()));
            order.setStatus(Status.UPDATED.name());

            this.template.convertAndSend(orderExchange, orderRoutingKey, order);
            response.setDescription(String.format("The state of the order with id: {%s} has been updated", orderId));

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            response.setStatus(Status.ERROR.name());
            response.setDescription(String.format("Exception updating row {%s} has been release: {%s}", orderId, e.getMessage()));
            return CompletableFuture.completedFuture(response);
        }
    }

    @Override
    public CompletableFuture<Response> changeStateOrder(String orderId, State state) {
        Response response = new Response();

        try {
            //Change status of orders
            String status = this.repository.updateStatusOrderById(orderId, state.getValue()).get();
            response.setStatus(state.getValue());

            if (!status.equalsIgnoreCase(Status.UPDATED.name())) {
                response.setDescription(String.format("The order with id: {%s} has an error", orderId));
                return CompletableFuture.completedFuture(response);
            }

            final Order order = new Order();
            order.setId(orderId);
            order.setState(state);
            order.setStatus(Status.UPDATED.name());

            this.template.convertAndSend(orderExchange, orderRoutingKey, order);
            response.setDescription(String.format("The state of the order with id: {%s} has been updated", orderId));

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            response.setStatus(Status.ERROR.name());
            response.setDescription(String.format("Exception updating row {%s} has been release: {%s}", orderId, e.getMessage()));
            return CompletableFuture.completedFuture(response);
        }
    }
}
