package co.edu.javeriana.orders.infrastructure.controllers;

import co.edu.javeriana.orders.application.OrderUpdateCommandService;
import co.edu.javeriana.orders.domain.Response;
import co.edu.javeriana.orders.domain.State;
import co.edu.javeriana.orders.domain.Status;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Api(value="Command para actualización de una Orden de pedido")
public class UpdateOrderCommandController {
    private final OrderUpdateCommandService service;

    @PutMapping("/order/{orderId}")
    @ApiOperation(value = "Sirve para actualizar el estado de la orden, este tomará el valor de CANCELADO", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La orden fue actualizada satisfactoriamente"),
            @ApiResponse(code = 404, message = "Error no se encontro informacion de la orden"),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    public ResponseEntity<CompletableFuture<Response>> cancel(@ApiParam("Identificador único de la orden. No puede ser vacio.")
                                                              @PathVariable String orderId) throws ExecutionException, InterruptedException {
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
    @ApiOperation(value = "Sirve para actualizar el estado de la orden, este tomará el valor de CANCELADO", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La estado de la orden fue actualizada satisfactoriamente"),
            @ApiResponse(code = 404, message = "Error no se encontro informacion de la orden"),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    public ResponseEntity<CompletableFuture<Response>> changeState(@ApiParam("Identificador único de la orden. No puede ser vacio.")
                                                                   @PathVariable String orderId,
                                                                   @ApiParam("Nuevo estado que puede tomar la orden. No puede ser vacio")
                                                                   @RequestBody State state) throws ExecutionException, InterruptedException {
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
