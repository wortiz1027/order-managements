package co.edu.javeriana.orders.events;

import co.edu.javeriana.orders.domain.Order;
import co.edu.javeriana.orders.domain.OrderRepository;
import co.edu.javeriana.orders.domain.Status;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EventHandler.class);

    private final OrderRepository repository;

    @RabbitListener(queues = "${events.amqp.queue}")
    public void consumer(final Order orderData) {
        LOG.info("To receive: {}", orderData);
        final Optional<Order> order = repository.findOrderById(orderData.getId());

        if (orderData.getStatus().equalsIgnoreCase(Status.CREATED.name()) && order.isEmpty()) {
            //Save order data
            this.repository.saveOrder(orderData);
            //Save products list of order
            this.repository.saveProductsByOrder(orderData.getId(), orderData.getProducts());
            //Log message
            LOG.info("Order with id [{}] has been created", orderData.getId());
            //Exit from method
            return;
        }

        if (orderData.getStatus().equalsIgnoreCase(Status.UPDATED.name()) && !order.isEmpty()) {
            //Updating state of order, this state could be CANCELADO or other
            this.repository.updateStatusOrderById(orderData.getId(), orderData.getState().getValue());
            //Log message
            LOG.info("Order with id [{}] has been updated", orderData.getId());
            //Exit from method
            return;
        }

        if (orderData.getStatus().equalsIgnoreCase(Status.DELETED.name()) && !order.isEmpty()) {
            // First, deleted all associated products
            this.repository.deleteAllProductsAssociateToOrder(orderData.getId());
            // Next, deleted the order
            this.repository.deleteOrderById(orderData.getId());
            //Log message
            LOG.info("Order with id [{}] has been deleted", orderData.getId());
            //Exit from method
            return;
        }

        //Log message
        LOG.info("Order has not been processed");
    }
}
