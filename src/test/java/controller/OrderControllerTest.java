package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import config.GlobalVar;
import model.ProviderOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.ProviderOrderStore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class OrderControllerTest {

    public ProviderOrderStore store = mock(ProviderOrderStore.class);
    public MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest() {
        ProviderOrderController providerOrderController = new ProviderOrderController();
        providerOrderController.setProviderOrderStore(store);
        mockMvc = MockMvcBuilders.standaloneSetup(providerOrderController).build();
    }

    public ProviderOrder providerOrder = MockData.providerOrder;
    public List<ProviderOrder> providerOrderList = MockData.providerOrderList;

    @Nested
    @DisplayName("getAllOrder test")
    class GetAllOrdersTest {
        ResultActions getAllPage0() throws Exception {
            return mockMvc.perform(get("/orders/p={page}",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getAllPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Order in database")
        class WhenNoProviderOrderInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getAllOrders(0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getAllPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 Orders in database")
        class WhenProviderOrdersInDb {
            @BeforeEach
            void storeReturnProviderOrderList() {
                given(store.getAllOrders(0)).willReturn(providerOrderList);
            }
            @Test
            @DisplayName("Should display 2 Orders")
            void responseProviderOrderList() throws Exception {
                String resultJson = getAllPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(providerOrderList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getOrderById test")
    class GetOrderByIdTest {
        ResultActions getById1() throws Exception {
            return mockMvc.perform(get("/orders/{id}", 1));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getById1().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Order with id")
        class WhenNoProviderOrderWithId{

            @BeforeEach
            void storeReturnNoProviderOrder() {
                given(store.getOrderById(1)).willReturn(null);
            }

            @Test
            @DisplayName("Should display zero todo items")
            void responseEmptyObject() throws Exception {
                String json = getById1().andReturn().getResponse().getContentAsString();
                assertEquals("", json);
            }
        }

        @Nested
        @DisplayName("When exist Orders with id")
        class WhenProviderOrderInDb {
            @BeforeEach
            void storeReturnProviderOrder() {
                given(store.getOrderById(1)).willReturn(providerOrder);
            }
            @Test
            @DisplayName("Should display Order")
            void responseProviderOrder() throws Exception {
                String resultJson = getById1().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(providerOrder);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addOrder test")
    class AddOrderTest {
        ResultActions add() throws Exception {
            String requestJson = "{\n" +
                    "    \"date\": \"28-09-2019\",\n" +
                    "    \"staff\": {\"id\": 3},\n" +
                    "    \"provider\": {\"id\": 2},\n" +
                    "    \"providerOrderDetails\": [\n" +
                    "        {\n" +
                    "            \"product\": {\"id\": 1},\n" +
                    "            \"price\": 5,\n" +
                    "            \"quantity\": 5\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"product\": {\"id\": 2},\n" +
                    "            \"price\": 5,\n" +
                    "            \"quantity\": 5\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}\n";
            return mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            add().andExpect(status().isOk());
        }
        @Test
        @DisplayName("Should display newly added providerOrder Id, the value is always 0 due to the auto generated id")
        void responseProviderOrderId() throws Exception {
            String json = add().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateOrder test")
    class UpdateOrderTest {
        ResultActions update() throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(providerOrder);

            return mockMvc.perform(put("/orders").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated providerOrder message")
        void responseUpdateMessage() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Order and its details with id: 1", json);
        }
    }
    @Nested
    @DisplayName("deleteOrder test")
    class DeleteOrderTest {

        ResultActions deleteId3() throws Exception {
            return mockMvc.perform(delete("/orders/{providerOrderId}", 3));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId3().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display deleted Order message")
        void responseDeleteMessage() throws Exception {
            String json = deleteId3().andReturn().getResponse().getContentAsString();
            assertEquals("deleted Order and its details with id: 3", json);
        }
    }
    @Nested
    @DisplayName("getOrderByDate test")
    class GetOrderByDateTest {
        ResultActions getByDatePage0Date28022019() throws Exception {
            return mockMvc.perform(get("/orders/date={date}/p={page}","28-02-2019",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByDatePage0Date28022019().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Order in database")
        class WhenNoProviderOrderInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getOrdersByDate(GlobalVar.dateFormatter.parse("28-02-2019"), 0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByDatePage0Date28022019().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 Order in database")
        class WhenProviderOrdersInDb {
            @BeforeEach
            void storeReturnProviderOrderList() throws Exception{
                given(store.getOrdersByDate(GlobalVar.dateFormatter.parse("28-02-2019"), 0)).willReturn(providerOrderList);
            }
            @Test
            @DisplayName("Should display 2 providerOrders")
            void responseProviderOrderList() throws Exception {
                String resultJson = getByDatePage0Date28022019().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(providerOrderList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getOrderByProvider test")
    class GetOrderByProviderTest {
        ResultActions getByProvider1Page0() throws Exception {
            return mockMvc.perform(get("/orders/provider={id}/p={page}",1,0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByProvider1Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Order in database")
        class WhenNoProviderOrderInDb{

            @BeforeEach
            void storeReturnEmptyList(){
                given(store.getOrdersByProvider(1, 0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByProvider1Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 Order in database")
        class WhenProviderOrdersInDb {
            @BeforeEach
            void storeReturnProviderOrderList(){
                given(store.getOrdersByProvider(1, 0)).willReturn(providerOrderList);
            }
            @Test
            @DisplayName("Should display 2 providerOrders")
            void responseProviderOrderList() throws Exception {
                String resultJson = getByProvider1Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(providerOrderList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getOrderFromTo test")
    class GetOrderFromToTest {
        ResultActions getByFrom28022019To28022020Page0() throws Exception {
            return mockMvc.perform(get("/orders/from={from}/to={to}/p={page}","28-02-2019", "28-02-2020",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByFrom28022019To28022020Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Order in database")
        class WhenNoProviderOrderInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getOrdersFromTo(GlobalVar.dateFormatter.parse("28-02-2019"),
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
        @DisplayName("When Orders in database")
        class WhenProviderOrdersInDb {
            @BeforeEach
            void storeReturnProviderOrderList() throws Exception{
                given(store.getOrdersFromTo(GlobalVar.dateFormatter.parse("28-02-2019"),
                        GlobalVar.dateFormatter.parse("28-02-2020"), 0)).willReturn(providerOrderList);
            }
            @Test
            @DisplayName("Should display orders")
            void responseProviderOrderList() throws Exception {
                String resultJson = getByFrom28022019To28022020Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(providerOrderList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getOrderDetails test")
    class GetOrderDetailsFromId {
        ResultActions getDetailsById() throws Exception {
            return mockMvc.perform(get("/orders/detail/{id}",1));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getDetailsById().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no Order Detail in database")
        class WhenNoOrderDetailsInDb{

            @BeforeEach
            void storeReturnEmptyList(){
                given(store.getOrderDetailByOrderId(1)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getDetailsById().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When Orders in database")
        class WhenOrderDetailsInDb {
            @BeforeEach
            void storeReturnProviderOrderList(){
                given(store.getOrderDetailByOrderId(1)).willReturn(providerOrder.getProviderOrderDetails());
            }
            @Test
            @DisplayName("Should display orders")
            void responseProviderOrderList() throws Exception {
                String resultJson = getDetailsById().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(providerOrder.getProviderOrderDetails());

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addOrderDetail test")
    class AddOrderDetailTest {
        ResultActions add() throws Exception {
            String requestJson = "{\n" +
                    "    \"product\": {\n" +
                    "        \"id\": 3\n" +
                    "    },\n" +
                    "    \"providerOrder\": {\n" +
                    "        \"id\": 1\n" +
                    "    },\n" +
                    "    \"price\": 500,\n" +
                    "    \"quantity\": 1\n" +
                    "}\n";
            return mockMvc.perform(post("/orders/detail").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            add().andExpect(status().isOk());
        }
        @Test
        @DisplayName("Should display newly added providerOrder Id, the value is always 0 due to the auto generated id")
        void responseProviderOrderId() throws Exception {
            String json = add().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateOrderDetail test")
    class UpdateOrderDetailTest {
        ResultActions update() throws Exception {
            String requestJson = "{\"id\":1, \"product\":{\"id\":1},\"providerOrder\":{\"id\":1},\"price\":1000, \"quantity\":100}";
            return mockMvc.perform(put("/orders/detail").contentType(MediaType.APPLICATION_JSON).content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated providerOrder message")
        void responseUpdateMessage() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Order Detail with id: 1 from Order with id: 1", json);
        }
    }
    @Nested
    @DisplayName("deleteOrder test")
    class DeleteOrderDetailTest {

        ResultActions deleteId3() throws Exception {
            return mockMvc.perform(delete("/orders/detail/{id}", 3));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId3().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display deleted Order Detail message")
        void responseDeleteMessage() throws Exception {
            String json = deleteId3().andReturn().getResponse().getContentAsString();
            assertEquals("delete Order Detail with id: 3 from Order with id: 0", json);
        }
    }
}