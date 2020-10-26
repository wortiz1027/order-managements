package co.edu.javeriana.orders.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Products {
    private List<Product> products;

    /*
    {
        this.services = initializeProducts();
    }

    private List<Product> initializeProducts() {
        return new ArrayList<>();
    }

    public void addProduct(final Product product) {
        this.products.add(product);
    }
     */
}
