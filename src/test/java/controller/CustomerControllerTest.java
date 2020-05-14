package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.CustomerStore;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class CustomerControllerTest {

    public CustomerStore store = mock(CustomerStore.class);
    public MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest() {
        CustomerController customerController = new CustomerController();
        customerController.setCustomerStore(store);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Nested
    @DisplayName("getAllCustomer test")
    class GetAllCustomers {
        ResultActions getAllPage0() throws Exception {
            return mockMvc.perform(get("/customers/p={page}",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getAllPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no customer in database")
        class WhenNoCustomerInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getAllCustomers(0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getAllPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 customers in database")
        class When2CustomerInDb {
            @BeforeEach
            void storeReturn2Customers() {
                Customer c1 = new Customer(1, "customer 1","address 1","0123576829","fax 1",
                        "email1@email.com","contact me 1");
                Customer c2 = new Customer(2, "customer 12","address 12","01232226829","fax 2",
                        "email12@email.com","contact me 22");
                given(store.getAllCustomers(0)).willReturn(Arrays.asList(c1, c2));
            }
            @Test
            @DisplayName("Should display 2 customers")
            void response2Customers() throws Exception {
                String json = getAllPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[{\"id\":1,\"name\":\"customer 1\",\"address\":\"address 1\",\"phone\":\"0123576829\",\"fax\":\"fax 1\",\"email\":\"email1@email.com\",\"contact\":\"contact me 1\"},{\"id\":2,\"name\":\"customer 12\",\"address\":\"address 12\",\"phone\":\"01232226829\",\"fax\":\"fax 2\",\"email\":\"email12@email.com\",\"contact\":\"contact me 22\"}]", json);
            }
        }
    }
    @Nested
    @DisplayName("getCustomerById test")
    class GetCustomerById {
        ResultActions getById1() throws Exception {
            return mockMvc.perform(get("/customers/{id}", 1));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getById1().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no customer with id")
        class WhenNoCustomerWithId{

            @BeforeEach
            void storeReturnNoCustomer() {
                given(store.getCustomerById(1)).willReturn(null);
            }

            @Test
            @DisplayName("Should display zero todo items")
            void responseEmptyObject() throws Exception {
                String json = getById1().andReturn().getResponse().getContentAsString();
                assertEquals("", json);
            }
        }

        @Nested
        @DisplayName("When exist customers with id")
        class When2CustomerInDb {
            @BeforeEach
            void storeReturnCustomer() {
                Customer c1 = new Customer(1, "customer 1","address 1","0123576829","fax 1",
                        "email1@email.com","contact me 1");
                given(store.getCustomerById(1)).willReturn(c1);
            }
            @Test
            @DisplayName("Should display 2 customers")
            void responseCustomerObject() throws Exception {
                String json = getById1().andReturn().getResponse().getContentAsString();
                assertEquals("{\"id\":1,\"name\":\"customer 1\",\"address\":\"address 1\",\"phone\":\"0123576829\",\"fax\":\"fax 1\",\"email\":\"email1@email.com\",\"contact\":\"contact me 1\"}", json);
            }
        }
    }

    @Nested
    @DisplayName("addCustomer test")
    class AddCustomer {
        Customer c = new Customer();

        ResultActions addCustomer(Customer c) throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(c);

            return mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @BeforeEach
        void storeReturnCustomer() {
            c.setName("customer 1");
            c.setAddress("address 1");
            c.setPhone("0123576829");
            c.setFax("fax 1");
            c.setEmail("email1@email.com");
            c.setContact("contact me 1");

            given(store.addCustomer(c)).willReturn(0);
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            addCustomer(c).andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display newly added customer Id")
        void responseCustomerObject() throws Exception {
            String json = addCustomer(c).andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateCustomer test")
    class UpdateCustomerTest {
        Customer c = new Customer(2, "customer 12","address 12","01232226829","fax 2",
                "email12@email.com","contact me 22");

        ResultActions updateCustomer(Customer c) throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(c);

            return mockMvc.perform(put("/customers").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            updateCustomer(c).andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated customer message")
        void responseCustomerObject() throws Exception {
            String json = updateCustomer(c).andReturn().getResponse().getContentAsString();
            assertEquals("updated customer with id: 2", json);
        }
    }
    @Nested
    @DisplayName("deleteCustomer test")
    class DeleteCustomerTest {

        ResultActions deleteCustomerId3() throws Exception {
            return mockMvc.perform(delete("/customers/{customerId}", 3));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteCustomerId3().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated customer message")
        void responseCustomerObject() throws Exception {
            String json = deleteCustomerId3().andReturn().getResponse().getContentAsString();
            assertEquals("deleted customer with id: 3", json);
        }
    }
}