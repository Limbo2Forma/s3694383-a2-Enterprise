package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class ProviderOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private ProviderOrder providerOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Product product;

    @NotNull
    private int price;

    @NotNull
    private int quantity;

    public ProviderOrderDetail() { }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) { this.product = product; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    @JsonIgnore
    public ProviderOrder getProviderOrder() {
        return providerOrder;
    }

    @JsonProperty
    public void setProviderOrder(ProviderOrder providerOrder) {
        this.providerOrder = providerOrder;
    }
}
