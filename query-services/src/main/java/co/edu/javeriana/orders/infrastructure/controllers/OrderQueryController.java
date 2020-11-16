package co.edu.javeriana.orders.infrastructure.controllers;

import co.edu.javeriana.orders.application.OrderQueryService;
import co.edu.javeriana.orders.application.dto.Response;
import co.edu.javeriana.orders.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public final class OrderQueryController {
    private final OrderQueryService service;

    @GetMapping("/orders/open")
    public ResponseEntity<CompletableFuture<Response>> allOpen(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "5") int size) throws ExecutionException, InterruptedException {
        Pageable paging = PageRequest.of(page, size);

        CompletableFuture<Response> rs = service.getAllOpenOrder(paging);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.SUCCESS.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.EMPTY.name()))
            return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/order/detail/{orderId}")
    public ResponseEntity<CompletableFuture<Response>> detail(@PathVariable String orderId) throws ExecutionException, InterruptedException {
        CompletableFuture<Response> rs = service.getOrderDetail(orderId);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.SUCCESS.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.EMPTY.name()))
            return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/order/state/{orderId}")
    public ResponseEntity<CompletableFuture<Response>> status(@PathVariable String orderId) throws ExecutionException, InterruptedException {
        CompletableFuture<Response> rs = service.getOrderState(orderId);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.SUCCESS.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.EMPTY.name()))
            return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/order/{orderCode}")
    public ResponseEntity<CompletableFuture<Response>> get(@PathVariable String orderCode) throws ExecutionException, InterruptedException {
        CompletableFuture<Response> rs = service.getOrderByCode(orderCode);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.SUCCESS.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.EMPTY.name()))
            return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/orders/product/{productCode}")
    public ResponseEntity<CompletableFuture<Response>> getByProduct(@PathVariable String productCode,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "5") int size) throws ExecutionException, InterruptedException {
        Pageable paging = PageRequest.of(page, size);

        CompletableFuture<Response> rs = service.getOrderByProductCode(paging, productCode);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.SUCCESS.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.EMPTY.name()))
            return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/orders/client/{clientId}")
    public ResponseEntity<CompletableFuture<Response>> getByClient(@PathVariable String clientId,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size) throws ExecutionException, InterruptedException {
        Pageable paging = PageRequest.of(page, size);

        CompletableFuture<Response> rs = service.getOrderByClient(paging, clientId);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.SUCCESS.name()))
            return new ResponseEntity<>(rs, HttpStatus.OK);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.EMPTY.name()))
            return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);

        if (rs.get().getStatus().getCode().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
    }
}
