package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.ProviderStore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ProviderControllerTest {

    public ProviderStore store = mock(ProviderStore.class);
    public MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest() {
        ProviderController providerController = new ProviderController();
        providerController.setProviderStore(store);
        mockMvc = MockMvcBuilders.standaloneSetup(providerController).build();
    }
    public Provider provider = MockData.provider;
    public List<Provider> providerList = MockData.providerList;

    @Nested
    @DisplayName("getAllProvider test")
    class GetAllProvidersTest {
        ResultActions getAllPage0() throws Exception {
            return mockMvc.perform(get("/providers/p={page}",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getAllPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no provider in database")
        class WhenNoProviderInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getAllProviders(0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getAllPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 providers in database")
        class WhenProvidersInDb {
            @BeforeEach
            void storeReturnProviderList() {
                given(store.getAllProviders(0)).willReturn(providerList);
            }
            @Test
            @DisplayName("Should display 2 providers")
            void responseProviderList() throws Exception {
                String resultJson = getAllPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(providerList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getProviderById test")
    class GetProviderByIdTest {
        ResultActions getById2() throws Exception {
            return mockMvc.perform(get("/providers/{id}", 2));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getById2().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no provider with id")
        class WhenNoProviderWithId{

            @BeforeEach
            void storeReturnNoProvider() {
                given(store.getProviderById(2)).willReturn(null);
            }

            @Test
            @DisplayName("Should display zero todo items")
            void responseEmptyObject() throws Exception {
                String json = getById2().andReturn().getResponse().getContentAsString();
                assertEquals("", json);
            }
        }

        @Nested
        @DisplayName("When exist providers with id")
        class WhenProviderInDb {
            @BeforeEach
            void storeReturnProvider() {
                given(store.getProviderById(2)).willReturn(provider);
            }
            @Test
            @DisplayName("Should display provider")
            void responseProviderObject() throws Exception {
                String resultJson = getById2().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(provider);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addProvider test")
    class AddProviderTest {

        ResultActions add() throws Exception {

            String requestJson = "{\n" +
                    "    \"name\": \"provider 3\",\n" +
                    "    \"address\": \"address 3333\",\n" +
                    "    \"phone\": \"0912782223\",\n" +
                    "    \"fax\": \"fax 333a\",\n" +
                    "    \"email\": \"emai333332@email.com\",\n" +
                    "    \"contact\": \"contact me pls\"\n" +
                    "}\n";

            return mockMvc.perform(post("/providers").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            add().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display newly added provider Id, the value is always 0 due to the auto generated id")
        void responseProviderObject() throws Exception {
            String json = add().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateProvider test")
    class UpdateProviderTest {
        ResultActions update() throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(provider);

            return mockMvc.perform(put("/providers").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated provider message")
        void responseProviderObject() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Provider with id: 2", json);
        }
    }
    @Nested
    @DisplayName("deleteProvider test")
    class DeleteProviderTest {

        ResultActions deleteId1() throws Exception {
            return mockMvc.perform(delete("/providers/{providerId}", 1));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId1().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated provider message")
        void responseProviderObject() throws Exception {
            String json = deleteId1().andReturn().getResponse().getContentAsString();
            assertEquals("deleted Provider with id: 1", json);
        }
    }
    @Nested
    @DisplayName("getProvidersByName test")
    class GetProvidersByNameTest {
        ResultActions getByNamePage0Name1() throws Exception {
            return mockMvc.perform(get("/providers/name={name}/p={page}","1",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByNamePage0Name1().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no provider in database")
        class WhenNoProviderInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getProviderByName("1", 0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByNamePage0Name1().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 providers in database")
        class WhenProvidersInDb {
            @BeforeEach
            void storeReturnProviderList() {
                given(store.getProviderByName("1",0)).willReturn(providerList);
            }
            @Test
            @DisplayName("Should display 2 providers")
            void responseProviderList() throws Exception {
                String resultJson = getByNamePage0Name1().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(providerList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getProvidersByPhone test")
    class GetProvidersByPhoneTest {
        ResultActions getByPhone012Page0() throws Exception {
            return mockMvc.perform(get("/providers/phone={phone}/p={page}","012",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByPhone012Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no provider in database")
        class WhenNoProviderInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getProviderByPhone("012",0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByPhone012Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 providers in database")
        class WhenProvidersInDb {
            @BeforeEach
            void storeReturnProviderList() {
                given(store.getProviderByPhone("012",0)).willReturn(providerList);
            }
            @Test
            @DisplayName("Should display 2 providers")
            void responseProviderList() throws Exception {
                String resultJson = getByPhone012Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(providerList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getProvidersByAddress test")
    class GetProvidersByAddressTest {
        ResultActions getByAddressAddrPage0() throws Exception {
            return mockMvc.perform(get("/providers/address={address}/p={page}","addr",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByAddressAddrPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no provider in database")
        class WhenNoProviderInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getProviderByAddress("addr",0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByAddressAddrPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When 2 providers in database")
        class WhenProvidersInDb {
            @BeforeEach
            void storeReturnProviderList() {
                given(store.getProviderByAddress("addr",0)).willReturn(providerList);
            }
            @Test
            @DisplayName("Should display 2 providers")
            void responseProviderList() throws Exception {
                String resultJson = getByAddressAddrPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(providerList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
}