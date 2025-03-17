package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.UserRole;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitRequestDto;
import com.twistercambodia.karasbackend.sale.dto.ItemRequestDto;
import com.twistercambodia.karasbackend.sale.dto.SaleRequestDto;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;
import com.twistercambodia.karasbackend.storage.config.MinioConfig;
import com.twistercambodia.karasbackend.storage.service.StorageService;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
@TestPropertySource(properties = "server.port=0")
@TestPropertySource(locations="classpath:application.properties")
public class SaleInventoryIntegrationTest {
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

    CustomerDto customerDto;

    VehicleDto vehicleDto;

    UserDto userDto;

    @BeforeAll
    public static void init() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8085")
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
        unitRequestDtoOne.setQuantity(100);
        unitRequestDtoOne.setToBaseUnit(1);

        UnitRequestDto unitRequestDtoTwo = new UnitRequestDto();
        unitRequestDtoTwo.setName("2L");
        unitRequestDtoTwo.setPrice(200);
        unitRequestDtoTwo.setQuantity(50);
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

        // Create Customer
        customerDto = new CustomerDto();

        customerDto.setName("Car Person A");
        customerDto.setNote("Give them a discount next time!");

        json = objectMapper.writeValueAsString(customerDto);

        mvcResult = this.mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        customerDto.setId(id);

        // Create Vehicle
        vehicleDto = new VehicleDto();

        vehicleDto.setCustomer(customerDto);
        vehicleDto.setEngineNo("Engine No 1");
        vehicleDto.setMakeAndModel("Toyota Camry 2024 Hybrid");
        vehicleDto.setMileage(65000);
        vehicleDto.setNote("Give it a bath next time!");
        vehicleDto.setPlateNumber("126 - 629");
        vehicleDto.setVinNo("JX12345678");

        json = objectMapper.writeValueAsString(vehicleDto);

        mvcResult = this.mockMvc.perform(
                post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        vehicleDto.setId(id);
    }

