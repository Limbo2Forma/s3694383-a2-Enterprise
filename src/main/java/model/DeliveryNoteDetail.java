package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class DeliveryNoteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private DeliveryNote deliveryNote;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Product product;

    @NotNull
    private int quantity;

    public DeliveryNoteDetail() { }

    public DeliveryNoteDetail(@NotNull Product product, @NotNull int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    @JsonIgnore
    public DeliveryNote getDeliveryNote() { return deliveryNote; }

    @JsonProperty
    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
