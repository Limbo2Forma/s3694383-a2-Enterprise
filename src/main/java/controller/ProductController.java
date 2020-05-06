package controller;

import model.Product;
import model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import store.ProductStore;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/products")
public class ProductController {
    private ProductStore productStore;
    @Autowired
    public void setProductStore(ProductStore productStore) { this.productStore = productStore; }

    @GetMapping(path="")
    public List<Product> getAllProduct() {
        return productStore.getAllProducts();
    }

    @GetMapping(path="/{productId}")
    public Product getProductById(@PathVariable int productId) { return productStore.getProductById(productId); }

    @PostMapping(path="")
    public int addProduct(@RequestBody Product product) { return productStore.addProduct(product); }

    @PutMapping(path="")
    public void updateProduct(@RequestBody Product product) { productStore.updateProduct(product); }

    @DeleteMapping(path="/{productId}")
    public void deleteProduct(@PathVariable int productId) { productStore.deleteProduct(productId); }

    @GetMapping(path="/name/{productName}")
    public List<Product> getProductByName(@PathVariable String productName) { return productStore.getProductByName(productName); }

    @GetMapping(path="/category/{categoryId}")
    public List<Product> getProductByCategory(@PathVariable int categoryId) { return productStore.getProductByCategory(categoryId); }

    @GetMapping(path="/categories")
    public List<ProductCategory> getAllProductCategory() {
        return productStore.getProductCategories();
    }

    @GetMapping(path="/categories/{categoryId}")
    public ProductCategory getProductCategoryById(@PathVariable int categoryId) { return productStore.getProductCategoryById(categoryId); }

    @PostMapping(path="/categories")
    public int addProductCategory(@RequestBody ProductCategory category) { return productStore.addProductCategory(category); }

    @PutMapping(path="/categories")
    public void updateProductCategory(@RequestBody ProductCategory category) { productStore.updateProductCategory(category); }

    @DeleteMapping(path="/categories/{categoryId}")
    public void deleteProductCategory(@PathVariable int categoryId) { productStore.deleteProductCategory(categoryId); }
}
