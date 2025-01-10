package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.UserRole;
import com.twistercambodia.karasbackend.autoService.dto.AutoServiceDto;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.inventory.dto.*;
import com.twistercambodia.karasbackend.inventory.enums.StockUpdate;
import com.twistercambodia.karasbackend.storage.config.MinioConfig;
import com.twistercambodia.karasbackend.storage.service.StorageService;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
public class RestockUnitIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private MinioConfig minioConfig; // Mock the MinIO configuration bean.

    @MockBean
    private StorageService storageService; // Mock the StorageService.

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    List<UnitRequestDto> unitRequestDtos = new ArrayList<>();

    SubcategoryRequestDto subcategoryRequestDto = new SubcategoryRequestDto();

    ProductRequestDto productRequestDto;

    UserDto userDto;

    @BeforeAll
    public static void init() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8083")
                .start();
    }

    public void setupProducts(ProductRequestDto requestProductRequestDto) throws Exception {
        System.out.println(requestProductRequestDto.getName());
        String json = objectMapper.writeValueAsString(requestProductRequestDto);
        MockMultipartFile file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );

        MvcResult mvcResult = this.mockMvc.perform(
                multipart("/products")
                        .file(file)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        requestProductRequestDto.setId(id);

        productRequestDto = requestProductRequestDto;
    }

    public void setupObjectMapper() {
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.findAndRegisterModules();
    }

    public void setupProductsWithUnits(List<UnitRequestDto> mockedUnitRequestDtos) throws Exception {
        for (UnitRequestDto unitRequestDto : mockedUnitRequestDtos) {
            unitRequestDto.setProductId(productRequestDto.getId());
            String json = objectMapper.writeValueAsString(unitRequestDto);

            MvcResult mvcResult = this.mockMvc.perform(
                    post("/units")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            ).andReturn();

            String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

            UnitRequestDto addedUnitRequestDto = new UnitRequestDto();

            addedUnitRequestDto.setId(id);
            addedUnitRequestDto.setName(unitRequestDto.getName());
            addedUnitRequestDto.setPrice(unitRequestDto.getPrice());
            addedUnitRequestDto.setProductId(unitRequestDto.getProductId());
            addedUnitRequestDto.setToBaseUnit(unitRequestDto.getToBaseUnit());
            addedUnitRequestDto.setQuantity(unitRequestDto.getQuantity());
            unitRequestDtos.add(addedUnitRequestDto);
        }
    }

    @BeforeEach
    public void setup() throws Exception {
        this.setupObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Engine Oil");

        String json = objectMapper.writeValueAsString(categoryDto);
        MockMultipartFile file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );

        MvcResult mvcResult = this.mockMvc.perform(
                multipart("/categories")
                        .file(file)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        categoryDto.setId(id);

        subcategoryRequestDto = new SubcategoryRequestDto();
        subcategoryRequestDto.setName("Passenger Engine Oil");
        subcategoryRequestDto.setCategoryId(categoryDto.getId());

        json = objectMapper.writeValueAsString(subcategoryRequestDto);
        file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );

        mvcResult = this.mockMvc.perform(
                multipart("/subcategories")
                        .file(file)
        ).andReturn();

        id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        subcategoryRequestDto.setId(id);

        // create product
        productRequestDto = new ProductRequestDto();
        productRequestDto.setSubcategoryId(subcategoryRequestDto.getId());
        productRequestDto.setName("Engine Oil A");
        productRequestDto.setVariable(true);
        productRequestDto.setBaseUnit("1L");

        this.setupProducts(productRequestDto);

        // create units
        UnitRequestDto unitRequestDtoOne = new UnitRequestDto();
        unitRequestDtoOne.setName("1L");
        // 500
        unitRequestDtoOne.setPrice(500);
        unitRequestDtoOne.setToBaseUnit(1);

        UnitRequestDto unitRequestDtoTwo = new UnitRequestDto();
        unitRequestDtoTwo.setName("2L");
        unitRequestDtoTwo.setPrice(200);
        unitRequestDtoTwo.setToBaseUnit(2);

        List<UnitRequestDto> unitRequestDtoMocks = new ArrayList<>();

        unitRequestDtoMocks.add(unitRequestDtoOne);
        unitRequestDtoMocks.add(unitRequestDtoTwo);

        this.setupProductsWithUnits(unitRequestDtoMocks);

        // Create User
        this.userDto = new UserDto();

        userDto.setUsername("Service Person A");
        userDto.setRole(UserRole.ADMIN);

        json = objectMapper.writeValueAsString(userDto);

        mvcResult = this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        userDto.setId(id);
    }

    @Test
    public void createRestock_ShouldRestockStock_status200() throws Exception {
        RestockRequestDto restockRequestDto = new RestockRequestDto();

        restockRequestDto.setCreatedAt(LocalDateTime.now().toString());
        restockRequestDto.setUserId(userDto.getId());

        RestockItemRequestDto itemOne = new RestockItemRequestDto();
        itemOne.setQuantity(1000);
        itemOne.setStatus(StockUpdate.RESTOCK);

        RestockItemRequestDto itemTwo = new RestockItemRequestDto();
        itemTwo.setQuantity(2000);
        itemOne.setStatus(StockUpdate.RESTOCK);

        List<RestockItemRequestDto> items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);

        restockRequestDto.setItems(items);

        String json = objectMapper.writeValueAsString(restockRequestDto);
        this.mockMvc.perform(
                post("/restock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(itemOne.getQuantity())
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(itemTwo.getQuantity())
        );
    }

    @Test
    public void createRestock_ShouldDeductStock_status200() throws Exception {
        RestockRequestDto restockRequestDto = new RestockRequestDto();

        restockRequestDto.setCreatedAt(LocalDateTime.now().toString());
        restockRequestDto.setUserId(userDto.getId());

        RestockItemRequestDto itemOne = new RestockItemRequestDto();
        itemOne.setQuantity(1000);
        itemOne.setStatus(StockUpdate.RESTOCK);

        RestockItemRequestDto itemTwo = new RestockItemRequestDto();
        itemTwo.setQuantity(2000);
        itemOne.setStatus(StockUpdate.RESTOCK);

        List<RestockItemRequestDto> items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);

        restockRequestDto.setItems(items);

        String json = objectMapper.writeValueAsString(restockRequestDto);
        this.mockMvc.perform(
                post("/restock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        itemOne.setQuantity(250);
        itemOne.setStatus(StockUpdate.DEDUCT);
        itemTwo.setQuantity(500);
        itemOne.setStatus(StockUpdate.DEDUCT);

        items.clear();

        items.add(itemOne);
        items.add(itemTwo);

        restockRequestDto.setItems(items);

        json = objectMapper.writeValueAsString(restockRequestDto);
        this.mockMvc.perform(
                post("/restock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(750)
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(1500)
        );
    }

    @Test
    public void createRestock_CompositeStatusSuccess_status200() throws Exception {
        RestockRequestDto restockRequestDto = new RestockRequestDto();

        restockRequestDto.setCreatedAt(LocalDateTime.now().toString());
        restockRequestDto.setUserId(userDto.getId());

        RestockItemRequestDto itemOne = new RestockItemRequestDto();
        itemOne.setQuantity(1000);
        itemOne.setStatus(StockUpdate.RESTOCK);

        RestockItemRequestDto itemTwo = new RestockItemRequestDto();
        itemTwo.setQuantity(2000);
        itemOne.setStatus(StockUpdate.RESTOCK);

        List<RestockItemRequestDto> items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);

        restockRequestDto.setItems(items);

        String json = objectMapper.writeValueAsString(restockRequestDto);
        this.mockMvc.perform(
                post("/restock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        itemOne.setQuantity(250);
        itemOne.setStatus(StockUpdate.RESTOCK);
        itemTwo.setQuantity(500);
        itemOne.setStatus(StockUpdate.DEDUCT);

        items.clear();

        items.add(itemOne);
        items.add(itemTwo);

        restockRequestDto.setItems(items);

        json = objectMapper.writeValueAsString(restockRequestDto);
        this.mockMvc.perform(
                post("/restock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(1250)
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(1500)
        );
    }
}
