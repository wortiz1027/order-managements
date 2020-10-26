package co.edu.javeriana.orders.infrastructure.controllers.orders;

import co.edu.javeriana.orders.application.orders.OrderCreatorCommandService;
import co.edu.javeriana.orders.domain.Order;
import co.edu.javeriana.orders.domain.Response;
import co.edu.javeriana.orders.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public final class CreateOrderCommandController {
    private OrderCreatorCommandService service;

    @PostMapping("/order")
    public ResponseEntity<CompletableFuture<Response>> handle(@RequestBody Order order) throws ExecutionException, InterruptedException {
        if (order == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CompletableFuture<Response> rs = this.service.createOrder(order);

        if (rs.get().getStatus().equalsIgnoreCase(Status.CREATED.name()))
            return new ResponseEntity<>(rs, HttpStatus.CREATED);

        if (rs.get().getStatus().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.CONFLICT);
    }
}
