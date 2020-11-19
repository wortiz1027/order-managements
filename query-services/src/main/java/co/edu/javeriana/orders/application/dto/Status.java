package co.edu.javeriana.orders.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "Estructura asociada al estado que puede tomar una respuesta.")
public class Status implements Serializable {
    @ApiModelProperty(notes = "Código de estado de la repuesta.")
    private String code;
    @ApiModelProperty(notes = "Descripción detallada de la respuesta.")
    private String description;
}
