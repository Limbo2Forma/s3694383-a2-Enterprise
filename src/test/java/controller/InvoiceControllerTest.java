package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import config.GlobalVar;
import model.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.InvoiceStore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class InvoiceControllerTest {

    public InvoiceStore store = mock(InvoiceStore.class);
    public MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest() {
        InvoiceController invoiceController = new InvoiceController();
        invoiceController.setInvoiceStore(store);
        mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();
    }

    public Invoice invoice = MockData.invoice;
    public List<Invoice> invoiceList = MockData.invoiceList;

    @Nested
    @DisplayName("getAllInvoice test")
    class GetAllInvoicesTest {
        ResultActions getAllPage0() throws Exception {
            return mockMvc.perform(get("/invoices/p={page}",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getAllPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Invoice in database")
        class WhenNoInvoiceInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getAllInvoices(0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getAllPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 Invoices in database")
        class WhenInvoicesInDb {
            @BeforeEach
            void storeReturnInvoiceList() {
                given(store.getAllInvoices(0)).willReturn(invoiceList);
            }
            @Test
            @DisplayName("Should display 2 Invoices")
            void responseInvoiceList() throws Exception {
                String resultJson = getAllPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(invoiceList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getInvoiceById test")
    class GetInvoiceByIdTest {
        ResultActions getById1() throws Exception {
            return mockMvc.perform(get("/invoices/{id}", 1));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getById1().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Invoice with id")
        class WhenNoInvoiceWithId{

            @BeforeEach
            void storeReturnNoInvoice() {
                given(store.getInvoiceById(1)).willReturn(null);
            }

            @Test
            @DisplayName("Should display zero todo items")
            void responseEmptyObject() throws Exception {
                String json = getById1().andReturn().getResponse().getContentAsString();
                assertEquals("", json);
            }
        }

        @Nested
        @DisplayName("When exist Invoices with id")
        class WhenInvoiceInDb {
            @BeforeEach
            void storeReturnInvoice() {
                given(store.getInvoiceById(1)).willReturn(invoice);
            }
            @Test
            @DisplayName("Should display Invoice")
            void responseInvoice() throws Exception {
                String resultJson = getById1().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(invoice);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addInvoice test")
    class AddInvoiceTest {
        ResultActions addImportedOrder2() throws Exception {
            String requestJson = "{\n" +
                    "    \"date\": \"30-12-2019\",\n" +
                    "    \"staff\": {\n" +
                    "        \"id\": 3\n" +
                    "    }\n" +
                    "}\n";
            return mockMvc.perform(post("/invoices/{id}",2).contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            addImportedOrder2().andExpect(status().isOk());
        }
        @Test
        @DisplayName("Should display newly added invoice Id, the value is always 0 due to the auto generated id")
        void responseInvoiceId() throws Exception {
            String json = addImportedOrder2().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateInvoice test")
    class UpdateInvoiceTest {
        ResultActions update() throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(invoice);

            return mockMvc.perform(put("/invoices").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated invoice message")
        void responseUpdateMessage() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Invoice and its details with id: 1", json);
        }
    }
    @Nested
    @DisplayName("deleteInvoice test")
    class DeleteInvoiceTest {

        ResultActions deleteId3() throws Exception {
            return mockMvc.perform(delete("/invoices/{invoiceId}", 3));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId3().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display deleted Invoice message")
        void responseDeleteMessage() throws Exception {
            String json = deleteId3().andReturn().getResponse().getContentAsString();
            assertEquals("deleted Invoice and its details with id: 3", json);
        }
    }
    @Nested
    @DisplayName("getInvoiceByCustomer test")
    class GetInvoiceByCustomerTest {
        ResultActions getByCustomer1Page0() throws Exception {
            return mockMvc.perform(get("/invoices/customer={id}/p={page}",1,0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByCustomer1Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Invoice in database")
        class WhenNoInvoiceInDb{

            @BeforeEach
            void storeReturnEmptyList(){
                given(store.getInvoicesByCustomer(1, 0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByCustomer1Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 Invoice in database")
        class WhenInvoicesInDb {
            @BeforeEach
            void storeReturnInvoiceList(){
                given(store.getInvoicesByCustomer(1, 0)).willReturn(invoiceList);
            }
            @Test
            @DisplayName("Should display 2 invoices")
            void responseInvoiceList() throws Exception {
                String resultJson = getByCustomer1Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(invoiceList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getInvoiceByDate test")
    class GetInvoiceByDateTest {
        ResultActions getByDatePage0Date28022019() throws Exception {
            return mockMvc.perform(get("/invoices/date={date}/p={page}","28-02-2019",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByDatePage0Date28022019().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Invoice in database")
        class WhenNoInvoiceInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getInvoicesByDate(GlobalVar.dateFormatter.parse("28-02-2019"), 0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByDatePage0Date28022019().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 Invoice in database")
        class WhenInvoicesInDb {
            @BeforeEach
            void storeReturnInvoiceList() throws Exception{
                given(store.getInvoicesByDate(GlobalVar.dateFormatter.parse("28-02-2019"), 0)).willReturn(invoiceList);
            }
            @Test
            @DisplayName("Should display 2 invoices")
            void responseInvoiceList() throws Exception {
                String resultJson = getByDatePage0Date28022019().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(invoiceList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getInvoiceFromTo test")
    class GetInvoiceFromToTest {
        ResultActions getByFrom28022019To28022020Page0() throws Exception {
            return mockMvc.perform(get("/invoices/from={from}/to={to}/p={page}","28-02-2019", "28-02-2020",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByFrom28022019To28022020Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Invoice in database")
        class WhenNoInvoiceInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getInvoicesFromTo(GlobalVar.dateFormatter.parse("28-02-2019"),
                        GlobalVar.dateFormatter.parse("28-02-2020"), 0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByFrom28022019To28022020Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When Invoices in database")
        class WhenInvoicesInDb {
            @BeforeEach
            void storeReturnInvoiceList() throws Exception{
                given(store.getInvoicesFromTo(GlobalVar.dateFormatter.parse("28-02-2019"),
                        GlobalVar.dateFormatter.parse("28-02-2020"), 0)).willReturn(invoiceList);
            }
            @Test
            @DisplayName("Should display invoices")
            void responseInvoiceList() throws Exception {
                String resultJson = getByFrom28022019To28022020Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(invoiceList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getInvoiceCustomerFromTo test")
    class GetInvoiceCustomerFromToTest {
        ResultActions getByCustomer1From2019To2020Page0() throws Exception {
            return mockMvc.perform(get("/invoices/customer={id}/from={from}/to={to}/p={page}",
                    1,"28-01-2019","28-01-2020",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByCustomer1From2019To2020Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Invoice in database")
        class WhenNoInvoiceInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getInvoicesCustomerFromTo(1,GlobalVar.dateFormatter.parse("28-01-2019"),
                        GlobalVar.dateFormatter.parse("28-01-2020"),0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByCustomer1From2019To2020Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 Invoice in database")
        class WhenInvoicesInDb {
            @BeforeEach
            void storeReturnInvoiceList() throws Exception{
                given(store.getInvoicesCustomerFromTo(1,GlobalVar.dateFormatter.parse("28-01-2019"),
                        GlobalVar.dateFormatter.parse("28-01-2020"),0)).willReturn(invoiceList);
            }
            @Test
            @DisplayName("Should display 2 invoices")
            void responseInvoiceList() throws Exception {
                String resultJson = getByCustomer1From2019To2020Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(invoiceList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getInvoiceStaffFromTo test")
    class GetInvoiceStaffFromToTest {
        ResultActions getByStaff1From2019To2020Page0() throws Exception {
            return mockMvc.perform(get("/invoices/staff={id}/from={from}/to={to}/p={page}",
                    1,"28-01-2019","28-01-2020",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByStaff1From2019To2020Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Invoice in database")
        class WhenNoInvoiceInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getInvoicesStaffFromTo(1,GlobalVar.dateFormatter.parse("28-01-2019"),
                        GlobalVar.dateFormatter.parse("28-01-2020"),0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByStaff1From2019To2020Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 Invoice in database")
        class WhenInvoicesInDb {
            @BeforeEach
            void storeReturnInvoiceList() throws Exception{
                given(store.getInvoicesStaffFromTo(1,GlobalVar.dateFormatter.parse("28-01-2019"),
                        GlobalVar.dateFormatter.parse("28-01-2020"),0)).willReturn(invoiceList);
            }
            @Test
            @DisplayName("Should display 2 invoices")
            void responseInvoiceList() throws Exception {
                String resultJson = getByStaff1From2019To2020Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(invoiceList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getInvoiceCustomerStaffFromTo test")
    class GetInvoiceCustomerStaffFromToTest {
        ResultActions getByCustomer2Staff1From2019To2020Page0() throws Exception {
            return mockMvc.perform(get("/invoices/customer={cid}/staff={sid}/from={from}/to={to}/p={page}",
                    2,1,"28-01-2019","28-01-2020",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByCustomer2Staff1From2019To2020Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Invoice in database")
        class WhenNoInvoiceInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getInvoicesCustomerStaffFromTo(2,1,GlobalVar.dateFormatter.parse("28-01-2019"),
                        GlobalVar.dateFormatter.parse("28-01-2020"),0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByCustomer2Staff1From2019To2020Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 Invoice in database")
        class WhenInvoicesInDb {
            @BeforeEach
            void storeReturnInvoiceList() throws Exception{
                given(store.getInvoicesCustomerStaffFromTo(2,1,GlobalVar.dateFormatter.parse("28-01-2019"),
                        GlobalVar.dateFormatter.parse("28-01-2020"),0)).willReturn(invoiceList);
            }
            @Test
            @DisplayName("Should display 2 invoices")
            void responseInvoiceList() throws Exception {
                String resultJson = getByCustomer2Staff1From2019To2020Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(invoiceList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("updateInvoicePrice test")
    class UpdateInvoiceDetailPrice {
        ResultActions update() throws Exception {
            String requestJson = "{\"id\":1,\"price\":9999}";
            return mockMvc.perform(put("/invoices/detail/price").contentType(MediaType.APPLICATION_JSON).content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated invoice message")
        void responseUpdateMessage() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Invoice Detail id 1 with price: 9999", json);
        }
    }
}