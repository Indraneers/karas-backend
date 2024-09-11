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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SaleControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    List<ProductDto> productDtos;

    List<UnitDto> unitDtos;

    public void setupProducts() throws Exception {
        for (ProductDto productDto : productDtos) {
            System.out.println(productDto.getName());
            String json = objectMapper.writeValueAsString(productDto);

            MvcResult mvcResult = this.mockMvc.perform(
                    post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            ).andReturn();

            String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

            productDto.setId(id);
        }
    }

    public void setupProductsWithUnits(List<UnitDto> mockedUnitDtos) throws Exception {
        for (ProductDto productDto : productDtos) {
            for (UnitDto unitDto : mockedUnitDtos) {
                unitDto.setProductId(productDto.getId());
                String json = objectMapper.writeValueAsString(unitDto);

                MvcResult mvcResult = this.mockMvc.perform(
                        post("/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andReturn();

                String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

                UnitDto addedUnitDto = new UnitDto();

                addedUnitDto.setId(id);
                addedUnitDto.setName(unitDto.getName());
                addedUnitDto.setPrice(unitDto.getPrice());
                addedUnitDto.setProductId(unitDto.getProductId());
                addedUnitDto.setQuantity(unitDto.getQuantity());

                unitDtos.add(addedUnitDto);
            }
        }
    }

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

        String categoryId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        this.productDtos = new ArrayList<>();

        ProductDto productDtoOne = new ProductDto();
        productDtoOne.setCategoryId(categoryId);
        productDtoOne.setName("Engine Oil A");

        productDtos.add(productDtoOne);

        ProductDto productDtoTwo = new ProductDto();
        productDtoTwo.setCategoryId(categoryId);
        productDtoTwo.setName("Engine Oil B");

        productDtos.add(productDtoTwo);

        this.setupProducts();

        UnitDto unitDtoOne = new UnitDto();
        unitDtoOne.setName("1L");
        unitDtoOne.setPrice(500);
        unitDtoOne.setQuantity(100);

        UnitDto unitDtoTwo = new UnitDto();
        unitDtoTwo.setName("2L");
        unitDtoTwo.setPrice(1000);
        unitDtoTwo.setQuantity(100);

        List<UnitDto> unitDtoMocks = new ArrayList<>();

        unitDtoMocks.add(unitDtoOne);
        unitDtoMocks.add(unitDtoTwo);

        this.unitDtos = new ArrayList<>();
        this.setupProductsWithUnits(unitDtoMocks);
    }

    @Test
    public void test() {}
}
