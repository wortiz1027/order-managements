package co.edu.javeriana.orders.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "Estructura de reespuesta asociada a las acciones realizadas sobre una petición.")
public final class Response implements Serializable {
    @ApiModelProperty(notes = "Estado de la repuesta.")
    private String status;
    @ApiModelProperty(notes = "Descripción detallada de la respuesta.")
    private String description;
}

