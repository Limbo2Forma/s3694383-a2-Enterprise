package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import config.GlobalVar;
import model.ReceivingNote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.ReceivingNoteStore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ReceivingNoteControllerTest {

    public ReceivingNoteStore store = mock(ReceivingNoteStore.class);
    public MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest() {
        ReceivingNoteController receivingNoteController = new ReceivingNoteController();
        receivingNoteController.setReceivingNoteStore(store);
        mockMvc = MockMvcBuilders.standaloneSetup(receivingNoteController).build();
    }

    public ReceivingNote receivingNote = MockData.receivingNote;
    public List<ReceivingNote> receivingNoteList = MockData.receivingNoteList;

    @Nested
    @DisplayName("getAllReceivingNote test")
    class GetAllReceivingNotesTest {
        ResultActions getAllPage0() throws Exception {
            return mockMvc.perform(get("/receivingNotes/p={page}",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getAllPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no ReceivingNote in database")
        class WhenNoReceivingNoteInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getAllReceivingNotes(0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getAllPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 ReceivingNotes in database")
        class WhenReceivingNotesInDb {
            @BeforeEach
            void storeReturnReceivingNoteList() {
                given(store.getAllReceivingNotes(0)).willReturn(receivingNoteList);
            }
            @Test
            @DisplayName("Should display 2 ReceivingNotes")
            void responseReceivingNoteList() throws Exception {
                String resultJson = getAllPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(receivingNoteList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getReceivingNoteById test")
    class GetReceivingNoteByIdTest {
        ResultActions getById1() throws Exception {
            return mockMvc.perform(get("/receivingNotes/{id}", 1));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getById1().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no ReceivingNote with id")
        class WhenNoReceivingNoteWithId{

            @BeforeEach
            void storeReturnNoReceivingNote() {
                given(store.getReceivingNoteById(1)).willReturn(null);
            }

            @Test
            @DisplayName("Should display zero todo items")
            void responseEmptyObject() throws Exception {
                String json = getById1().andReturn().getResponse().getContentAsString();
                assertEquals("", json);
            }
        }

        @Nested
        @DisplayName("When exist ReceivingNotes with id")
        class WhenReceivingNoteInDb {
            @BeforeEach
            void storeReturnReceivingNote() {
                given(store.getReceivingNoteById(1)).willReturn(receivingNote);
            }
            @Test
            @DisplayName("Should display ReceivingNote")
            void responseReceivingNote() throws Exception {
                String resultJson = getById1().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(receivingNote);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addReceivingNote test")
    class AddReceivingNoteTest {
        ResultActions addImportedOrder2() throws Exception {
            String requestJson = "{\n" +
                    "    \"date\": \"30-12-2019\",\n" +
                    "    \"staff\": {\n" +
                    "        \"id\": 3\n" +
                    "    }\n" +
                    "}\n";
            return mockMvc.perform(post("/receivingNotes/{id}",2).contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            addImportedOrder2().andExpect(status().isOk());
        }
        @Test
        @DisplayName("Should display newly added receivingNote Id, the value is always 0 due to the auto generated id")
        void responseReceivingNoteId() throws Exception {
            String json = addImportedOrder2().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateReceivingNote test")
    class UpdateReceivingNoteTest {
        ResultActions update() throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(receivingNote);

            return mockMvc.perform(put("/receivingNotes").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated receivingNote message")
        void responseUpdateMessage() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Receiving Note and its details with id: 1", json);
        }
    }
    @Nested
    @DisplayName("deleteReceivingNote test")
    class DeleteReceivingNoteTest {

        ResultActions deleteId3() throws Exception {
            return mockMvc.perform(delete("/receivingNotes/{receivingNoteId}", 3));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId3().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display deleted ReceivingNote message")
        void responseDeleteMessage() throws Exception {
            String json = deleteId3().andReturn().getResponse().getContentAsString();
            assertEquals("deleted Receiving Note and its details with id: 3", json);
        }
    }
    @Nested
    @DisplayName("getReceivingNoteByDate test")
    class GetReceivingNoteByDateTest {
        ResultActions getByDatePage0Date28022019() throws Exception {
            return mockMvc.perform(get("/receivingNotes/date={date}/p={page}","28-02-2019",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByDatePage0Date28022019().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no ReceivingNote in database")
        class WhenNoReceivingNoteInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getReceivingNotesByDate(GlobalVar.dateFormatter.parse("28-02-2019"), 0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByDatePage0Date28022019().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 ReceivingNote in database")
        class WhenReceivingNotesInDb {
            @BeforeEach
            void storeReturnReceivingNoteList() throws Exception{
                given(store.getReceivingNotesByDate(GlobalVar.dateFormatter.parse("28-02-2019"), 0)).willReturn(receivingNoteList);
            }
            @Test
            @DisplayName("Should display 2 receivingNotes")
            void responseReceivingNoteList() throws Exception {
                String resultJson = getByDatePage0Date28022019().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(receivingNoteList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getReceivingNoteFromTo test")
    class GetReceivingNoteFromToTest {
        ResultActions getByFrom28022019To28022020Page0() throws Exception {
            return mockMvc.perform(get("/receivingNotes/from={from}/to={to}/p={page}","28-02-2019", "28-02-2020",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByFrom28022019To28022020Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no ReceivingNote in database")
        class WhenNoReceivingNoteInDb{

            @BeforeEach
            void storeReturnEmptyList() throws Exception{
                given(store.getReceivingNotesFromTo(GlobalVar.dateFormatter.parse("28-02-2019"),
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
        @DisplayName("When ReceivingNotes in database")
        class WhenReceivingNotesInDb {
            @BeforeEach
            void storeReturnReceivingNoteList() throws Exception{
                given(store.getReceivingNotesFromTo(GlobalVar.dateFormatter.parse("28-02-2019"),
                        GlobalVar.dateFormatter.parse("28-02-2020"), 0)).willReturn(receivingNoteList);
            }
            @Test
            @DisplayName("Should display receivingNotes")
            void responseReceivingNoteList() throws Exception {
                String resultJson = getByFrom28022019To28022020Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(receivingNoteList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
}