package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.storage.config.MinioConfig;
import com.twistercambodia.karasbackend.storage.service.StorageService;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import com.twistercambodia.karasbackend.vehicle.entity.VehicleType;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
@TestPropertySource(locations="classpath:application.properties")
public class VehicleControllerTests {
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

    private CustomerDto customerDto;

    @BeforeEach
    public void setup() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        customerDto = new CustomerDto();

        customerDto.setName("Car Person A");
        customerDto.setNote("Give them a discount next time!");

        String json = objectMapper.writeValueAsString(customerDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        customerDto.setId(id);
    }

    @Test
    void createVehicle_shouldReturnNewVehicle_status200() throws Exception {
        VehicleDto vehicleDto = new VehicleDto();

        vehicleDto.setCustomer(customerDto);
        vehicleDto.setEngineNo("Engine No 1");
        vehicleDto.setMakeAndModel("Toyota Camry 2024 Hybrid");
        vehicleDto.setMileage(65000);
        vehicleDto.setNote("Give it a bath next time!");
        vehicleDto.setPlateNumber("126 - 629");
        vehicleDto.setVinNo("JX12345678");
        vehicleDto.setVehicleType(VehicleType.PASSENGER_CAR);

        String json = objectMapper.writeValueAsString(vehicleDto);

        this.mockMvc.perform(
                        post("/vehicles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.customer.name")
                                .value((vehicleDto.getCustomer().getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.engineNo")
                                .value((vehicleDto.getEngineNo()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.makeAndModel")
                                .value((vehicleDto.getMakeAndModel()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.mileage")
                                .value((vehicleDto.getMileage()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.note")
                                .value((vehicleDto.getNote()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.plateNumber")
                                .value((vehicleDto.getPlateNumber()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.vinNo")
                                .value((vehicleDto.getVinNo()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.vehicleType")
                                .value((vehicleDto.getVehicleType().name()))
                );
    }

    @Test
    void createVehicle_DuplicateVehicleException_status400() throws Exception {
        VehicleDto vehicleDto = new VehicleDto();

        vehicleDto.setCustomer(customerDto);
        vehicleDto.setEngineNo("Engine No 1");
        vehicleDto.setMakeAndModel("Toyota Camry 2024 Hybrid");
        vehicleDto.setMileage(65000);
        vehicleDto.setNote("Give it a bath next time!");
        vehicleDto.setPlateNumber("126 - 629");
        vehicleDto.setVinNo("JX12345678");
        vehicleDto.setVehicleType(VehicleType.PASSENGER_CAR);

        String json = objectMapper.writeValueAsString(vehicleDto);

        this.mockMvc.perform(
                        post("/vehicles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                );

        this.mockMvc.perform(
                        post("/vehicles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Invalid Data")
                );
    }

    @Test
    void updateVehicle_ShouldUpdateVehicle_status200() throws Exception {
        VehicleDto vehicleDto = new VehicleDto();

        vehicleDto.setCustomer(customerDto);
        vehicleDto.setEngineNo("Engine No 1");
        vehicleDto.setMakeAndModel("Toyota Camry 2024 Hybrid");
        vehicleDto.setMileage(65000);
        vehicleDto.setNote("Give it a bath next time!");
        vehicleDto.setPlateNumber("126 - 629");
        vehicleDto.setVinNo("JX12345678");
        vehicleDto.setVehicleType(VehicleType.PASSENGER_CAR);

        String json = objectMapper.writeValueAsString(vehicleDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        vehicleDto.setId(id);
        vehicleDto.setCustomer(customerDto);
        vehicleDto.setEngineNo("Engine No 2");
        vehicleDto.setMakeAndModel("Toyota Camry 2022 Hybrid");
        vehicleDto.setMileage(75000);
        vehicleDto.setNote("Give it an oil change next time!");
        vehicleDto.setPlateNumber("126 - 6294");
        vehicleDto.setVinNo("JX12345679");
        vehicleDto.setVehicleType(VehicleType.COMMERCIAL_VEHICLE);

        json = objectMapper.writeValueAsString(vehicleDto);

        this.mockMvc.perform(
                        put("/vehicles/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.customer.name")
                                .value((vehicleDto.getCustomer().getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.engineNo")
                                .value((vehicleDto.getEngineNo()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.makeAndModel")
                                .value((vehicleDto.getMakeAndModel()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.mileage")
                                .value((vehicleDto.getMileage()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.note")
                                .value((vehicleDto.getNote()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.plateNumber")
                                .value((vehicleDto.getPlateNumber()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.vinNo")
                                .value((vehicleDto.getVinNo()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.vehicleType")
                                .value((vehicleDto.getVehicleType().name()))
                );
    }

    @Test
    void deleteVehicle_ShouldDeleteVehicle_status200() throws Exception {
        VehicleDto vehicleDto = new VehicleDto();

        vehicleDto.setCustomer(customerDto);
        vehicleDto.setEngineNo("Engine No 1");
        vehicleDto.setMakeAndModel("Toyota Camry 2024 Hybrid");
        vehicleDto.setMileage(65000);
        vehicleDto.setNote("Give it a bath next time!");
        vehicleDto.setPlateNumber("126 - 629");
        vehicleDto.setVinNo("JX12345678");
        vehicleDto.setVehicleType(VehicleType.PASSENGER_CAR);

        String json = objectMapper.writeValueAsString(vehicleDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        this.mockMvc.perform(
                        delete("/vehicles/" + id)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.customer.name")
                                .value((vehicleDto.getCustomer().getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.engineNo")
                                .value((vehicleDto.getEngineNo()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.makeAndModel")
                                .value((vehicleDto.getMakeAndModel()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.mileage")
                                .value((vehicleDto.getMileage()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.note")
                                .value((vehicleDto.getNote()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.plateNumber")
                                .value((vehicleDto.getPlateNumber()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.vinNo")
                                .value((vehicleDto.getVinNo()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.vehicleType")
                                .value((vehicleDto.getVehicleType().name()))
                );
    }
}
