package controller;

import model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import store.CustomerStore;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/customers")
public class CustomerController {
    private CustomerStore customerStore;
    @Autowired
    public void setCustomerStore(CustomerStore customerStore) {
        this.customerStore = customerStore;
    }

    @GetMapping(path="")
    public List<Customer> getAllCustomers() {
        return customerStore.getAllCustomers();
    }

    @GetMapping(path = "/{customerId}")
    public Customer getCustomerById(@PathVariable int customerId){ return customerStore.getCustomerById(customerId); }

    @PostMapping(path = "")
    public int addCustomer(@RequestBody Customer customer){ return customerStore.addCustomer(customer); }

    @DeleteMapping(path = "/{customerId}")
    public void deleteCustomer(@PathVariable int customerId){
        customerStore.deleteCustomer(customerId);
    }

    @PutMapping(path = "")
    public void updateCustomer(@RequestBody Customer customer){
        customerStore.updateCustomer(customer);
    }

    @GetMapping(path ="/name/{name}")
    public List<Customer> getCustomerByName(@PathVariable String name){ return customerStore.getCustomerByName(name); }

    @GetMapping(path ="/address/{address}")
    public List<Customer> getCustomerByAddress(@PathVariable String address){ return customerStore.getCustomerByAddress(address); }

    @GetMapping(path ="/phone/{phone}")
    public List<Customer> getCustomerByPhone(@PathVariable String phone){ return customerStore.getCustomerByPhone(phone); }
}
