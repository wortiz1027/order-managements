package co.edu.javeriana.orders.application;

import co.edu.javeriana.orders.application.dto.Response;
import co.edu.javeriana.orders.domain.Order;
import co.edu.javeriana.orders.domain.OrderRepository;
import co.edu.javeriana.orders.domain.State;
import co.edu.javeriana.orders.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderQueryServiceImp implements OrderQueryService {
    private final OrderRepository repository;

    @Override
    public CompletableFuture<Response> getAllOpenOrder() {
        Response response = new Response();
        co.edu.javeriana.orders.application.dto.Status status = new co.edu.javeriana.orders.application.dto.Status();

        try {
            Optional<List<Order>> orders = this.repository.getAllOpenOrder();

            if (orders.isEmpty()) {
                status.setCode(Status.EMPTY.name());
                status.setDescription(String.format("There is not information for orders with active status"));
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            status.setCode(Status.SUCCESS.name());
            status.setDescription(String.format("There is information for active orders"));
            response.setStatus(status);
            response.setOrders(orders.get());

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            status.setCode(Status.ERROR.name());
            status.setDescription(String.format("Error getting rows: %s", e.getMessage()));
            response.setStatus(status);

            return CompletableFuture.completedFuture(response);
        }
    }

    @Override
    public CompletableFuture<Response> getOrderDetail(String orderId) {
        Response response = new Response();
        co.edu.javeriana.orders.application.dto.Status status = new co.edu.javeriana.orders.application.dto.Status();

        try {
            Optional<Order> order = this.repository.getOrderDetail(orderId);

            if (!order.isPresent()) {
                status.setCode(Status.EMPTY.name());
                status.setDescription(String.format("There is not information for order with id: %s", orderId));
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            status.setCode(Status.SUCCESS.name());
            status.setDescription(String.format("There is information for order with id: %s", orderId));
            response.setStatus(status);
            order.get().totalToPay();
            response.setOrder(order.get());

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            status.setCode(Status.ERROR.name());
            status.setDescription(String.format("Error getting rows: %s", e.getMessage()));
            response.setStatus(status);

            return CompletableFuture.completedFuture(response);
        }
    }

    @Override
    public CompletableFuture<Response> getOrderState(String orderId) {
        Response response = new Response();
        co.edu.javeriana.orders.application.dto.Status status = new co.edu.javeriana.orders.application.dto.Status();

        try {
            Optional<State> state = this.repository.getOrderState(orderId);

            if (!state.isPresent()) {
                status.setCode(Status.EMPTY.name());
                status.setDescription(String.format("There is not information of state order with id: %s", orderId));
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            status.setCode(Status.SUCCESS.name());
            status.setDescription(String.format("There is informations for state order with id: %s", orderId));
            response.setStatus(status);
            response.setState(state.get());

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            status.setCode(Status.ERROR.name());
            status.setDescription(String.format("Error getting rows: %s", e.getMessage()));
            response.setStatus(status);

            return CompletableFuture.completedFuture(response);
        }
    }

    @Override
    public CompletableFuture<Response> getOrderByCode(String orderCode) {
        Response response = new Response();
        co.edu.javeriana.orders.application.dto.Status status = new co.edu.javeriana.orders.application.dto.Status();

        try {
            Optional<Order> order = this.repository.getOrderByCode(orderCode);

            if (!order.isPresent()) {
                status.setCode(Status.EMPTY.name());
                status.setDescription(String.format("There is not information for order with code: %s", orderCode));
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            status.setCode(Status.SUCCESS.name());
            status.setDescription(String.format("There is informations for order with code: %s", orderCode));
            response.setStatus(status);
            response.setOrder(order.get());

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            status.setCode(Status.ERROR.name());
            status.setDescription(String.format("Error getting rows: %s", e.getMessage()));
            response.setStatus(status);

            return CompletableFuture.completedFuture(response);
        }
    }

    @Override
    public CompletableFuture<Response> getOrderByProductCode(String productCode) {
        Response response = new Response();
        co.edu.javeriana.orders.application.dto.Status status = new co.edu.javeriana.orders.application.dto.Status();

        try {
            Optional<List<Order>> orders = this.repository.getOrderByProductCode(productCode);

            if (!orders.isPresent()) {
                status.setCode(Status.EMPTY.name());
                status.setDescription(String.format("There is not information for orders with product code: %s associated", productCode));
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            status.setCode(Status.SUCCESS.name());
            status.setDescription(String.format("There is informations for order wit product code: %s associated", productCode));
            response.setStatus(status);
            response.setOrders(orders.get());

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            status.setCode(Status.ERROR.name());
            status.setDescription(String.format("Error getting rows: %s", e.getMessage()));
            response.setStatus(status);

            return CompletableFuture.completedFuture(response);
        }
    }

    @Override
    public CompletableFuture<Response> getOrderByClient(String clientId) {
        Response response = new Response();
        co.edu.javeriana.orders.application.dto.Status status = new co.edu.javeriana.orders.application.dto.Status();

        try {
            Optional<List<Order>> orders = this.repository.getOrderByClient(clientId);

            if (orders.isEmpty()) {
                status.setCode(Status.EMPTY.name());
                status.setDescription(String.format("There is not information for orders with client id:  %s associated", clientId));
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            status.setCode(Status.SUCCESS.name());
            status.setDescription(String.format("There is informations for order wit client id: %s associated", clientId));
            response.setStatus(status);
            response.setOrders(orders.get());

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            status.setCode(Status.ERROR.name());
            status.setDescription(String.format("Error getting rows: %s", e.getMessage()));
            response.setStatus(status);

            return CompletableFuture.completedFuture(response);
        }
    }
}