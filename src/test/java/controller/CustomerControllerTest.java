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
import java.util.List;

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

    public Customer customer = MockData.customer;
    public List<Customer> customerList = MockData.customerList;

    @Nested
    @DisplayName("getAllCustomer test")
    class GetAllCustomersTest {
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
        class WhenCustomersInDb {
            @BeforeEach
            void storeReturnCustomerList() {
                given(store.getAllCustomers(0)).willReturn(MockData.customerList);
            }
            @Test
            @DisplayName("Should display 2 customers")
            void responseCustomerList() throws Exception {
                String resultJson = getAllPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(customerList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getCustomerById test")
    class GetCustomerByIdTest {
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
        class WhenCustomerInDb {
            @BeforeEach
            void storeReturnCustomer() {
                given(store.getCustomerById(1)).willReturn(MockData.customer);
            }
            @Test
            @DisplayName("Should display customer")
            void responseCustomer() throws Exception {
                String resultJson = getById1().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(MockData.customer);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addCustomer test")
    class AddCustomerTest {
        ResultActions add() throws Exception {
            String requestJson = "{\n" +
                    "        \"name\": \"customer 1a\",\n" +
                    "        \"address\": \"address 1a\",\n" +
                    "        \"phone\": \"0912782263\",\n" +
                    "        \"fax\": \"fax 2a\",\n" +
                    "        \"email\": \"email2@email.com\",\n" +
                    "        \"contact\": \"contact me pls\"\n" +
                    "}\n";

            return mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            add().andExpect(status().isOk());
        }
        @Test
        @DisplayName("Should display newly added customer Id, the value is always 0 due to the auto generated id")
        void responseCustomerId() throws Exception {
            String json = add().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateCustomer test")
    class UpdateCustomerTest {
        ResultActions update() throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(customer);

            return mockMvc.perform(put("/customers").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated customer message")
        void responseUpdateMessage() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Customer with id: 1", json);
        }
    }
    @Nested
    @DisplayName("deleteCustomer test")
    class DeleteCustomerTest {

        ResultActions deleteId3() throws Exception {
            return mockMvc.perform(delete("/customers/{customerId}", 3));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId3().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated customer message")
        void responseDeleteMessage() throws Exception {
            String json = deleteId3().andReturn().getResponse().getContentAsString();
            assertEquals("deleted Customer with id: 3", json);
        }
    }
    @Nested
    @DisplayName("getCustomersByName test")
    class GetCustomersByNameTest {
        ResultActions getByNamePage0Name1() throws Exception {
            return mockMvc.perform(get("/customers/name={name}/p={page}","1",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByNamePage0Name1().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no customer in database")
        class WhenNoCustomerInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getCustomerByName("1", 0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByNamePage0Name1().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 customers in database")
        class WhenCustomersInDb {
            @BeforeEach
            void storeReturnCustomerList() {
                given(store.getCustomerByName("1",0)).willReturn(customerList);
            }
            @Test
            @DisplayName("Should display 2 customers")
            void responseCustomerList() throws Exception {
                String resultJson = getByNamePage0Name1().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(customerList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getCustomersByPhone test")
    class GetCustomersByPhoneTest {
        ResultActions getByPhone012Page0() throws Exception {
            return mockMvc.perform(get("/customers/phone={phone}/p={page}","012",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByPhone012Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no customer in database")
        class WhenNoCustomerInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getCustomerByPhone("012",0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByPhone012Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 customers in database")
        class WhenCustomersInDb {
            @BeforeEach
            void storeReturnCustomerList() {
                given(store.getCustomerByPhone("012",0)).willReturn(customerList);
            }
            @Test
            @DisplayName("Should display 2 customers")
            void responseCustomerList() throws Exception {
                String resultJson = getByPhone012Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(customerList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getCustomersByAddress test")
    class GetCustomersByAddressTest {
        ResultActions getByAddressAddrPage0() throws Exception {
            return mockMvc.perform(get("/customers/address={address}/p={page}","addr",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByAddressAddrPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no customer in database")
        class WhenNoCustomerInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getCustomerByAddress("addr",0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByAddressAddrPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 customers in database")
        class WhenCustomersInDb {
            @BeforeEach
            void storeReturnCustomerList() {
                given(store.getCustomerByAddress("addr",0)).willReturn(customerList);
            }
            @Test
            @DisplayName("Should display 2 customers")
            void responseCustomerList() throws Exception {
                String resultJson = getByAddressAddrPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(customerList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
}