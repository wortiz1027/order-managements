package co.edu.javeriana.orders.application.dto;

import co.edu.javeriana.orders.domain.Order;
import co.edu.javeriana.orders.domain.State;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Estructura de reespuesta asociada a las acciones realizadas sobre una petición.")
public final class Response implements Serializable {
    @ApiModelProperty(notes = "Estado que puede tomar una respuesta.")
    private Status status;
    @ApiModelProperty(notes = "Estructura de una orde; para consultas de detalle.")
    private Order order;
    @ApiModelProperty(notes = "Colección de entidades del tipo Ordenes.")
    private Map<String, Object> data;
    @ApiModelProperty(notes = "Estado actual en el que se encuentra una orden en cierto momento del proceso.")
    private State state;
}

