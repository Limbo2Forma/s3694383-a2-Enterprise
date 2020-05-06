package model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public void setName(String name) {
        this.name = name;
    }

    @Column(unique = true)
    @NotBlank
    private String name;

    public ProductCategory() { }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
}
