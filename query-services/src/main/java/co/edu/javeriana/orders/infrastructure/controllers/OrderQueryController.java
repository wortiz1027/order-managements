package co.edu.javeriana.orders.infrastructure.controllers;

import co.edu.javeriana.orders.application.OrderQueryService;
import co.edu.javeriana.orders.application.dto.Response;
import co.edu.javeriana.orders.domain.Status;
import io.swagger.annotations.*;
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
@Api(value="Interfaz que sirve como punto de entrada para obtener información de las ordenes.")
public final class OrderQueryController {
    private final OrderQueryService service;

    @GetMapping("/orders/open")
    @ApiOperation(value = "Sirve para consultar la lista de ordenes ABIERTAS que actualmente se encuentran registradas en la base de datos de ordenes", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Las ordenes fuerón consultadas exitosamente"),
            @ApiResponse(code = 404, message = "Error no se encontrarón ordenes en nuestro repositorio de información."),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    public ResponseEntity<CompletableFuture<Response>> allOpen(@ApiParam("Define el número de página a referenciar.")
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @ApiParam("Determina el tamaño máximo de resgistros que debe traer la consulta.")
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
    @ApiOperation(value = "Sirve para consultar la información detalladas de una orden teniendo en cuenta el identificador único de esta.", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La orden fue consultada exitosamente"),
            @ApiResponse(code = 404, message = "Error no se encontrarón ordenes en nuestro repositorio de información."),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    public ResponseEntity<CompletableFuture<Response>> detail(@ApiParam("Identificador único de la orden.")
                                                              @PathVariable String orderId) throws ExecutionException, InterruptedException {
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
    @ApiOperation(value = "Sirve para consultar el estado actual en el que se encuentra una orden cuyo identificador único es pasado como valor de entrada.", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "El estado de la orden fue consultado exitosamente"),
            @ApiResponse(code = 404, message = "Error no se encontro un estado relacionado a la orden requerida."),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    public ResponseEntity<CompletableFuture<Response>> status(@ApiParam("Identificador único de la orden.")
                                                              @PathVariable String orderId) throws ExecutionException, InterruptedException {
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
    @ApiOperation(value = "Sirve para consultar la información de una orden basado en su código.", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La orden fue consultado exitosamente"),
            @ApiResponse(code = 404, message = "Error no se encontro la orden solicitada."),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    public ResponseEntity<CompletableFuture<Response>> get(@ApiParam("Código único de la orden.")
                                                           @PathVariable String orderCode) throws ExecutionException, InterruptedException {
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
    @ApiOperation(value = "Sirve para consultar todas las ordenes que coincidan con la asociación de un producto.", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Las ordenes fue consultadas exitosamente"),
            @ApiResponse(code = 404, message = "Error no se encontrarón ordenes en nuestro repositorio de información."),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    public ResponseEntity<CompletableFuture<Response>> getByProduct(@ApiParam("Código único del producto.")
                                                                    @PathVariable String productCode,
                                                                    @ApiParam("Define el número de página a referenciar.")
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @ApiParam("Determina el tamaño máximo de resgistros que debe traer la consulta.")
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
    @ApiOperation(value = "Sirve para consultar todas las ordenes que se encuentran asociadas a un cliente.", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Las ordenes fue consultadas exitosamente"),
            @ApiResponse(code = 404, message = "Error no se encontrarón ordenes asociadas al cliente solicitado.."),
            @ApiResponse(code = 500, message = "Error interno en el servidor, contacte y reporte con el administrador")
    })
    public ResponseEntity<CompletableFuture<Response>> getByClient(@ApiParam("Identificador único del cliente.")
                                                                   @PathVariable String clientId,
                                                                   @ApiParam("Define el número de página a referenciar.")
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @ApiParam("Determina el tamaño máximo de resgistros que debe traer la consulta.")
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
