package co.edu.javeriana.orders.application;

import co.edu.javeriana.orders.application.dto.Response;
import co.edu.javeriana.orders.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderQueryServiceImp implements OrderQueryService {
    private final OrderRepository repository;

    @Override
    public CompletableFuture<Response> getAllOpenOrder(Pageable paging) {
        Response response = new Response();
        co.edu.javeriana.orders.application.dto.Status status = new co.edu.javeriana.orders.application.dto.Status();

        try {
            Optional<Page<Order>> orders = this.repository.getAllOpenOrder(paging);

            if (orders.isEmpty()) {
                status.setCode(Status.EMPTY.name());
                status.setDescription(String.format("There is not information for orders with active status"));
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            List<Order> orderTemp = orders.get().getContent();//Operaciones de carga de producto
            orderTemp = includeProductToOrders(orderTemp);
            //Se obtiene el valor total a pagar basado en sus productos
            executeTotalToPay(orderTemp);

            Map<String, Object> data = new HashMap<>();
            data.put("orders", orderTemp);
            data.put("currentPage", orders.get().getNumber());
            data.put("totalItems", orders.get().getTotalElements());
            data.put("totalPages", orders.get().getTotalPages());

            status.setCode(Status.SUCCESS.name());
            status.setDescription(String.format("There is information for active orders"));
            response.setStatus(status);
            response.setData(data);

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
    public CompletableFuture<Response> getOrderByProductCode(Pageable paging, String productCode) {
        Response response = new Response();
        co.edu.javeriana.orders.application.dto.Status status = new co.edu.javeriana.orders.application.dto.Status();

        try {
            Optional<Page<Order>> orders = this.repository.getOrderByProductCode(paging, productCode);

            if (!orders.isPresent()) {
                status.setCode(Status.EMPTY.name());
                status.setDescription(String.format("There is not information for orders with product code: %s associated", productCode));
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            List<Order> orderTemp = orders.get().getContent();
            //Operaciones de carga de producto
            orderTemp = includeProductToOrders(orderTemp);
            //Se obtiene el valor total a pagar basado en sus productos
            executeTotalToPay(orderTemp);

            Map<String, Object> data = new HashMap<>();
            data.put("orders", orderTemp);
            data.put("currentPage", orders.get().getNumber());
            data.put("totalItems", orders.get().getTotalElements());
            data.put("totalPages", orders.get().getTotalPages());

            status.setCode(Status.SUCCESS.name());
            status.setDescription(String.format("There is informations for order wit product code: %s associated", productCode));
            response.setStatus(status);
            response.setData(data);

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            status.setCode(Status.ERROR.name());
            status.setDescription(String.format("Error getting rows: %s", e.getMessage()));
            response.setStatus(status);

            return CompletableFuture.completedFuture(response);
        }
    }

    @Override
    public CompletableFuture<Response> getOrderByClient(Pageable paging, String clientId) {
        Response response = new Response();
        co.edu.javeriana.orders.application.dto.Status status = new co.edu.javeriana.orders.application.dto.Status();

        try {
            Optional<Page<Order>> orders = this.repository.getOrderByClient(paging, clientId);

            if (orders.isEmpty()) {
                status.setCode(Status.EMPTY.name());
                status.setDescription(String.format("There is not information for orders with client id:  %s associated", clientId));
                response.setStatus(status);
                return CompletableFuture.completedFuture(response);
            }

            List<Order> orderTemp = orders.get().getContent();
            //Operaciones de carga de producto
            orderTemp = includeProductToOrders(orderTemp);
            //Se obtiene el valor total a pagar basado en sus productos
            executeTotalToPay(orderTemp);

            Map<String, Object> data = new HashMap<>();
            data.put("orders", orderTemp);
            data.put("currentPage", orders.get().getNumber());
            data.put("totalItems", orders.get().getTotalElements());
            data.put("totalPages", orders.get().getTotalPages());

            status.setCode(Status.SUCCESS.name());
            status.setDescription(String.format("There is informations for order wit client id: %s associated", clientId));
            response.setStatus(status);
            response.setData(data);

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            status.setCode(Status.ERROR.name());
            status.setDescription(String.format("Error getting rows: %s", e.getMessage()));
            response.setStatus(status);

            return CompletableFuture.completedFuture(response);
        }
    }

    private List<Order> includeProductToOrders(final List<Order> orders) {
        orders.forEach((final Order order) -> {
                                                final List<Product> products = (List<Product>) this.repository.findProductsByOrderId(order.getId()).get();
                                                order.setProducts(products);
                                              });
        return orders;

    }

    private void executeTotalToPay(final List<Order> orders) {
        orders.forEach((final Order order) -> { order.totalToPay(); });
    }
}