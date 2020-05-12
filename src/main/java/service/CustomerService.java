package service;

import config.GlobalVar;
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

    public List<Customer> getAllCustomers(int page){
        Query query = sessionFactory.getCurrentSession().createQuery("from Customer");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
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

    public List<Customer> getCustomerByName(String name, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Customer as p where p.name like :customerName ");
        query.setParameter("customerName", "%" + name + "%");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Customer> getCustomerByAddress(String address, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Customer as p where p.address like :address ");
        query.setParameter("address", "%" + address + "%");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Customer> getCustomerByPhone(String phone, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Customer as p where p.phone like :phone ");
        query.setParameter("phone", "%" + phone + "%");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }
}