    @Test
    public void createProduct_WithoutBaseUnitIfVariableShouldFail_status400() throws Exception {
        // setup invalid mocked unit
        ProductRequestDto invalidProductRequestDto = new ProductRequestDto();

        // create product
        invalidProductRequestDto = new ProductRequestDto();
        invalidProductRequestDto.setSubcategoryId(subcategoryRequestDto.getId());
        invalidProductRequestDto.setName("Engine Oil B");
        invalidProductRequestDto.setVariable(true);
        invalidProductRequestDto.setBaseUnit("");

        String json = objectMapper.writeValueAsString(
                invalidProductRequestDto
        );

        this.mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void updateProduct_WithoutBaseUnitIfVariableShouldFail_status400() throws Exception {
        // setup invalid mocked unit
        ProductRequestDto invalidProductRequestDto;

        // create product
        invalidProductRequestDto = new ProductRequestDto();
        invalidProductRequestDto.setSubcategoryId(productRequestDto.getSubcategoryId());
        invalidProductRequestDto.setName(productRequestDto.getName());
        invalidProductRequestDto.setVariable(productRequestDto.isVariable());
        invalidProductRequestDto.setBaseUnit("");

        String json = objectMapper.writeValueAsString(
                invalidProductRequestDto);
        MockMultipartFile file = new MockMultipartFile(
                        "data",
                        json,
                        String.valueOf(MediaType.APPLICATION_JSON),
                        json.getBytes()
                );

        this.mockMvc.perform(
                multipart("/products")
                        .file(file)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createUnit_WithoutToBaseUnitShouldFail_status400() throws Exception {
        // setup invalid mocked unit
        UnitRequestDto invalidUnitRequestDto = new UnitRequestDto();

        invalidUnitRequestDto.setName("1 Drum");
        invalidUnitRequestDto.setPrice(27500);
        invalidUnitRequestDto.setProductId(productRequestDto.getId());
        invalidUnitRequestDto.setQuantity(100);


        String json = objectMapper.writeValueAsString(
                invalidUnitRequestDto
        );

        this.mockMvc.perform(
                post("/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void updateUnit_WithoutToBaseUnitShouldFail_status400() throws Exception {
        UnitRequestDto validUnitRequestDto = unitRequestDtos.get(0);
        // setup invalid mocked unit
        UnitRequestDto invalidUnitRequestDto = new UnitRequestDto();
        invalidUnitRequestDto.setId(validUnitRequestDto.getId());
        invalidUnitRequestDto.setName(validUnitRequestDto.getName());
        invalidUnitRequestDto.setPrice(validUnitRequestDto.getPrice());
        invalidUnitRequestDto.setProductId(validUnitRequestDto.getProductId());
        invalidUnitRequestDto.setQuantity(validUnitRequestDto.getQuantity());

        String json = objectMapper.writeValueAsString(
                invalidUnitRequestDto
        );

        this.mockMvc.perform(
                put("/units/" + invalidUnitRequestDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
            ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createSale_ShouldDeduceStock_status200() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreatedAt(LocalDateTime.now().toString());
        saleRequestDto.setDueAt(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(0);
        saleRequestDto.setStatus(SaleStatus.PAID);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        ItemRequestDto firstItemRequestDto = new ItemRequestDto();

        firstItemRequestDto.setUnitId(unitRequestDtos.get(0).getId());
        firstItemRequestDto.setPrice(unitRequestDtos.get(0).getPrice());
        firstItemRequestDto.setQuantity(10);
        itemRequestDtos.add(firstItemRequestDto);

        ItemRequestDto secondItemRequestDto = new ItemRequestDto();
        secondItemRequestDto.setUnitId(unitRequestDtos.get(1).getId());
        secondItemRequestDto.setPrice(unitRequestDtos.get(1).getPrice());
        secondItemRequestDto.setQuantity(20);
        itemRequestDtos.add(secondItemRequestDto);

        saleRequestDto.setItems(itemRequestDtos);

        String json = objectMapper.writeValueAsString(saleRequestDto);

        this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitRequestDtos.get(0).getQuantity() - 10)
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitRequestDtos.get(1).getQuantity() - 20)
        );
    }

    @Test
    public void updateSale_ShouldUpdateStock_status200() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreatedAt(LocalDateTime.now().toString());
        saleRequestDto.setDueAt(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(0);
        saleRequestDto.setStatus(SaleStatus.PAID);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        ItemRequestDto firstItemRequestDto = new ItemRequestDto();

        firstItemRequestDto.setUnitId(unitRequestDtos.get(0).getId());
        firstItemRequestDto.setPrice(unitRequestDtos.get(0).getPrice());
        firstItemRequestDto.setQuantity(10);
        itemRequestDtos.add(firstItemRequestDto);

        ItemRequestDto secondItemRequestDto = new ItemRequestDto();
        secondItemRequestDto.setUnitId(unitRequestDtos.get(1).getId());
        secondItemRequestDto.setPrice(unitRequestDtos.get(1).getPrice());
        secondItemRequestDto.setQuantity(20);
        itemRequestDtos.add(secondItemRequestDto);

        saleRequestDto.setItems(itemRequestDtos);

        String json = objectMapper.writeValueAsString(saleRequestDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        itemRequestDtos.get(0).setQuantity(20);
        itemRequestDtos.get(1).setQuantity(5);

        json = objectMapper.writeValueAsString(saleRequestDto);

        String saleId = JsonPath.read(
                mvcResult.getResponse().getContentAsString(), "$.id"
        );

        this.mockMvc.perform(
                put("/sales/" + saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitRequestDtos.get(0).getQuantity() - 20)
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitRequestDtos.get(1).getQuantity() - 5)
        );
    }

    @Test
    public void removeSale_shouldRemoveStock_status200() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreatedAt(LocalDateTime.now().toString());
        saleRequestDto.setDueAt(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(0);
        saleRequestDto.setStatus(SaleStatus.PAID);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        ItemRequestDto firstItemRequestDto = new ItemRequestDto();

        firstItemRequestDto.setUnitId(unitRequestDtos.get(0).getId());
        firstItemRequestDto.setPrice(unitRequestDtos.get(0).getPrice());
        firstItemRequestDto.setQuantity(10);
        itemRequestDtos.add(firstItemRequestDto);

        ItemRequestDto secondItemRequestDto = new ItemRequestDto();
        secondItemRequestDto.setUnitId(unitRequestDtos.get(1).getId());
        secondItemRequestDto.setPrice(unitRequestDtos.get(1).getPrice());
        secondItemRequestDto.setQuantity(20);
        itemRequestDtos.add(secondItemRequestDto);

        saleRequestDto.setItems(itemRequestDtos);

        String json = objectMapper.writeValueAsString(saleRequestDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String saleId = JsonPath.read(
                mvcResult.getResponse().getContentAsString(), "$.id"
        );

        this.mockMvc.perform(
                delete("/sales/" + saleId)
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitRequestDtos.get(0).getQuantity())
        );

        this.mockMvc.perform(
                get("/units/" + unitRequestDtos.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitRequestDtos.get(1).getQuantity())
        );
    }
}
