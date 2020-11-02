package co.edu.javeriana.orders.infrastructure.controllers.orders;

import co.edu.javeriana.orders.application.orders.OrderUpdateCommandService;
import co.edu.javeriana.orders.domain.Response;
import co.edu.javeriana.orders.domain.State;
import co.edu.javeriana.orders.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UpdateOrderCommandController {
    private final OrderUpdateCommandService service;

    @PutMapping("/order/{orderId}")
    public ResponseEntity<CompletableFuture<Response>> cancel(@PathVariable String orderId) throws ExecutionException, InterruptedException {
        if (orderId == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CompletableFuture<Response> rs = service.cancelOrder(orderId);

        if (rs.get().getStatus().equalsIgnoreCase(Status.UPDATED.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.CONFLICT);
    }

    @PutMapping("/order/state/{orderId}")
    public ResponseEntity<CompletableFuture<Response>> changeState(@PathVariable String orderId, @RequestBody State state) throws ExecutionException, InterruptedException {
        if (orderId == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CompletableFuture<Response> rs = service.changeStateOrder(orderId, state);

        if (rs.get().getStatus().equalsIgnoreCase(Status.UPDATED.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.CONFLICT);
    }
}
