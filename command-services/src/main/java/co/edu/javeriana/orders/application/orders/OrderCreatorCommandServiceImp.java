package co.edu.javeriana.orders.application.orders;

import co.edu.javeriana.orders.domain.Order;
import co.edu.javeriana.orders.domain.OrderRepository;
import co.edu.javeriana.orders.domain.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public final class OrderCreatorCommandServiceImp implements  OrderCreatorCommandService {
    private OrderRepository repository;

    @Override
    public CompletableFuture<Response> createOrder(Order order) {
        Response response = new Response();
        try {
            String status = this.repository.saveOrder(order).get();
            response.setStatus(status);

            if (!status.equalsIgnoreCase(Status.CREATED.name())) {
                response.setDescription(String.format("The order with id: {%s} has an error", order.getId()));
                return CompletableFuture.completedFuture(response);
            }

            response.setDescription(String.format("The order with id: {%s} has been created", order.getId()));
            order.setStatus(Status.CREATED.name());

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            response.setStatus(Status.ERROR.name());
            response.setDescription(String.format("Exception creating row {%s} has been release: {%s}", order.getId(), e.getMessage()));
            return CompletableFuture.completedFuture(response);
        }
    }
}
