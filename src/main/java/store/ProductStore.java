package store;

import model.Product;
import model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.ProductService;

import java.util.List;

@Transactional
@Service
public class ProductStore {
    private ProductService productService;
    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getAllProducts(int page){
        return productService.getAllProducts(page);
    }

    public Product getProductById(int productId){
        return productService.getProductById(productId);
    }

    public int addProduct(Product product){
        return productService.addProduct(product);
    }

    public void updateProduct(Product product){
        productService.updateProduct(product);
    }

    public void deleteProduct(int productId){
        productService.deleteProduct(productId);
    }

    public List<Product> getProductByName(String productName, int page){
       return productService.getProductByName(productName, page);
    }

    public List<Product> getProductByCategory(int categoryId, int page){
        return productService.getProductByCategory(categoryId, page); }

    public List<ProductCategory> getProductCategories() { return productService.getProductCategories(); }

    public int addProductCategory(ProductCategory category) { return productService.addProductCategory(category); }

    public void updateProductCategory(ProductCategory category) { productService.updateProductCategory(category); }

    public void deleteProductCategory(int categoryId) { productService.deleteProductCategory(categoryId); }
}
