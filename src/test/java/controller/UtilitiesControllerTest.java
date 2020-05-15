package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import config.GlobalVar;
import model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.UtilitiesStore;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class UtilitiesControllerTest {

    public UtilitiesStore store = mock(UtilitiesStore.class);
    public MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest() {
        UtilitiesController customerController = new UtilitiesController();
        customerController.setUtilitiesStore(store);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Nested
    @DisplayName("getAllUtilities test")
    class GetRevenueTest {
        @Nested
        @DisplayName("Get revenue in a date range")
        class RevenueFromToTest{
            ResultActions getRevenueFromTo() throws Exception {
                return mockMvc.perform(get("/utilities/revenue/from={from}/to={to}","28-01-2019","28-01-2020"));
            }
            @BeforeEach
            void storeReturnEmptyList() throws Exception {
                given(store.revenueFromTo(GlobalVar.dateFormatter.parse("28-01-2019"),
                        GlobalVar.dateFormatter.parse("28-01-2020"))).willReturn((long) 100);
            }
            @Test
            @DisplayName("Should return the HTTP status code 200")
            void shouldReturnHttpStatusCodeOk() throws Exception {
                getRevenueFromTo().andExpect(status().isOk());
            }
            @Test
            @DisplayName("Should display revenue")
            void responseEmptyList() throws Exception {
                String json = getRevenueFromTo().andReturn().getResponse().getContentAsString();
                assertEquals("100", json);
            }
        }
        @Nested
        @DisplayName("Get revenue in a date range by a customer")
        class RevenueCustomerFromToTest{
            ResultActions getRevenueCustomerFromTo() throws Exception {
                return mockMvc.perform(get("/utilities/revenue/customer={id}/from={from}/to={to}",1,"28-01-2019","28-01-2020"));
            }
            @BeforeEach
            void storeReturnEmptyList() throws Exception {
                given(store.revenueCustomerFromTo(1,GlobalVar.dateFormatter.parse("28-01-2019"),
                        GlobalVar.dateFormatter.parse("28-01-2020"))).willReturn((long) 60);
            }
            @Test
            @DisplayName("Should return the HTTP status code 200")
            void shouldReturnHttpStatusCodeOk() throws Exception {
                getRevenueCustomerFromTo().andExpect(status().isOk());
            }
            @Test
            @DisplayName("Should display revenue")
            void responseEmptyList() throws Exception {
                String json = getRevenueCustomerFromTo().andReturn().getResponse().getContentAsString();
                assertEquals("60", json);
            }
        }
        @Nested
        @DisplayName("Get revenue in a date range by a staff")
        class RevenueStaffFromToTest{
            ResultActions getRevenueStaffFromTo() throws Exception {
                return mockMvc.perform(get("/utilities/revenue/staff={id}/from={from}/to={to}",2,"28-01-2019","28-01-2020"));
            }
            @BeforeEach
            void storeReturnEmptyList() throws Exception {
                given(store.revenueStaffFromTo(2,GlobalVar.dateFormatter.parse("28-01-2019"),
                        GlobalVar.dateFormatter.parse("28-01-2020"))).willReturn((long) 30);
            }
            @Test
            @DisplayName("Should return the HTTP status code 200")
            void shouldReturnHttpStatusCodeOk() throws Exception {
                getRevenueStaffFromTo().andExpect(status().isOk());
            }
            @Test
            @DisplayName("Should display revenue")
            void responseEmptyList() throws Exception {
                String json = getRevenueStaffFromTo().andReturn().getResponse().getContentAsString();
                assertEquals("30", json);
            }
        }
        @Nested
        @DisplayName("Get revenue in a date range")
        class RevenueCustomerStaffFromToTest{
            ResultActions getRevenueCustomerStaffFromTo() throws Exception {
                return mockMvc.perform(get("/utilities/revenue/customer={cid}/staff={sid}/from={from}/to={to}"
                        ,3,2,"28-01-2019","28-01-2020"));
            }
            @BeforeEach
            void storeReturnEmptyList() throws Exception {
                given(store.revenueCustomerStaffFromTo(3,2,GlobalVar.dateFormatter.parse("28-01-2019"),
                        GlobalVar.dateFormatter.parse("28-01-2020"))).willReturn((long) 10);
            }
            @Test
            @DisplayName("Should return the HTTP status code 200")
            void shouldReturnHttpStatusCodeOk() throws Exception {
                getRevenueCustomerStaffFromTo().andExpect(status().isOk());
            }
            @Test
            @DisplayName("Should display revenue")
            void responseEmptyList() throws Exception {
                String json = getRevenueCustomerStaffFromTo().andReturn().getResponse().getContentAsString();
                assertEquals("10", json);
            }
        }
    }
    @Nested
    @DisplayName("warehouseDate test")
    class WarehouseTest {
        ResultActions getInventoriesInWarehouseByDatePage0() throws Exception {
            return mockMvc.perform(get("/utilities/warehouse/date={date}/p={page}","20-08-2019",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getInventoriesInWarehouseByDatePage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("Get inventories in warehouse")
        class WhenNoUtilitiesInDb{
            List<Inventory> inventories = MockData.inventories;
            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getInventoriesByDate(GlobalVar.dateFormatter.parse("20-08-2019"),0))
                        .willReturn(inventories);
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String actualJson = getInventoriesInWarehouseByDatePage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(inventories);

                assertEquals(expectedJson, actualJson);
            }
        }

    }
}