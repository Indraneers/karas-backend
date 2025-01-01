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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
public class SaleControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private JwtDecoder jwtDecoder;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    List<ProductDto> productDtos;

    List<UnitDto> unitDtos;

    CustomerDto customerDto;

    VehicleDto vehicleDto;

    UserDto userDto;

    @BeforeAll
    public static void init() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082")
                .start();
    }

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

    public void setupObjectMapper() {
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.findAndRegisterModules();
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
        this.setupObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        // Create Category
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

        // Create Products
        ProductDto productDtoOne = new ProductDto();
        productDtoOne.setCategoryId(categoryId);
        productDtoOne.setName("Engine Oil A");
        productDtoOne.setVariable(false);

        productDtos.add(productDtoOne);

        ProductDto productDtoTwo = new ProductDto();
        productDtoTwo.setCategoryId(categoryId);
        productDtoTwo.setName("Engine Oil B");
        productDtoTwo.setVariable(false);

        productDtos.add(productDtoTwo);

        this.setupProducts();

        // Create Units
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
    public void createSale_shouldReturnPaidSale_status200() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreated(LocalDateTime.now().toString());
        saleRequestDto.setDueDate(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(100); // $1 Discount
        saleRequestDto.setStatus(SaleStatus.PAID);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        // Create item
        for (int i = 0; i < unitDtos.size(); ++i) {
            // Create Items
            ItemRequestDto itemRequestDto = new ItemRequestDto();

            itemRequestDto.setUnitId(unitDtos.get(i).getId());
            itemRequestDto.setPrice(unitDtos.get(i).getPrice());
            itemRequestDto.setQuantity(2);
            itemRequestDtos.add(itemRequestDto);
        }

        saleRequestDto.setItems(itemRequestDtos);

        String json = objectMapper.writeValueAsString(saleRequestDto);

        this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.jsonPath("$.customer.id")
                        .value(saleRequestDto.getCustomerId()),
                MockMvcResultMatchers.jsonPath("$.vehicle.id")
                        .value(saleRequestDto.getVehicleId()),
                MockMvcResultMatchers.jsonPath("$.user.id")
                        .value(saleRequestDto.getUserId()),
                MockMvcResultMatchers.jsonPath("$.created")
                        .value(saleRequestDto.getCreated()),
                MockMvcResultMatchers.jsonPath("$.dueDate")
                        .value(saleRequestDto.getDueDate()),
                MockMvcResultMatchers.jsonPath("$.discount")
                        .value(saleRequestDto.getDiscount()),
                MockMvcResultMatchers.jsonPath("$.items[0].unit.id")
                        .value(saleRequestDto.getItems().get(0).getUnitId()),
                MockMvcResultMatchers.jsonPath("$.items[1].unit.id")
                        .value(saleRequestDto.getItems().get(1).getUnitId()),
                MockMvcResultMatchers.jsonPath("$.items", hasSize(4)),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(saleRequestDto.getStatus().toString())
        );
    }

    @Test
    public void updateSale_shouldReturnUpdatedSaleAndPruneOldItems_status200() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreated(LocalDateTime.now().toString());
        saleRequestDto.setDueDate(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(100); // $1 Discount
        saleRequestDto.setStatus(SaleStatus.PAID);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        // Create item
        for (int i = 0; i < unitDtos.size(); ++i) {
            // Create Items
            ItemRequestDto itemRequestDto = new ItemRequestDto();

            itemRequestDto.setUnitId(unitDtos.get(i).getId());
            itemRequestDto.setPrice(unitDtos.get(i).getPrice());
            itemRequestDto.setQuantity(2);
            itemRequestDtos.add(itemRequestDto);
        }

        saleRequestDto.setItems(itemRequestDtos);

        String json = objectMapper.writeValueAsString(saleRequestDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String itemIdOne = JsonPath.read(
                mvcResult.getResponse().getContentAsString(), "$.items[0].id"
        );

        String itemIdTwo = JsonPath.read(
                mvcResult.getResponse().getContentAsString(), "$.items[1].id"
                );

        String saleId = JsonPath.read(
                mvcResult.getResponse().getContentAsString(), "$.id"
        );

        itemRequestDtos.clear();

        // Update new items
        for (int i = 0; i < unitDtos.size(); ++i) {
            ItemRequestDto itemRequestDto = new ItemRequestDto();

            itemRequestDto.setUnitId(unitDtos.get(i).getId());
            itemRequestDto.setPrice(unitDtos.get(i).getPrice());
            itemRequestDto.setQuantity(3);
            itemRequestDtos.add(itemRequestDto);
        }

        saleRequestDto.setItems(itemRequestDtos);

        json = objectMapper.writeValueAsString(saleRequestDto);

        this.mockMvc.perform(
                put("/sales/" + saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.jsonPath("$.customer.id")
                        .value(saleRequestDto.getCustomerId()),
                MockMvcResultMatchers.jsonPath("$.vehicle.id")
                        .value(saleRequestDto.getVehicleId()),
                MockMvcResultMatchers.jsonPath("$.user.id")
                        .value(saleRequestDto.getUserId()),
                MockMvcResultMatchers.jsonPath("$.created")
                        .value(saleRequestDto.getCreated()),
                MockMvcResultMatchers.jsonPath("$.dueDate")
                        .value(saleRequestDto.getDueDate()),
                MockMvcResultMatchers.jsonPath("$.discount")
                        .value(saleRequestDto.getDiscount()),
                MockMvcResultMatchers.jsonPath("$.items[0].unit.id")
                        .value(not(itemIdOne)),
                MockMvcResultMatchers.jsonPath("$.items[1].unit.id")
                        .value(saleRequestDto.getItems().get(1).getUnitId()),
                MockMvcResultMatchers.jsonPath("$.items[1].unit.id")
                        .value(not(itemIdTwo)),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(saleRequestDto.getStatus().toString())
        );
    }

    @Test
    public void deleteSale_shouldDeleteSale_status400() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreated(LocalDateTime.now().toString());
        saleRequestDto.setDueDate(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(100); // $1 Discount
        saleRequestDto.setStatus(SaleStatus.PAID);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        // Create item
        for (int i = 0; i < unitDtos.size(); ++i) {
            // Create Items
            ItemRequestDto itemRequestDto = new ItemRequestDto();

            itemRequestDto.setUnitId(unitDtos.get(i).getId());
            itemRequestDto.setPrice(unitDtos.get(i).getPrice());
            itemRequestDto.setQuantity(2);
            itemRequestDtos.add(itemRequestDto);
        }

        saleRequestDto.setItems(itemRequestDtos);

        String json = objectMapper.writeValueAsString(saleRequestDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(
                mvcResult.getResponse().getContentAsString(), "$.id"
        );

        this.mockMvc.perform(
                delete("/sales/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        this.mockMvc.perform(
                get("/sales/" + id)
        ).andExpect(
                status().isNotFound()
        );
    }
}
