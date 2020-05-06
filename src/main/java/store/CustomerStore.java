package store;

import model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.CustomerService;

import java.util.List;

@Transactional
@Service
public class CustomerStore {
    private CustomerService customerService;
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    public Customer getCustomerById(int customerId){
        return customerService.getCustomerById(customerId);
    }

    public int addCustomer(Customer customer){ return customerService.addCustomer(customer); }

    public void updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
    }

    public void deleteCustomer(int customerId){
        customerService.deleteCustomer(customerId);
    }

    public List<Customer> getCustomerByName(String customerName){ return customerService.getCustomerByName(customerName); }

    public List<Customer> getCustomerByAddress(String address){ return customerService.getCustomerByAddress(address); }

    public List<Customer> getCustomerByPhone(String phone){
        return customerService.getCustomerByPhone(phone);
    }
}
