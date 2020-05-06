package service;

import model.Product;
import model.ProductCategory;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ProductService {
    private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Product> getAllProducts(){
        return sessionFactory.getCurrentSession().createQuery("from Product").list();
    }

    public Product getProductById(int productId){
        return sessionFactory.getCurrentSession().get(Product.class, productId);
    }

    public int addProduct(Product product){
        product.setCurrentQuantity(0);
        sessionFactory.getCurrentSession().save(product);
        return product.getId();
    }

    public void updateProduct(Product product){
        product.setCurrentQuantity(getProductById(product.getId()).getCurrentQuantity());
        sessionFactory.getCurrentSession().update(product);
    }

    public void deleteProduct(int productId){
        Product product = getProductById(productId);
        sessionFactory.getCurrentSession().delete(product);
    }

    public List<Product> getProductByName(String productName){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Product as p where p.name like :productName ");
        query.setParameter("productName", "%" + productName + "%");
        return query.list();
    }

    public List<Product> getProductByCategory(int categoryId){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Product as p where p.productCategory.id = :categoryId ");
        query.setParameter("categoryId", categoryId);
        return query.list();
    }

    public List<ProductCategory> getProductCategories() {
        return sessionFactory.getCurrentSession().createQuery("from ProductCategory ").list();
    }

    public ProductCategory getProductCategoryById(int categoryId) {
        return sessionFactory.getCurrentSession().get(ProductCategory.class, categoryId);
    }

    public int addProductCategory(ProductCategory category){
        sessionFactory.getCurrentSession().save(category);
        return category.getId();
    }

    public void updateProductCategory(ProductCategory category){
        sessionFactory.getCurrentSession().update(category);
    }

    public void deleteProductCategory(int categoryId){
        ProductCategory category = getProductCategoryById(categoryId);
        sessionFactory.getCurrentSession().delete(category);
    }
}
