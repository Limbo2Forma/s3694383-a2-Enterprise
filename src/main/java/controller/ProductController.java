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

    @GetMapping(path="/p={page}")
    public List<Product> getAllProduct(@PathVariable int page) {
        return productStore.getAllProducts(page);
    }

    @GetMapping(path="/{productId}")
    public Product getProductById(@PathVariable int productId) { return productStore.getProductById(productId); }

    @PostMapping(path="")
    public int addProduct(@RequestBody Product product) { return productStore.addProduct(product); }

    @PutMapping(path="")
    public String updateProduct(@RequestBody Product product) {
        productStore.updateProduct(product);
        return "updated Product with id: " + product.getId();
    }

    @DeleteMapping(path="/{productId}")
    public String deleteProduct(@PathVariable int productId) {
        productStore.deleteProduct(productId);
        return "deleted Product with id: " + productId;
    }

    @GetMapping(path="/name={productName}/p={page}")
    public List<Product> getProductByName(@PathVariable String productName, @PathVariable int page) {
        return productStore.getProductByName(productName, page);
    }

    @GetMapping(path="/category={categoryId}/p={page}")
    public List<Product> getProductByCategory(@PathVariable int categoryId, @PathVariable int page) {
        return productStore.getProductByCategory(categoryId, page);
    }

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
