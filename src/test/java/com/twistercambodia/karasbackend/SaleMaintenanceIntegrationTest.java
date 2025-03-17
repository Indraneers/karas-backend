package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.UserRole;
import com.twistercambodia.karasbackend.autoService.dto.AutoServiceDto;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceDto;
import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceAutoServiceDto;
import com.twistercambodia.karasbackend.sale.dto.SaleRequestDto;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;
import com.twistercambodia.karasbackend.storage.config.MinioConfig;
import com.twistercambodia.karasbackend.storage.service.StorageService;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import org.h2.tools.Server;
import org.hamcrest.Matchers;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
@TestPropertySource(locations="classpath:application.properties")
public class SaleMaintenanceIntegrationTest {
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

    private List<AutoServiceDto> autoServiceDtos;

    CustomerDto customerDto;

    VehicleDto vehicleDto;

    UserDto userDto;

    @BeforeAll
    public static void init() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8086")
                .start();
    }

    public void setupObjectMapper() {
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.findAndRegisterModules();
    }

    @BeforeEach
    public void setup() throws Exception {
        this.setupObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        this.autoServiceDtos = new ArrayList<>();

        AutoServiceDto autoServiceOne = new AutoServiceDto();
        autoServiceOne.setName("Tire refill");
        autoServiceOne.setPrice(500);

        String json = objectMapper.writeValueAsString(autoServiceOne);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/auto-services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id =JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        autoServiceOne.setId(id);

        autoServiceDtos.add(autoServiceOne);

        AutoServiceDto autoServiceTwo = new AutoServiceDto();
        autoServiceTwo.setName("Fluid transfer");
        autoServiceTwo.setPrice(1000);

        json = objectMapper.writeValueAsString(autoServiceTwo);

        id =JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        autoServiceTwo.setId(id);

        autoServiceDtos.add(autoServiceTwo);

        mvcResult = this.mockMvc.perform(
                post("/auto-services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

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
    public void createSale_shouldReturnCorrectMaintenance_status200() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreatedAt(LocalDateTime.now().toString());
        saleRequestDto.setDueAt(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(0);
        saleRequestDto.setStatus(SaleStatus.PAID);
        saleRequestDto.setItems(new ArrayList<>());

        MaintenanceDto maintenanceDto = new MaintenanceDto();

        maintenanceDto.setCreatedAt(LocalDateTime.now().toString());
        maintenanceDto.setMileage(70000);
        maintenanceDto.setNote("Test");
        maintenanceDto.setVehicleId(vehicleDto.getId());

        MaintenanceAutoServiceDto maintenanceServiceOne = new MaintenanceAutoServiceDto();
        maintenanceServiceOne.setService(autoServiceDtos.get(0));
        maintenanceServiceOne.setPrice(autoServiceDtos.get(0).getPrice());

        MaintenanceAutoServiceDto maintenanceServiceTwo = new MaintenanceAutoServiceDto();
        maintenanceServiceTwo.setService(autoServiceDtos.get(1));
        maintenanceServiceTwo.setPrice(autoServiceDtos.get(1).getPrice());

        Set<MaintenanceAutoServiceDto> maintenanceAutoServiceDtoSet = new HashSet<>();
        maintenanceAutoServiceDtoSet.add(maintenanceServiceOne);
        maintenanceAutoServiceDtoSet.add(maintenanceServiceTwo);

        maintenanceDto.setServices(new HashSet<>());
        maintenanceDto.getServices().add(maintenanceServiceOne);
        maintenanceDto.getServices().add(maintenanceServiceTwo);

        saleRequestDto.setMaintenance(maintenanceDto);

        List<String> expectedServicesName = maintenanceDto.getServices()
                .stream()
                .map(ms -> ms.getService().getName())
                .toList();

        List<Integer> expectedServicesPrice = maintenanceDto.getServices()
                .stream()
                .map(MaintenanceAutoServiceDto::getPrice)
                .toList();

        List<Integer> expectedServicesDiscount = maintenanceDto.getServices()
                .stream()
                .map(MaintenanceAutoServiceDto::getDiscount)
                .toList();

        String json = objectMapper.writeValueAsString(saleRequestDto);

        System.out.println(this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse().getContentAsString());

        this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.jsonPath("$.maintenance.mileage")
                        .value(maintenanceDto.getMileage()),

                MockMvcResultMatchers.jsonPath("$.maintenance.note")
                        .value(maintenanceDto.getNote()),
                MockMvcResultMatchers.jsonPath("$.maintenance.services[*].service.name")
                        .value(Matchers.containsInAnyOrder(expectedServicesName.toArray())),
                MockMvcResultMatchers.jsonPath("$.maintenance.services[*].price")
                        .value(Matchers.containsInAnyOrder(expectedServicesPrice.toArray())),
                MockMvcResultMatchers.jsonPath("$.maintenance.services[*].discount")
                        .value(Matchers.containsInAnyOrder(expectedServicesDiscount.toArray()))
        );
    }

    @Test
    public void updateSale_shouldReturnCorrectMaintenance_status200() throws Exception {
        SaleRequestDto saleRequestDto = new SaleRequestDto();

        saleRequestDto.setCustomerId(customerDto.getId());
        saleRequestDto.setVehicleId(vehicleDto.getId());
        saleRequestDto.setUserId(userDto.getId());
        saleRequestDto.setCreatedAt(LocalDateTime.now().toString());
        saleRequestDto.setDueAt(LocalDateTime.now().toString());
        saleRequestDto.setDiscount(0);
        saleRequestDto.setStatus(SaleStatus.PAID);
        saleRequestDto.setItems(new ArrayList<>());

        MaintenanceDto maintenanceDto = new MaintenanceDto();

        maintenanceDto.setCreatedAt(LocalDateTime.now().toString());
        maintenanceDto.setMileage(70000);
        maintenanceDto.setNote("Test");
        maintenanceDto.setVehicleId(vehicleDto.getId());

        MaintenanceAutoServiceDto maintenanceServiceOne = new MaintenanceAutoServiceDto();
        maintenanceServiceOne.setService(autoServiceDtos.get(0));
        maintenanceServiceOne.setPrice(autoServiceDtos.get(0).getPrice());

        MaintenanceAutoServiceDto maintenanceServiceTwo = new MaintenanceAutoServiceDto();
        maintenanceServiceTwo.setService(autoServiceDtos.get(1));
        maintenanceServiceTwo.setPrice(autoServiceDtos.get(1).getPrice());

        Set<MaintenanceAutoServiceDto> maintenanceAutoServiceDtoSet = new HashSet<>();
        maintenanceAutoServiceDtoSet.add(maintenanceServiceOne);
        maintenanceAutoServiceDtoSet.add(maintenanceServiceTwo);

        maintenanceDto.setServices(new HashSet<>());
        maintenanceDto.getServices().add(maintenanceServiceOne);
        maintenanceDto.getServices().add(maintenanceServiceTwo);

        saleRequestDto.setMaintenance(maintenanceDto);

        maintenanceDto.setServices(maintenanceAutoServiceDtoSet);
        saleRequestDto.setMaintenance(maintenanceDto);

        String json = objectMapper.writeValueAsString(saleRequestDto);
        MvcResult mvcResult = this.mockMvc.perform(
                post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String saleId = JsonPath.read(
                mvcResult.getResponse().getContentAsString(), "$.id"
        );

        maintenanceServiceOne.setPrice(250);
        maintenanceServiceOne.setDiscount(250);

        Set<MaintenanceAutoServiceDto> newMaintenanceServices = new HashSet<>();
        newMaintenanceServices.add(maintenanceServiceOne);

        maintenanceDto.setServices(newMaintenanceServices);
        maintenanceDto.setMileage((75000));
        maintenanceDto.setNote("Test 2");

        List<String> expectedServicesName = maintenanceDto.getServices()
                .stream()
                .map(ms -> ms.getService().getName())
                .toList();

        List<Integer> expectedServicesPrice = maintenanceDto.getServices()
                .stream()
                .map(MaintenanceAutoServiceDto::getPrice)
                .toList();

        List<Integer> expectedServicesDiscount = maintenanceDto.getServices()
                .stream()
                .map(MaintenanceAutoServiceDto::getDiscount)
                .toList();

        json = objectMapper.writeValueAsString(saleRequestDto);

        this.mockMvc.perform(
                put("/sales/" + saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.jsonPath("$.maintenance.mileage")
                        .value(maintenanceDto.getMileage()),
                MockMvcResultMatchers.jsonPath("$.maintenance.note")
                        .value(maintenanceDto.getNote()),
                MockMvcResultMatchers.jsonPath("$.maintenance.services[*].service.name")
                        .value(Matchers.containsInAnyOrder(expectedServicesName.toArray())),
                MockMvcResultMatchers.jsonPath("$.maintenance.services[*].price")
                        .value(Matchers.containsInAnyOrder(expectedServicesPrice.toArray())),
                MockMvcResultMatchers.jsonPath("$.maintenance.services[*].discount")
                        .value(Matchers.containsInAnyOrder(expectedServicesDiscount.toArray()))
        );
    }
}
