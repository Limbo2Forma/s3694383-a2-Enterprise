package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class ReceivingNoteDetail {
    @Id
    private int id;

    @OneToOne(cascade=CascadeType.PERSIST, fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "id")
    private ProviderOrderDetail providerOrderDetail;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private ReceivingNote receivingNote;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Product product;

    @NotNull
    private int quantity;

    public ReceivingNoteDetail() { }

    public ReceivingNoteDetail(ProviderOrderDetail providerOrderDetail) {
        this.providerOrderDetail = providerOrderDetail;
        this.product = providerOrderDetail.getProduct();
        this.quantity = providerOrderDetail.getQuantity();
        this.product.setCurrentQuantity(this.product.getCurrentQuantity() + this.quantity);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    @JsonIgnore
    public ReceivingNote getReceivingNote() {
        return receivingNote;
    }

    @JsonProperty
    public void setReceivingNote(ReceivingNote receivingNote) {
        this.receivingNote = receivingNote;
    }

    public Product getProduct() { return product; }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProviderOrderDetail(ProviderOrderDetail providerOrderDetail) {
        this.providerOrderDetail = providerOrderDetail;
        this.product = providerOrderDetail.getProduct();
        this.quantity = providerOrderDetail.getQuantity();
        this.product.setCurrentQuantity(this.product.getCurrentQuantity() + this.quantity);
    }
}
