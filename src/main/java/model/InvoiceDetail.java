package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class InvoiceDetail {
    @Id
    private int id;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "id")
    private DeliveryNoteDetail deliveryNoteDetail;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Product product;

    @NotNull
    private int price;

    @NotNull
    private int quantity;

    public InvoiceDetail() { }

    public InvoiceDetail(DeliveryNoteDetail deliveryNoteDetail) {
        this.deliveryNoteDetail = deliveryNoteDetail;
        this.product = deliveryNoteDetail.getProduct();
        this.quantity = deliveryNoteDetail.getQuantity();
        this.price = product.getSinglePrice() * quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    @JsonIgnore
    public Invoice getInvoice() { return invoice; }

    @JsonProperty
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public void setDeliveryNoteDetail(DeliveryNoteDetail deliveryNoteDetail){
        this.deliveryNoteDetail = deliveryNoteDetail;
        this.product = deliveryNoteDetail.getProduct();
        this.quantity = deliveryNoteDetail.getQuantity();
        this.price = product.getSinglePrice() * quantity;
    }
}
