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

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private ReceivingNote receivingNote;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Product product;

    @NotNull
    private int quantity;

    public ReceivingNoteDetail() { }

    public ReceivingNoteDetail(int id, @NotNull ReceivingNote receivingNote, @NotNull Product product, @NotNull int quantity) {
        this.id = id;
        this.receivingNote = receivingNote;
        this.product = product;
        this.quantity = quantity;
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
}
