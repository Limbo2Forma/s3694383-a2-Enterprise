package model;

public class Inventory {
    private Product product;
    private long quantity = 0;

    public Inventory(Product product, long quantity){
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct(){ return product; }

    public long getQuantity(){ return quantity; }
}
