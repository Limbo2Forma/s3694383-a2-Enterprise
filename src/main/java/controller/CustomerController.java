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
    public void setCustomerStore(CustomerStore customerStore) { this.customerStore = customerStore; }

    @GetMapping(path="/p={page}")
    public List<Customer> getAllCustomers(@PathVariable int page) {
        return customerStore.getAllCustomers(page);
    }

    @GetMapping(path = "/{customerId}")
    public Customer getCustomerById(@PathVariable int customerId){ return customerStore.getCustomerById(customerId); }

    @PostMapping(path = "")
    public int addCustomer(@RequestBody Customer customer){ return customerStore.addCustomer(customer); }

    @DeleteMapping(path = "/{customerId}")
    public String deleteCustomer(@PathVariable int customerId){
        customerStore.deleteCustomer(customerId);
        return "deleted Customer with id: " + customerId;
    }

    @PutMapping(path = "")
    public String updateCustomer(@RequestBody Customer customer){
        customerStore.updateCustomer(customer);
        return "updated Customer with id: " + customer.getId();
    }

    @GetMapping(path ="/name={name}/p={page}")
    public List<Customer> getCustomerByName(@PathVariable String name, @PathVariable int page){ return customerStore.getCustomerByName(name,page); }

    @GetMapping(path ="/address={address}/p={page}")
    public List<Customer> getCustomerByAddress(@PathVariable String address, @PathVariable int page){ return customerStore.getCustomerByAddress(address,page); }

    @GetMapping(path ="/phone={phone}/p={page}")
    public List<Customer> getCustomerByPhone(@PathVariable String phone, @PathVariable int page){ return customerStore.getCustomerByPhone(phone,page); }
}
