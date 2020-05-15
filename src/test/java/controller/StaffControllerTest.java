package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Staff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.StaffStore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class StaffControllerTest {

    public StaffStore store = mock(StaffStore.class);
    public MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest() {
        StaffController staffController = new StaffController();
        staffController.setStaffStore(store);
        mockMvc = MockMvcBuilders.standaloneSetup(staffController).build();
    }   
    
    public Staff staff = MockData.staff;
    public List<Staff> staffList = MockData.staffList;

    @Nested
    @DisplayName("getAllStaff test")
    class GetAllStaffsTest {
        ResultActions getAllPage0() throws Exception {
            return mockMvc.perform(get("/staffs/p={page}",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getAllPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no staff in database")
        class WhenNoStaffInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getAllStaffs(0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getAllPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When staffs in database")
        class WhenStaffsInDb {
            @BeforeEach
            void storeReturn2Staffs() {
                given(store.getAllStaffs(0)).willReturn(staffList);
            }
            @Test
            @DisplayName("Should display staffs")
            void responseStaffsList() throws Exception {
                String resultJson = getAllPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(staffList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getStaffById test")
    class GetStaffByIdTest {
        ResultActions getById3() throws Exception {
            return mockMvc.perform(get("/staffs/{id}", 3));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getById3().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no staff with id")
        class WhenNoStaffWithId{

            @BeforeEach
            void storeReturnNoStaff() {
                given(store.getStaffById(3)).willReturn(null);
            }

            @Test
            @DisplayName("Should display zero todo items")
            void responseEmptyObject() throws Exception {
                String json = getById3().andReturn().getResponse().getContentAsString();
                assertEquals("", json);
            }
        }

        @Nested
        @DisplayName("When exist staffs with id")
        class WhenStaffInDb {
            @BeforeEach
            void storeReturnStaff() {
                given(store.getStaffById(3)).willReturn(staff);
            }
            @Test
            @DisplayName("Should display staff")
            void responseStaffObject() throws Exception {
                String resultJson = getById3().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(staff);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addStaff test")
    class AddStaffTest {
        ResultActions add() throws Exception {
            String requestJson = "{\n" +
                    "    \"name\": \"staff 2a22222\",\n" +
                    "    \"address\": \"address 1133\",\n" +
                    "    \"phone\": \"0912782223\",\n" +
                    "    \"email\": \"emai2132@email.com\"\n" +
                    "}\n";

            return mockMvc.perform(post("/staffs").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            add().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display newly added staff Id, the value is always 0 due to the auto generated id")
        void responseStaffObject() throws Exception {
            String json = add().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateStaff test")
    class UpdateStaffTest {
        ResultActions update() throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(staff);

            return mockMvc.perform(put("/staffs").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated staff message")
        void responseStaffObject() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Staff with id: 3", json);
        }
    }
    @Nested
    @DisplayName("deleteStaff test")
    class DeleteStaffTest {

        ResultActions deleteId3() throws Exception {
            return mockMvc.perform(delete("/staffs/{staffId}", 3));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId3().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated staff message")
        void responseStaffObject() throws Exception {
            String json = deleteId3().andReturn().getResponse().getContentAsString();
            assertEquals("deleted Staff with id: 3", json);
        }
    }
    @Nested
    @DisplayName("getStaffsByName test")
    class GetStaffsByNameTest {
        ResultActions getByNamePage0NameSta() throws Exception {
            return mockMvc.perform(get("/staffs/name={name}/p={page}","sta",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByNamePage0NameSta().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no staff in database")
        class WhenNoStaffInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getStaffByName("sta", 0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByNamePage0NameSta().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 3 staffs with name in database")
        class WhenStaffsInDb {
            @BeforeEach
            void storeReturnStaffs() {
                given(store.getStaffByName("sta",0)).willReturn(staffList);
            }
            @Test
            @DisplayName("Should display 3 staffs")
            void responseStaffs() throws Exception {
                String resultJson = getByNamePage0NameSta().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(staffList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getStaffsByPhone test")
    class GetStaffsByPhoneTest {
        ResultActions getByPhone012Page0() throws Exception {
            return mockMvc.perform(get("/staffs/phone={phone}/p={page}","012",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByPhone012Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no staff in database")
        class WhenNoStaffInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getStaffByPhone("012",0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByPhone012Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 3 staffs in database")
        class WhenStaffsInDb {
            @BeforeEach
            void storeReturnStaffs() {
                given(store.getStaffByPhone("012",0)).willReturn(staffList);
            }
            @Test
            @DisplayName("Should display 3 staffs")
            void responseStaffs() throws Exception {
                String resultJson = getByPhone012Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(staffList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getStaffsByAddress test")
    class GetStaffsByAddressTest {
        ResultActions getByAddressAddrPage0() throws Exception {
            return mockMvc.perform(get("/staffs/address={address}/p={page}","addr",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByAddressAddrPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no staff in database")
        class WhenNoStaffInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getStaffByAddress("addr",0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByAddressAddrPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When staffs in database")
        class WhenStaffsInDb {
            @BeforeEach
            void storeReturn2Staffs() {
                given(store.getStaffByAddress("addr",0)).willReturn(staffList);
            }
            @Test
            @DisplayName("Should display staffs")
            void response2Staffs() throws Exception {
                String resultJson = getByAddressAddrPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(staffList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
}