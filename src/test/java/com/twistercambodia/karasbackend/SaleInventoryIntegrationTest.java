package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.UserRole;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitDto;
import com.twistercambodia.karasbackend.sale.dto.ItemRequestDto;
import com.twistercambodia.karasbackend.sale.dto.SaleRequestDto;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
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
public class SaleInventoryIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private JwtDecoder jwtDecoder;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    List<UnitDto> unitDtos = new ArrayList<>();

    CategoryDto categoryDto = new CategoryDto();

    ProductDto productDto;

    CustomerDto customerDto;

    VehicleDto vehicleDto;

    UserDto userDto;

    @BeforeAll
    public static void init() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8083")
                .start();
    }

    public void setupProducts(ProductDto requestProductDto) throws Exception {
        System.out.println(requestProductDto.getName());
        String json = objectMapper.writeValueAsString(requestProductDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        requestProductDto.setId(id);

        productDto = requestProductDto;
    }

    public void setupObjectMapper() {
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.findAndRegisterModules();
    }

    public void setupProductsWithUnits(List<UnitDto> mockedUnitDtos) throws Exception {
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
            addedUnitDto.setToBaseUnit(unitDto.getToBaseUnit());
            addedUnitDto.setQuantity(unitDto.getQuantity());
            unitDtos.add(addedUnitDto);
        }
    }
    @BeforeEach
    public void setup() throws Exception {
        this.setupObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        // Create Category
        categoryDto = new CategoryDto();
        categoryDto.setName("Engine Oil");

        String json = objectMapper.writeValueAsString(categoryDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String categoryId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        categoryDto.setId(categoryId);

        // create product
        productDto = new ProductDto();
        productDto.setCategoryId(categoryId);
        productDto.setName("Engine Oil A");
        productDto.setVariable(true);
        productDto.setBaseUnit("1L");

        this.setupProducts(productDto);

        // create units
        UnitDto unitDtoOne = new UnitDto();
        unitDtoOne.setName("1L");
        // 500
        unitDtoOne.setPrice(500);
        unitDtoOne.setQuantity(100);
        unitDtoOne.setToBaseUnit(1);

        UnitDto unitDtoTwo = new UnitDto();
        unitDtoTwo.setName("2L");
        unitDtoTwo.setPrice(200);
        unitDtoTwo.setQuantity(50);
        unitDtoTwo.setToBaseUnit(2);

        List<UnitDto> unitDtoMocks = new ArrayList<>();

        unitDtoMocks.add(unitDtoOne);
        unitDtoMocks.add(unitDtoTwo);

        this.setupProductsWithUnits(unitDtoMocks);

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

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

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
        ProductDto invalidProductDto = new ProductDto();

        // create product
        invalidProductDto = new ProductDto();
        invalidProductDto.setCategoryId(categoryDto.getId());
        invalidProductDto.setName("Engine Oil B");
        invalidProductDto.setVariable(true);
        invalidProductDto.setBaseUnit("");

        String json = objectMapper.writeValueAsString(
                invalidProductDto
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
        ProductDto invalidProductDto;

        // create product
        invalidProductDto = new ProductDto();
        invalidProductDto.setCategoryId(productDto.getCategoryId());
        invalidProductDto.setName(productDto.getName());
        invalidProductDto.setVariable(productDto.isVariable());
        invalidProductDto.setBaseUnit("");

        String json = objectMapper.writeValueAsString(
                invalidProductDto
        );

        this.mockMvc.perform(
                put("/products/" + productDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createUnit_WithoutToBaseUnitShouldFail_status400() throws Exception {
        // setup invalid mocked unit
        UnitDto invalidUnitDto = new UnitDto();

        invalidUnitDto.setName("1 Drum");
        invalidUnitDto.setPrice(27500);
        invalidUnitDto.setProductId(productDto.getId());
        invalidUnitDto.setQuantity(100);


        String json = objectMapper.writeValueAsString(
                invalidUnitDto
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
        UnitDto validUnitDto = unitDtos.get(0);
        // setup invalid mocked unit
        UnitDto invalidUnitDto = new UnitDto();
        invalidUnitDto.setId(validUnitDto.getId());
        invalidUnitDto.setName(validUnitDto.getName());
        invalidUnitDto.setPrice(validUnitDto.getPrice());
        invalidUnitDto.setProductId(validUnitDto.getProductId());
        invalidUnitDto.setQuantity(validUnitDto.getQuantity());

        String json = objectMapper.writeValueAsString(
                invalidUnitDto
        );

        this.mockMvc.perform(
                put("/units/" + invalidUnitDto.getId())
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
        saleRequestDto.setCreated(LocalDateTime.now().toString());
        saleRequestDto.setDueDate(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(0);
        saleRequestDto.setStatus(SaleStatus.PAID);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        ItemRequestDto firstItemRequestDto = new ItemRequestDto();

        firstItemRequestDto.setUnitId(unitDtos.get(0).getId());
        firstItemRequestDto.setPrice(unitDtos.get(0).getPrice());
        firstItemRequestDto.setQuantity(10);
        itemRequestDtos.add(firstItemRequestDto);

        ItemRequestDto secondItemRequestDto = new ItemRequestDto();
        secondItemRequestDto.setUnitId(unitDtos.get(1).getId());
        secondItemRequestDto.setPrice(unitDtos.get(1).getPrice());
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
                get("/units/" + unitDtos.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitDtos.get(0).getQuantity() - 10)
        );

        this.mockMvc.perform(
                get("/units/" + unitDtos.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitDtos.get(1).getQuantity() - 20)
        );
    }

    @Test
    public void updateSale_ShouldUpdateStock_status200() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreated(LocalDateTime.now().toString());
        saleRequestDto.setDueDate(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(0);
        saleRequestDto.setStatus(SaleStatus.PAID);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        ItemRequestDto firstItemRequestDto = new ItemRequestDto();

        firstItemRequestDto.setUnitId(unitDtos.get(0).getId());
        firstItemRequestDto.setPrice(unitDtos.get(0).getPrice());
        firstItemRequestDto.setQuantity(10);
        itemRequestDtos.add(firstItemRequestDto);

        ItemRequestDto secondItemRequestDto = new ItemRequestDto();
        secondItemRequestDto.setUnitId(unitDtos.get(1).getId());
        secondItemRequestDto.setPrice(unitDtos.get(1).getPrice());
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
                get("/units/" + unitDtos.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitDtos.get(0).getQuantity() - 20)
        );

        this.mockMvc.perform(
                get("/units/" + unitDtos.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitDtos.get(1).getQuantity() - 5)
        );
    }

    @Test
    public void removeSale_shouldRemoveStock_status200() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreated(LocalDateTime.now().toString());
        saleRequestDto.setDueDate(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(0);
        saleRequestDto.setStatus(SaleStatus.PAID);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        ItemRequestDto firstItemRequestDto = new ItemRequestDto();

        firstItemRequestDto.setUnitId(unitDtos.get(0).getId());
        firstItemRequestDto.setPrice(unitDtos.get(0).getPrice());
        firstItemRequestDto.setQuantity(10);
        itemRequestDtos.add(firstItemRequestDto);

        ItemRequestDto secondItemRequestDto = new ItemRequestDto();
        secondItemRequestDto.setUnitId(unitDtos.get(1).getId());
        secondItemRequestDto.setPrice(unitDtos.get(1).getPrice());
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
                get("/units/" + unitDtos.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitDtos.get(0).getQuantity())
        );

        this.mockMvc.perform(
                get("/units/" + unitDtos.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity")
                        .value(unitDtos.get(1).getQuantity())
        );
    }
}
