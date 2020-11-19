package co.edu.javeriana.orders.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Estructura de una orden de compra.")
public final class Order implements Serializable {
    @ApiModelProperty(notes = "Identificador único de la orden.")
    private String id;
    @ApiModelProperty(notes = "Código único de la orden.")
    private String code;
    @ApiModelProperty(notes = "Fecha de creación de la orden.")
    private LocalDate creationDate;
    @ApiModelProperty(notes = "Cliente que realiza el pedido.")
    private Customer customer;
    @ApiModelProperty(notes = "Lista de productos que se desean comprar.")
    private List<Product> products;
    @ApiModelProperty(notes = "Medio de pago mediante el cual se desea efecturar la cancelación del costo de la orden.")
    private Payment payment;
    @ApiModelProperty(notes = "Estado que toma la orden al momento de ser gestionada.")
    private State state;
    private String status;
    @ApiModelProperty(notes = "Valor total a pagar por los productos asociados a la orden.")
    private double total;

    public void totalToPay() {
        products.forEach((final Product product) -> {this.total = this.total + product.getPrice() * product.getQuantity();});
    }
}