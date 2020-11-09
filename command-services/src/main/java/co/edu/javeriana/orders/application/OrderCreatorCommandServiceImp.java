package co.edu.javeriana.orders.application;

import co.edu.javeriana.orders.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderCreatorCommandServiceImp implements  OrderCreatorCommandService {
    @Value("${events.amqp.exchange}")
    String orderExchange;

    @Value("${events.amqp.routing-key}")
    String orderRoutingKey;

    private final OrderRepository repository;
    private final AmqpTemplate template;

    @Override
    public CompletableFuture<Response> createOrder(Order order) {
        Response response = new Response();

        try {
            //Save values for orders
            order.setState(new State(OrderState.ABIERTA.name()));
            String status = this.repository.saveOrder(order).get();
            response.setStatus(status);

            if (!status.equalsIgnoreCase(Status.CREATED.name())) {
                response.setDescription(String.format("The order with id: {%s} has an error", order.getId()));
                return CompletableFuture.completedFuture(response);
            }

            //Save values for products by orders
            status = this.repository.saveProductsByOrder(order.getId(), order.getProducts()).get();
            response.setStatus(status);

            if (!status.equalsIgnoreCase(Status.CREATED.name())) {
                response.setDescription(String.format("The products by orders with order id: {%s} has an error", order.getId()));
                return CompletableFuture.completedFuture(response);
            }

            order.setStatus(Status.CREATED.name());
            this.template.convertAndSend(orderExchange, orderRoutingKey, order);
            response.setDescription(String.format("The order with id: {%s} has been created", order.getId()));

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            response.setStatus(Status.ERROR.name());
            response.setDescription(String.format("Exception creating row {%s} has been release: {%s}", order.getId(), e.getMessage()));
            return CompletableFuture.completedFuture(response);
        }
    }
}