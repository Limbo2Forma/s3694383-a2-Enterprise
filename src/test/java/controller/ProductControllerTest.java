package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Product;
import model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.ProductStore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ProductControllerTest {

    public ProductStore store = mock(ProductStore.class);
    public MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest() {
        ProductController productController = new ProductController();
        productController.setProductStore(store);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    public ProductCategory category = MockData.category;
    public List<ProductCategory> categoryList = MockData.categoryList;
    public Product product = MockData.product;
    public List<Product> productList = MockData.productList;

    @Nested
    @DisplayName("getAllProduct test")
    class GetAllProductsTest {
        ResultActions getAllPage0() throws Exception {
            return mockMvc.perform(get("/products/p={page}",0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getAllPage0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no product in database")
        class WhenNoProductInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getAllProducts(0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getAllPage0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When products in database")
        class WhenProductsInDb {
            @BeforeEach
            void storeReturn2Products() {
                given(store.getAllProducts(0)).willReturn(productList);
            }
            @Test
            @DisplayName("Should display products")
            void responseProductsList() throws Exception {
                String resultJson = getAllPage0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(productList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getProductById test")
    class GetProductByIdTest {
        ResultActions getById2() throws Exception {
            return mockMvc.perform(get("/products/{id}", 2));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getById2().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no product with id")
        class WhenNoProductWithId{

            @BeforeEach
            void storeReturnNoProduct() {
                given(store.getProductById(2)).willReturn(null);
            }

            @Test
            @DisplayName("Should display zero todo items")
            void responseEmptyObject() throws Exception {
                String json = getById2().andReturn().getResponse().getContentAsString();
                assertEquals("", json);
            }
        }

        @Nested
        @DisplayName("When exist products with id")
        class WhenProductInDb {
            @BeforeEach
            void storeReturnProduct() {
                given(store.getProductById(2)).willReturn(product);
            }
            @Test
            @DisplayName("Should display product")
            void responseProductObject() throws Exception {
                String resultJson = getById2().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(product);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addProduct test")
    class AddProductTest {
        ResultActions add() throws Exception {
            String requestJson = "{\n" +
                    "    \"name\": \"name32\",\n" +
                    "    \"model\": \"model32\",\n" +
                    "    \"brand\": \"brand32\",\n" +
                    "    \"company\": \"company32\",\n" +
                    "    \"description\": \"desc32\",\n" +
                    "    \"productCategory\": {\n" +
                    "        \"id\": 2\n" +
                    "    },\n" +
                    "    \"singlePrice\": 10\n" +
                    "}\n";

            return mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            add().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display newly added product Id, the value is always 0 due to the auto generated id")
        void responseProductObject() throws Exception {
            String json = add().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateProduct test")
    class UpdateProductTest {
        ResultActions update() throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(product);

            return mockMvc.perform(put("/products").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated product message")
        void responseProductObject() throws Exception {
            String json = update().andReturn().getResponse().getContentAsString();
            assertEquals("updated Product with id: 2", json);
        }
    }
    @Nested
    @DisplayName("deleteProduct test")
    class DeleteProductTest {

        ResultActions deleteId3() throws Exception {
            return mockMvc.perform(delete("/products/{productId}", 1));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId3().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated product message")
        void responseProductObject() throws Exception {
            String json = deleteId3().andReturn().getResponse().getContentAsString();
            assertEquals("deleted Product with id: 1", json);
        }
    }
    @Nested
    @DisplayName("getProductByName test")
    class GetProductsByName {
        ResultActions getByName45Page0() throws Exception {
            return mockMvc.perform(get("/products/name={pr}/p={page}","45", 0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByName45Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no product in database")
        class WhenNoProductInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getProductByName("45",0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByName45Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When products in database")
        class WhenProductsInDb {
            @BeforeEach
            void storeReturnProducts() {
                given(store.getProductByName("45",0)).willReturn(productList);
            }
            @Test
            @DisplayName("Should display products")
            void responseProductsList() throws Exception {
                String resultJson = getByName45Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(productList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getProductByCategory test")
    class GetProductByCategory {
        ResultActions getByCategory1Page0() throws Exception {
            return mockMvc.perform(get("/products/category={id}/p={page}",1, 0));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getByCategory1Page0().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no product in database")
        class WhenNoProductInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getProductByCategory(1,0)).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getByCategory1Page0().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When products in database")
        class WhenProductsInDb {
            @BeforeEach
            void storeReturnProducts() {
                given(store.getProductByCategory(1,0)).willReturn(productList);
            }
            @Test
            @DisplayName("Should display products")
            void responseProductsList() throws Exception {
                String resultJson = getByCategory1Page0().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(productList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("getAllCategories test")
    class GetAllCategoriesTest {
        ResultActions getAll() throws Exception {
            return mockMvc.perform(get("/products/categories"));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            getAll().andExpect(status().isOk());
        }

        @Nested
        @DisplayName("When no category in database")
        class WhenNoProductInDb{

            @BeforeEach
            void storeReturnEmptyList() {
                given(store.getProductCategories()).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("Should display empty array")
            void responseEmptyList() throws Exception {
                String json = getAll().andReturn().getResponse().getContentAsString();
                assertEquals("[]", json);
            }
        }

        @Nested
        @DisplayName("When products in database")
        class WhenProductsInDb {
            @BeforeEach
            void storeReturnProducts() {
                given(store.getProductCategories()).willReturn(categoryList);
            }
            @Test
            @DisplayName("Should display products")
            void responseProductsList() throws Exception {
                String resultJson = getAll().andReturn().getResponse().getContentAsString();

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                String expectedJson = mapper.writer().writeValueAsString(categoryList);

                assertEquals(expectedJson, resultJson);
            }
        }
    }
    @Nested
    @DisplayName("addProductCategory test")
    class AddCategoryTest {
        ResultActions add() throws Exception {
            String requestJson = "{\n" +
                    "    \"name\": \"category 3\"\n" +
                    "}\n";

            return mockMvc.perform(post("/products/categories").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            add().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display newly added product Id, the value is always 0 due to the auto generated id")
        void responseProductObject() throws Exception {
            String json = add().andReturn().getResponse().getContentAsString();
            assertEquals("0", json);
        }
    }
    @Nested
    @DisplayName("updateProductCategory test")
    class UpdateCategoryTest {
        ResultActions update(ProductCategory c) throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(c);

            return mockMvc.perform(put("/products/categories").contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            update(category).andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated product message")
        void responseProductObject() throws Exception {
            String json = update(category).andReturn().getResponse().getContentAsString();
            assertEquals("updated Category with id: 1", json);
        }
    }
    @Nested
    @DisplayName("deleteProductCategory test")
    class DeleteCategoryTest {

        ResultActions deleteId2() throws Exception {
            return mockMvc.perform(delete("/products/categories/{id}", 2));
        }

        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            deleteId2().andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should display updated product message")
        void responseProductObject() throws Exception {
            String json = deleteId2().andReturn().getResponse().getContentAsString();
            assertEquals("deleted Category with id: 2", json);
        }
    }
}