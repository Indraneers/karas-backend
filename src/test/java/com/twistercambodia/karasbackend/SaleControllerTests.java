package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.entity.UserRole;
import com.twistercambodia.karasbackend.config.TestSecurityConfig;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitRequestDto;
import com.twistercambodia.karasbackend.sale.dto.ItemRequestDto;
import com.twistercambodia.karasbackend.sale.dto.SaleRequestDto;
import com.twistercambodia.karasbackend.sale.dto.SaleResponseDto;
import com.twistercambodia.karasbackend.sale.entity.PaymentType;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;
import com.twistercambodia.karasbackend.storage.config.MinioConfig;
import com.twistercambodia.karasbackend.storage.service.StorageService;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import com.twistercambodia.karasbackend.vehicle.entity.VehicleType;
import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations="classpath:application.properties")
@Import(TestSecurityConfig.class)
@SpringBootTest()
public class SaleControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TestSecurityConfig testSecurityConfig;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private MinioConfig minioConfig; // Mock the MinIO configuration bean.

    @MockBean
    private StorageService storageService; // Mock the StorageService.

    private ObjectMapper objectMapper;

    private ModelMapper modelMapper;

    private MockMvc mockMvc;

    List<ProductRequestDto> productRequestDtos;

    List<UnitRequestDto> unitRequestDtos;

    CustomerDto customerDto;

    VehicleDto vehicleDto;

    UserDto userDto;

    @BeforeAll
    public static void init() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082")
                .start();
    }

    public void setupProducts() throws Exception {
        for (ProductRequestDto productRequestDto : productRequestDtos) {
            System.out.println(productRequestDto.getName());
            String json = objectMapper.writeValueAsString(productRequestDto);
            MockMultipartFile file = new MockMultipartFile(
                    "data",
                    json,
                    String.valueOf(MediaType.APPLICATION_JSON),
                    json.getBytes()
            );

            MvcResult mvcResult = this.mockMvc.perform(
                    multipart("/products")
                            .file(file)
                            .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
            ).andReturn();

            String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

            productRequestDto.setId(id);
        }
    }

    public void setupUtility() {
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.findAndRegisterModules();
        this.modelMapper = new ModelMapper();
    }

    public void setupProductsWithUnits(List<UnitRequestDto> mockedUnitRequestDtos) throws Exception {
        for (ProductRequestDto productRequestDto : productRequestDtos) {
            for (UnitRequestDto unitRequestDto : mockedUnitRequestDtos) {
                unitRequestDto.setProductId(productRequestDto.getId());
                String json = objectMapper.writeValueAsString(unitRequestDto);

                MvcResult mvcResult = this.mockMvc.perform(
                        post("/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                ).andReturn();

                String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

                UnitRequestDto addedUnitRequestDto = new UnitRequestDto();

                addedUnitRequestDto.setId(id);
                addedUnitRequestDto.setName(unitRequestDto.getName());
                addedUnitRequestDto.setPrice(unitRequestDto.getPrice());
                addedUnitRequestDto.setProductId(unitRequestDto.getProductId());
                addedUnitRequestDto.setQuantity(unitRequestDto.getQuantity());

                unitRequestDtos.add(addedUnitRequestDto);
            }
        }
    }

    @BeforeEach
    public void setup() throws Exception {
        this.setupUtility();
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();


        // Create User
        this.userDto = new UserDto();

        userDto.setUsername("Service Person A");
        userDto.setRole(UserRole.ADMIN);
        userDto.setEmail("admin@example.com");

        String json = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = this.mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(TestSecurityConfig.testJwt("admin", "USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        userDto.setId(id);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Engine Oil");

        json = objectMapper.writeValueAsString(categoryDto);
        MockMultipartFile file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );

        mvcResult = this.mockMvc.perform(
                        multipart("/categories")
                                .file(file)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andReturn();

        id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        categoryDto.setId(id);

        SubcategoryRequestDto subcategoryRequestDto = new SubcategoryRequestDto();
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
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andReturn();

        id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        subcategoryRequestDto.setId(id);

        this.productRequestDtos = new ArrayList<>();

        // Create Products
        ProductRequestDto productRequestDtoOne = new ProductRequestDto();
        productRequestDtoOne.setSubcategoryId(subcategoryRequestDto.getId());
        productRequestDtoOne.setName("Engine Oil A");
        productRequestDtoOne.setVariable(false);

        productRequestDtos.add(productRequestDtoOne);

        ProductRequestDto productRequestDtoTwo = new ProductRequestDto();
        productRequestDtoTwo.setSubcategoryId(subcategoryRequestDto.getId());
        productRequestDtoTwo.setName("Engine Oil B");
        productRequestDtoTwo.setVariable(false);

        productRequestDtos.add(productRequestDtoTwo);

        this.setupProducts();

        // Create Units
        UnitRequestDto unitRequestDtoOne = new UnitRequestDto();
        unitRequestDtoOne.setName("1L");
        unitRequestDtoOne.setPrice(500);
        unitRequestDtoOne.setQuantity(100);

        UnitRequestDto unitRequestDtoTwo = new UnitRequestDto();
        unitRequestDtoTwo.setName("2L");
        unitRequestDtoTwo.setPrice(1000);
        unitRequestDtoTwo.setQuantity(100);

        List<UnitRequestDto> unitRequestDtoMocks = new ArrayList<>();

        unitRequestDtoMocks.add(unitRequestDtoOne);
        unitRequestDtoMocks.add(unitRequestDtoTwo);

        this.unitRequestDtos = new ArrayList<>();
        this.setupProductsWithUnits(unitRequestDtoMocks);

        // Create Customer
        customerDto = new CustomerDto();

        customerDto.setName("Car Person A");
        customerDto.setNote("Give them a discount next time!");

        json = objectMapper.writeValueAsString(customerDto);

        mvcResult = this.mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
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
        vehicleDto.setVehicleType(VehicleType.PASSENGER_CAR);

        json = objectMapper.writeValueAsString(vehicleDto);

        mvcResult = this.mockMvc.perform(
                post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andReturn();

        id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        vehicleDto.setId(id);
    }

    public SaleResponseDto createSaleObject() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreatedAt(LocalDateTime.now().toString());
        saleRequestDto.setDueAt(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(100); // $1 Discount
        saleRequestDto.setStatus(SaleStatus.PAID);
        saleRequestDto.setPaymentType(PaymentType.BANK);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        // Create item
        for (int i = 0; i < unitRequestDtos.size(); ++i) {
            // Create Items
            ItemRequestDto itemRequestDto = new ItemRequestDto();

            itemRequestDto.setUnitId(unitRequestDtos.get(i).getId());
            itemRequestDto.setPrice(unitRequestDtos.get(i).getPrice());
            itemRequestDto.setQuantity(2);
            itemRequestDtos.add(itemRequestDto);
        }

        saleRequestDto.setItems(itemRequestDtos);

        String json = objectMapper.writeValueAsString(saleRequestDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andReturn();

        SaleResponseDto saleResponseDto = this.modelMapper.map(saleRequestDto, SaleResponseDto.class);
        saleResponseDto.setId(JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id"));

        return saleResponseDto;
    }

    @Test
    public void createSale_shouldReturnPaidSale_status200() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreatedAt(LocalDateTime.now().toString());
        saleRequestDto.setDueAt(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(100); // $1 Discount
        saleRequestDto.setStatus(SaleStatus.PAID);
        saleRequestDto.setPaymentType(PaymentType.BANK);

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        // Create item
        for (int i = 0; i < unitRequestDtos.size(); ++i) {
            // Create Items
            ItemRequestDto itemRequestDto = new ItemRequestDto();

            itemRequestDto.setUnitId(unitRequestDtos.get(i).getId());
            itemRequestDto.setPrice(unitRequestDtos.get(i).getPrice());
            itemRequestDto.setQuantity(2);
            itemRequestDtos.add(itemRequestDto);
        }

        saleRequestDto.setItems(itemRequestDtos);

        String json = objectMapper.writeValueAsString(saleRequestDto);

        this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andExpectAll(
                MockMvcResultMatchers.jsonPath("$.customer.id")
                        .value(saleRequestDto.getCustomerId()),
                MockMvcResultMatchers.jsonPath("$.vehicle.id")
                        .value(saleRequestDto.getVehicleId()),
                MockMvcResultMatchers.jsonPath("$.user.id")
                        .value(saleRequestDto.getUserId()),
                MockMvcResultMatchers.jsonPath("$.createdAt")
                        .value(saleRequestDto.getCreatedAt()),
                MockMvcResultMatchers.jsonPath("$.dueAt")
                        .value(saleRequestDto.getDueAt()),
                MockMvcResultMatchers.jsonPath("$.discount")
                        .value(saleRequestDto.getDiscount()),
                MockMvcResultMatchers.jsonPath("$.items[0].unit.id")
                        .value(saleRequestDto.getItems().get(0).getUnitId()),
                MockMvcResultMatchers.jsonPath("$.items[1].unit.id")
                        .value(saleRequestDto.getItems().get(1).getUnitId()),
                MockMvcResultMatchers.jsonPath("$.items", hasSize(4)),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(saleRequestDto.getStatus().toString()),
                MockMvcResultMatchers.jsonPath("$.paymentType")
                        .value(saleRequestDto.getPaymentType().toString())
        );

        // check if category exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/sale?page=0")
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content")
                                .isArray()
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content",
                                hasSize(1))
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].name")
                                .value("Sale Creation")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("POST")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("SALE")
                );
    }

    @Test
    public void getBySalesByCustomerId_shouldReturnSales_status200() throws Exception {
        SaleResponseDto saleResponseDto = createSaleObject();

        String json = this.mockMvc.perform(
                get("/customers/" + customerDto.getId() + "/sales?page=" + 0)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andReturn().getResponse().getContentAsString();

        System.out.println(json);

        this.mockMvc.perform(
                get("/customers/" + customerDto.getId() + "/sales?page=" + 0)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andExpectAll(
                MockMvcResultMatchers.jsonPath("$.content[0].customer.id")
                        .value(saleResponseDto.getCustomer().getId()),
                MockMvcResultMatchers.jsonPath("$.content[0].vehicle.id")
                        .value(saleResponseDto.getVehicle().getId()),
                MockMvcResultMatchers.jsonPath("$.content[0].user.id")
                        .value(saleResponseDto.getUser().getId()),
                MockMvcResultMatchers.jsonPath("$.content[0].createdAt")
                        .value(saleResponseDto.getCreatedAt()),
                MockMvcResultMatchers.jsonPath("$.content[0].dueAt")
                        .value(saleResponseDto.getDueAt()),
                MockMvcResultMatchers.jsonPath("$.content[0].discount")
                        .value(saleResponseDto.getDiscount()),
                MockMvcResultMatchers.jsonPath("$.content[0].items[0].unit.id")
                        .value(saleResponseDto.getItems().get(0).getUnit().getId()),
                MockMvcResultMatchers.jsonPath("$.content[0].items[1].unit.id")
                        .value(saleResponseDto.getItems().get(1).getUnit().getId()),
                MockMvcResultMatchers.jsonPath("$.content[0].items", hasSize(4)),
                MockMvcResultMatchers.jsonPath("$.content[0].status")
                        .value(saleResponseDto.getStatus().toString()),
                MockMvcResultMatchers.jsonPath("$.content[0].paymentType")
                        .value(saleResponseDto.getPaymentType().toString())
        );
    }

    @Test
    public void updateSale_shouldReturnUpdatedSaleAndPruneOldItems_status200() throws Exception {
        SaleResponseDto saleResponseDto = createSaleObject();

        String json = objectMapper.writeValueAsString(saleResponseDto);

        String itemIdOne = saleResponseDto.getItems().get(0).getId();

        String itemIdTwo = saleResponseDto.getItems().get(1).getId();

        String saleId = saleResponseDto.getId();

        SaleRequestDto updatedSale = this.modelMapper.map(saleResponseDto, SaleRequestDto.class);
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        // Update new items
        for (int i = 0; i < unitRequestDtos.size(); ++i) {
            ItemRequestDto itemRequestDto = new ItemRequestDto();

            itemRequestDto.setUnitId(unitRequestDtos.get(i).getId());
            itemRequestDto.setPrice(unitRequestDtos.get(i).getPrice());
            itemRequestDto.setQuantity(3);
            itemRequestDtos.add(itemRequestDto);
        }

        updatedSale.setItems(itemRequestDtos);

        json = objectMapper.writeValueAsString(updatedSale);

        this.mockMvc.perform(
                put("/sales/" + saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andExpectAll(
                MockMvcResultMatchers.jsonPath("$.customer.id")
                        .value(updatedSale.getCustomerId()),
                MockMvcResultMatchers.jsonPath("$.vehicle.id")
                        .value(updatedSale.getVehicleId()),
                MockMvcResultMatchers.jsonPath("$.user.id")
                        .value(updatedSale.getUserId()),
                MockMvcResultMatchers.jsonPath("$.createdAt")
                        .value(updatedSale.getCreatedAt()),
                MockMvcResultMatchers.jsonPath("$.dueAt")
                        .value(updatedSale.getDueAt()),
                MockMvcResultMatchers.jsonPath("$.discount")
                        .value(updatedSale.getDiscount()),
                MockMvcResultMatchers.jsonPath("$.items[0].unit.id")
                        .value(not(itemIdOne)),
                MockMvcResultMatchers.jsonPath("$.items[1].unit.id")
                        .value(updatedSale.getItems().get(1).getUnitId()),
                MockMvcResultMatchers.jsonPath("$.items[1].unit.id")
                        .value(not(itemIdTwo)),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(updatedSale.getStatus().toString()),
                MockMvcResultMatchers.jsonPath("$.paymentType")
                        .value(updatedSale.getPaymentType().toString())
        );

        // check if sale exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/sale?page=0")
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content")
                                .isArray()
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content",
                                hasSize(2))
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].name")
                                .value("Sale Updated")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("PUT")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("SALE")
                );
    }

    @Test
    public void deleteSale_shouldDeleteSale_status400() throws Exception {
        SaleResponseDto saleResponseDto = createSaleObject();
        String id = saleResponseDto.getId();

        this.mockMvc.perform(
                delete("/sales/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        );

        this.mockMvc.perform(
                get("/sales/" + id)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andExpect(
                status().isNotFound()
        );

        // check if sales exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/sale?page=0")
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content")
                                .isArray()
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content",
                                hasSize(2))
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].name")
                                .value("Sale Deletion")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("DELETE")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("SALE")
                );
    }

    @Test
    public void getSales_shouldFilterByUserId_status200() throws Exception {
        SaleResponseDto saleResponseDto = createSaleObject();
        String id = saleResponseDto.getId();

        // create another user
        UserDto user2 = new UserDto();
        user2.setUsername("Sales Person B");
        user2.setEmail("admin2@example.com");

        String json =  objectMapper.writeValueAsString(user2);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andReturn();

        user2.setId(JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id"));

        this.mockMvc.perform(
                get("/sales?page=0&userId=" + user2.getId())
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content").isEmpty()
                );

        this.mockMvc.perform(
                        get("/sales?page=0&userId=" + userDto.getId())
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content").isNotEmpty()
                );
    }
}
