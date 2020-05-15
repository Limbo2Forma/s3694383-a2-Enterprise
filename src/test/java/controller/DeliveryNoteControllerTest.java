package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import config.GlobalVar;
import model.DeliveryNote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.DeliveryNoteStore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class DeliveryNoteControllerTest {

    public DeliveryNoteStore store = mock(DeliveryNoteStore.class);
    public MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest() {
        DeliveryNoteController deliveryNoteController = new DeliveryNoteController();
        deliveryNoteController.setDeliveryNoteStore(store);
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryNoteController).build();
    }

    public DeliveryNote deliveryNote = MockData.deliveryNote;
    public List<DeliveryNote> deliveryNoteList = MockData.deliveryNoteList;

    @Nested
    @DisplayName("getAllDeliveryNote test")
    class GetAllDeliveryNotesTest {
        ResultActions getAllPage0() throws Exception {
            return mockMvc.perform(get("/deliveryNotes/p={page}",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getAllPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no DeliveryNote in database")
        class WhenNoDeliveryNoteInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getAllDeliveryNotes(0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getAllPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 DeliveryNotes in database")
        class WhenDeliveryNotesInDb {
            @BeforeEach
            void storeReturnDeliveryNoteList() {
                given(store.getAllDeliveryNotes(0)).willReturn(deliveryNoteList);
            }
            @Test
            @DisplayName("Should display 2 DeliveryNotes")
            void responseDeliveryNoteList() throws Exception {
                String resultJson = getAllPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(deliveryNoteList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getDeliveryNoteById test")
    class GetDeliveryNoteByIdTest {
        ResultActions getById1() throws Exception {
            return mockMvc.perform(get("/deliveryNotes/{id}", 1));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getById1().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no DeliveryNote with id")
        class WhenNoDeliveryNoteWithId{

            @BeforeEach
            void storeReturnNoDeliveryNote() {
                given(store.getDeliveryNoteById(1)).willReturn(null);
            }

            @Test
            @DisplayName("Should display zero todo items")
            void responseEmptyObject() throws Exception {
                String json = getById1().andReturn().getResponse().getContentAsString();
                assertEquals("", json);
            }
        }

        @Nested
        @DisplayName("When exist DeliveryNotes with id")
        class WhenDeliveryNoteInDb {
            @BeforeEach
            void storeReturnDeliveryNote() {
                given(store.getDeliveryNoteById(1)).willReturn(deliveryNote);
            }
            @Test
            @DisplayName("Should display DeliveryNote")
            void responseDeliveryNote() throws Exception {
                String resultJson = getById1().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(deliveryNote);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addDeliveryNote test")
    class AddDeliveryNoteTest {
        ResultActions add() throws Exception {
            String requestJson = "{\n" +
                    "    \"date\": \"28-09-2019\",\n" +
                    "    \"staff\": {\"id\": 3},\n" +
                    "    \"provider\": {\"id\": 2},\n" +
                    "    \"deliveryNoteDetails\": [\n" +
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
            return mockMvc.perform(post("/deliveryNotes").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            add().andExpect(status().isOk());
        }
        @Test
        @DisplayName("Should display newly added deliveryNote Id, the value is always 0 due to the auto generated id")
        void responseDeliveryNoteId() throws Exception {
            String json = add().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateDeliveryNote test")
    class UpdateDeliveryNoteTest {
        ResultActions update() throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(deliveryNote);

            return mockMvc.perform(put("/deliveryNotes").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated deliveryNote message")
        void responseUpdateMessage() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Delivery Note and its details with id: 1", json);
        }
    }
    @Nested
    @DisplayName("deleteDeliveryNote test")
    class DeleteDeliveryNoteTest {

        ResultActions deleteId3() throws Exception {
            return mockMvc.perform(delete("/deliveryNotes/{deliveryNoteId}", 3));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId3().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display deleted DeliveryNote message")
        void responseDeleteMessage() throws Exception {
            String json = deleteId3().andReturn().getResponse().getContentAsString();
            assertEquals("deleted Delivery Note and its details with id: 3", json);
        }
    }
    @Nested
    @DisplayName("getDeliveryNoteByDate test")
    class GetDeliveryNoteByDateTest {
        ResultActions getByDatePage0Date28022019() throws Exception {
            return mockMvc.perform(get("/deliveryNotes/date={date}/p={page}","28-02-2019",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByDatePage0Date28022019().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no DeliveryNote in database")
        class WhenNoDeliveryNoteInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getDeliveryNotesByDate(GlobalVar.dateFormatter.parse("28-02-2019"), 0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByDatePage0Date28022019().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 DeliveryNote in database")
        class WhenDeliveryNotesInDb {
            @BeforeEach
            void storeReturnDeliveryNoteList() throws Exception{
                given(store.getDeliveryNotesByDate(GlobalVar.dateFormatter.parse("28-02-2019"), 0)).willReturn(deliveryNoteList);
            }
            @Test
            @DisplayName("Should display 2 deliveryNotes")
            void responseDeliveryNoteList() throws Exception {
                String resultJson = getByDatePage0Date28022019().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(deliveryNoteList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getDeliveryNoteFromTo test")
    class GetDeliveryNoteFromToTest {
        ResultActions getByFrom28022019To28022020Page0() throws Exception {
            return mockMvc.perform(get("/deliveryNotes/from={from}/to={to}/p={page}","28-02-2019", "28-02-2020",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByFrom28022019To28022020Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no DeliveryNote in database")
        class WhenNoDeliveryNoteInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getDeliveryNotesFromTo(GlobalVar.dateFormatter.parse("28-02-2019"),
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
        @DisplayName("When DeliveryNotes in database")
        class WhenDeliveryNotesInDb {
            @BeforeEach
            void storeReturnDeliveryNoteList() throws Exception{
                given(store.getDeliveryNotesFromTo(GlobalVar.dateFormatter.parse("28-02-2019"),
                        GlobalVar.dateFormatter.parse("28-02-2020"), 0)).willReturn(deliveryNoteList);
            }
            @Test
            @DisplayName("Should display deliveryNotes")
            void responseDeliveryNoteList() throws Exception {
                String resultJson = getByFrom28022019To28022020Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(deliveryNoteList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getDeliveryNoteDetails test")
    class GetDeliveryNoteDetailsFromId {
        ResultActions getDetailsById() throws Exception {
            return mockMvc.perform(get("/deliveryNotes/detail/{id}",1));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getDetailsById().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no DeliveryNote Detail in database")
        class WhenNoDeliveryNoteDetailsInDb{

            @BeforeEach
            void storeReturnEmptyList(){
                given(store.getDeliveryNoteDetailByDeliveryNoteId(1)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getDetailsById().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When DeliveryNotes in database")
        class WhenDeliveryNoteDetailsInDb {
            @BeforeEach
            void storeReturnDeliveryNoteList(){
                given(store.getDeliveryNoteDetailByDeliveryNoteId(1)).willReturn(deliveryNote.getDeliveryNoteDetails());
            }
            @Test
            @DisplayName("Should display deliveryNotes")
            void responseDeliveryNoteList() throws Exception {
                String resultJson = getDetailsById().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(deliveryNote.getDeliveryNoteDetails());

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addDeliveryNoteDetail test")
    class AddDeliveryNoteDetailTest {
        ResultActions add() throws Exception {
            String requestJson = "{\n" +
                    "    \"product\": {\n" +
                    "        \"id\": 3\n" +
                    "    },\n" +
                    "    \"deliveryNote\": {\n" +
                    "        \"id\": 1\n" +
                    "    },\n" +
                    "    \"price\": 500,\n" +
                    "    \"quantity\": 1\n" +
                    "}\n";
            return mockMvc.perform(post("/deliveryNotes/detail").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            add().andExpect(status().isOk());
        }
        @Test
        @DisplayName("Should display newly added deliveryNote Id, the value is always 0 due to the auto generated id")
        void responseDeliveryNoteId() throws Exception {
            String json = add().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateDeliveryNoteDetail test")
    class UpdateDeliveryNoteDetailTest {
        ResultActions update() throws Exception {
            String requestJson = "{\"id\":2, \"product\":{\"id\":1},\"deliveryNote\":{\"id\":2}, \"quantity\":100}";
            return mockMvc.perform(put("/deliveryNotes/detail").contentType(MediaType.APPLICATION_JSON).content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated deliveryNote message")
        void responseUpdateMessage() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Delivery Note Detail with id: 2 from Delivery Note with id: 2", json);
        }
    }
    @Nested
    @DisplayName("deleteDeliveryNote test")
    class DeleteDeliveryNoteDetailTest {

        ResultActions deleteId3() throws Exception {
            return mockMvc.perform(delete("/deliveryNotes/detail/{id}", 3));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId3().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display deleted DeliveryNote Detail message")
        void responseDeleteMessage() throws Exception {
            String json = deleteId3().andReturn().getResponse().getContentAsString();
            assertEquals("deleted Delivery Note Detail with id: 3 from Delivery Note with id: 0", json);
        }
    }
}