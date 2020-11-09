package co.edu.javeriana.orders.infrastructure.controllers;

import co.edu.javeriana.orders.application.OrderDeleteService;
import co.edu.javeriana.orders.domain.Response;
import co.edu.javeriana.orders.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public final class DeleteOrderCommandController {
    private final OrderDeleteService service;

    @DeleteMapping("/order/remove/{orderId}")
    public ResponseEntity<CompletableFuture<Response>> remove(@PathVariable String orderId) throws ExecutionException, InterruptedException {
        if (orderId == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CompletableFuture<Response> rs = service.deleteOrder(orderId);

        if (rs.get().getStatus().equalsIgnoreCase(Status.DELETED.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.CONFLICT);
    }
}
