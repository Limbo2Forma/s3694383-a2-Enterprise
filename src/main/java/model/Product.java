package model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String model;

    @Column
    @NotBlank
    private String brand;

    @Column
    @NotBlank
    private String company;

    @Column
    @NotBlank
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private ProductCategory productCategory;

    @Column
    @NotNull
    @Positive
    private int singlePrice;

    @Column
    @NotNull
    @PositiveOrZero
    private int currentQuantity = 0;

    public Product() { }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public String getBrand() { return brand; }

    public void setBrand(String brand) { this.brand = brand; }

    public String getCompany() { return company; }

    public void setCompany(String company) { this.company = company; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public ProductCategory getProductCategory() { return productCategory; }

    public void setProductCategory(ProductCategory productCategory) { this.productCategory = productCategory; }

    public int getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(int singlePrice) { this.singlePrice = singlePrice; }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) { this.currentQuantity = currentQuantity; }
}
