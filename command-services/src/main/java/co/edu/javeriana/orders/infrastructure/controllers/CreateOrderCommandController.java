package co.edu.javeriana.orders.infrastructure.controllers;

import co.edu.javeriana.orders.application.OrderCreatorCommandService;
import co.edu.javeriana.orders.domain.Order;
import co.edu.javeriana.orders.domain.Response;
import co.edu.javeriana.orders.domain.Status;
import io.swagger.annotations.*;
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
@Api(value="Command para la creación de una Orden de pedido")
public final class CreateOrderCommandController {
    private final OrderCreatorCommandService service;

    @PostMapping("/order")
    @ApiOperation(value = "Sirve para crear una nueva orden de comprar asociada a los servicios ofrecidos por TouresBalon", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "La solicitud ha tenido éxito y se ha creado una nueva orden")
    })
    public ResponseEntity<CompletableFuture<Response>> handle(@ApiParam("Información de la orden que debe ser creada.")
                                                              @RequestBody Order order) throws ExecutionException, InterruptedException {
        if (order == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CompletableFuture<Response> rs = service.createOrder(order);

        if (rs.get().getStatus().equalsIgnoreCase(Status.CREATED.name()))
            return new ResponseEntity<>(rs, HttpStatus.CREATED);

        if (rs.get().getStatus().equalsIgnoreCase(Status.ERROR.name()))
            return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(rs, HttpStatus.CONFLICT);
    }
}
