package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitDto;
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
public class UnitControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private ProductDto productDto;

    @BeforeEach
    public void setup() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        ProductDto productDto = new ProductDto();

        productDto.setName("Twister Engine Oil A");

        String json = objectMapper.writeValueAsString(productDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        productDto.setId(id);
    }

    @Test
    void createUnit_shouldReturnNewUnit_status200() throws Exception {
        UnitDto unitDto = new UnitDto();

        unitDto.setName("1L");
        unitDto.setProductId(this.productDto.getId());
        unitDto.setPrice(100);
        unitDto.setQuantity(50);


        String json = objectMapper.writeValueAsString(unitDto);

        this.mockMvc.perform(
                        post("/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productId")
                                .value((unitDto.getProductId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((unitDto.getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.price")
                                .value((unitDto.getPrice()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.quantity")
                                .value((unitDto.getQuantity()))
                );
    }

    @Test
    void createUnit_DuplicateUnitException_status400() throws Exception {
        UnitDto unitDto = new UnitDto();

        unitDto.setName("1L");
        unitDto.setProductId(this.productDto.getId());
        unitDto.setPrice(100);
        unitDto.setQuantity(50);

        String json = objectMapper.writeValueAsString(unitDto);

        this.mockMvc.perform(
                post("/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        this.mockMvc.perform(
                        post("/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Unit with the same attributes already exist")
                );
    }

    @Test
    void updateProduct_ShouldUpdateProduct_status200() throws Exception {
        UnitDto unitDto = new UnitDto();

        unitDto.setName("1L");
        unitDto.setProductId(this.productDto.getId());
        unitDto.setPrice(100);
        unitDto.setQuantity(50);

        String json = objectMapper.writeValueAsString(unitDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        unitDto.setId(id);
        unitDto.setName("2L");
        unitDto.setProductId(this.productDto.getId());
        unitDto.setPrice(200);
        unitDto.setQuantity(25);

        json = objectMapper.writeValueAsString(productDto);

        this.mockMvc.perform(
                        post("/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productId")
                                .value((unitDto.getProductId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((unitDto.getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.price")
                                .value((unitDto.getPrice()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.quantity")
                                .value((unitDto.getQuantity()))
                );
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_status200() throws Exception {
        UnitDto unitDto = new UnitDto();

        unitDto.setName("1L");
        unitDto.setProductId(this.productDto.getId());
        unitDto.setPrice(100);
        unitDto.setQuantity(50);

        String json = objectMapper.writeValueAsString(unitDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        this.mockMvc.perform(
                        post("/units" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productId")
                                .value((unitDto.getProductId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((unitDto.getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.price")
                                .value((unitDto.getPrice()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.quantity")
                                .value((unitDto.getQuantity()))
                );
    }
}
