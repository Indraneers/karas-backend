package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private CategoryDto categoryDto;

    @BeforeEach
    public void setup() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        categoryDto = new CategoryDto();
        categoryDto.setName("Engine Oil");

        String json = objectMapper.writeValueAsString(categoryDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        categoryDto.setId(id);
    }

    @Test
    void createProduct_shouldReturnNewProduct_status200() throws Exception {
        ProductDto productDto = new ProductDto();

        productDto.setName("Twister Engine Oil A");
        productDto.setCategoryId(this.categoryDto.getId());

        String json = objectMapper.writeValueAsString(productDto);

        this.mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.categoryId")
                                .value((productDto.getCategoryId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((productDto.getName()))
                );

        this.mockMvc.perform(
                get("/categories/" + categoryDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productCount")
                                .value(1)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.products[0].name")
                                .value(productDto.getName())
                );
    }

    @Test
    void createProduct_DuplicateProductException_status400() throws Exception {
        ProductDto productDto = new ProductDto();

        productDto.setName("Twister Engine Oil A");
        productDto.setCategoryId(this.categoryDto.getId());

        String json = objectMapper.writeValueAsString(productDto);

        this.mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        this.mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Product with the same attributes already exist")
                );
    }

    @Test
    void updateProduct_ShouldUpdateProduct_status200() throws Exception {
        ProductDto productDto = new ProductDto();

        productDto.setName("Twister Engine Oil A");
        productDto.setCategoryId(this.categoryDto.getId());

        String json = objectMapper.writeValueAsString(productDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        productDto.setId(id);
        productDto.setName("Twister Engine Oil B");

        json = objectMapper.writeValueAsString(productDto);

        this.mockMvc.perform(
                        put("/products/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.categoryId")
                                .value((productDto.getCategoryId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((productDto.getName()))
                );
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_status200() throws Exception {
        ProductDto productDto = new ProductDto();

        productDto.setName("Twister Engine Oil A");
        productDto.setCategoryId(this.categoryDto.getId());

        String json = objectMapper.writeValueAsString(productDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        this.mockMvc.perform(
                        delete("/products/" + id)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.categoryId")
                                .value((productDto.getCategoryId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((productDto.getName()))
                );

        this.mockMvc.perform(
                        get("/categories/" + categoryDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productCount")
                                .value(0)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.products")
                                .isEmpty()
                );
    }
}
