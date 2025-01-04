package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
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
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
public class UnitControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private JwtDecoder jwtDecoder;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    SubcategoryRequestDto subcategoryRequestDto;

    private ProductRequestDto productRequestDto;

    @BeforeEach
    public void setup() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Engine Oil");

        String json = objectMapper.writeValueAsString(categoryDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        categoryDto.setId(id);

        subcategoryRequestDto = new SubcategoryRequestDto();
        subcategoryRequestDto.setName("Passenger Engine Oil");
        subcategoryRequestDto.setCategoryId(categoryDto.getId());

        json = objectMapper.writeValueAsString(subcategoryRequestDto);

        mvcResult = this.mockMvc.perform(
                post("/subcategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        subcategoryRequestDto.setId(id);

        productRequestDto = new ProductRequestDto();

        productRequestDto.setSubcategoryId(subcategoryRequestDto.getId());
        productRequestDto.setName("Twister Engine Oil A");

        String productDtoJson = objectMapper.writeValueAsString(productRequestDto);

        mvcResult = this.mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson)
        ).andReturn();

        String productId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        productRequestDto.setId(productId);
    }

    @Test
    void createUnit_shouldReturnNewUnit_status200() throws Exception {
        UnitRequestDto unitRequestDto = new UnitRequestDto();

        unitRequestDto.setName("1L");
        unitRequestDto.setProductId(this.productRequestDto.getId());
        unitRequestDto.setPrice(100);
        unitRequestDto.setQuantity(50);


        String json = objectMapper.writeValueAsString(unitRequestDto);

        this.mockMvc.perform(
                        post("/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.product.id")
                                .value((unitRequestDto.getProductId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((unitRequestDto.getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.price")
                                .value((unitRequestDto.getPrice()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.quantity")
                                .value((unitRequestDto.getQuantity()))
                );

        this.mockMvc.perform(
                        get("/products/" + productRequestDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.unitCount")
                                .value(1)
                );
    }

    @Test
    void updateUnit_ShouldUpdateProduct_status200() throws Exception {
        UnitRequestDto unitRequestDto = new UnitRequestDto();

        unitRequestDto.setName("1L");
        unitRequestDto.setProductId(this.productRequestDto.getId());
        unitRequestDto.setPrice(100);
        unitRequestDto.setQuantity(50);

        String json = objectMapper.writeValueAsString(unitRequestDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        unitRequestDto.setId(id);
        unitRequestDto.setName("2L");
        unitRequestDto.setProductId(this.productRequestDto.getId());
        unitRequestDto.setPrice(200);
        unitRequestDto.setQuantity(25);

        json = objectMapper.writeValueAsString(unitRequestDto);

        this.mockMvc.perform(
                        put("/units/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.product.id")
                                .value((unitRequestDto.getProductId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((unitRequestDto.getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.price")
                                .value((unitRequestDto.getPrice()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.quantity")
                                .value((unitRequestDto.getQuantity()))
                );
    }

    @Test
    void deleteUnit_ShouldDeleteProduct_status200() throws Exception {
        UnitRequestDto unitRequestDto = new UnitRequestDto();

        unitRequestDto.setName("1L");
        unitRequestDto.setProductId(this.productRequestDto.getId());
        unitRequestDto.setPrice(100);
        unitRequestDto.setQuantity(50);

        String json = objectMapper.writeValueAsString(unitRequestDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        this.mockMvc.perform(
                        delete("/units/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.product.id")
                                .value((unitRequestDto.getProductId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((unitRequestDto.getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.price")
                                .value((unitRequestDto.getPrice()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.quantity")
                                .value((unitRequestDto.getQuantity()))
                );

        this.mockMvc.perform(
                        get("/products/" + productRequestDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.unitCount")
                                .value(0)
                );
    }
}
