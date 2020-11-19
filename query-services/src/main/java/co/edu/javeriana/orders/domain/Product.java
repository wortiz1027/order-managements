package co.edu.javeriana.orders.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Estructura de una producto.")
public final class Product implements Serializable {
    @ApiModelProperty(notes = "Identificador único del producto.")
    private String id;
    @ApiModelProperty(notes = "Código único del producto.")
    private String code;
    @ApiModelProperty(notes = "Precio por el cual se esta comprando el producto.")
    private double price;
    @ApiModelProperty(notes = "Cantidad de productos a ser comprados.")
    private int quantity;
}
