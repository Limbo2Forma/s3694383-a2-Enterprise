package service;

import model.Customer;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CustomerService {
    private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Customer> getAllCustomers(){
        return sessionFactory.getCurrentSession().createQuery("from Customer").list();
    }

    public Customer getCustomerById(int customerId){
        return sessionFactory.getCurrentSession().get(Customer.class, customerId);
    }

    public int addCustomer(Customer customer){
        sessionFactory.getCurrentSession().save(customer);
        return customer.getId();
    }

    public void updateCustomer(Customer customer){
        sessionFactory.getCurrentSession().update(customer);
    }

    public void deleteCustomer(int customerId){
        Customer customer = getCustomerById(customerId);
        sessionFactory.getCurrentSession().delete(customer);
    }

    public List<Customer> getCustomerByName(String name){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Customer as p where p.name like :customerName ");
        query.setParameter("customerName", "%" + name + "%");
        return query.list();
    }

    public List<Customer> getCustomerByAddress(String address){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Customer as p where p.address like :address ");
        query.setParameter("address", "%" + address + "%");
        return query.list();
    }

    public List<Customer> getCustomerByPhone(String phone){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Customer as p where p.phone like :phone ");
        query.setParameter("phone", "%" + phone + "%");
        return query.list();
    }
}
