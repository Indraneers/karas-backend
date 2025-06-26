package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.UserRole;
import com.twistercambodia.karasbackend.config.TestSecurityConfig;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.storage.config.MinioConfig;
import com.twistercambodia.karasbackend.storage.service.StorageService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
@TestPropertySource(locations="classpath:application.properties")
public class CustomerControllerTests {
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

    private UserDto userDto;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
        this.objectMapper = new ObjectMapper();

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
    }

    @Test
    void createCustomer_ShouldReturnNewCustomer_Status200() throws Exception {
        CustomerDto customerDto = new CustomerDto();

        customerDto.setName("Car Person A");
        customerDto.setNote("Give them a discount next time!");

        String json = objectMapper.writeValueAsString(customerDto);

        this.mockMvc.perform(
                post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((customerDto.getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.note")
                                .value((customerDto.getNote()))
                );

        // check if customer exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/customer?page=0")
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
                                .value("Customer Creation")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("POST")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("CUSTOMER")
                );
    }

    @Test
    void createCustomer_DuplicateCustomerException_Status400() throws Exception {
        CustomerDto customerDto = new CustomerDto();

        customerDto.setName("Car Person A");
        customerDto.setNote("Give them a discount next time!");

        String json = objectMapper.writeValueAsString(customerDto);

        this.mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        );

        this.mockMvc.perform(
                        post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value("Invalid Data")
                );
    }

    @Test
    void updateCustomer_ShouldUpdateCustomer_Status200() throws Exception {
        CustomerDto customerDto = new CustomerDto();

        customerDto.setName("Car Person A");
        customerDto.setNote("Give them a discount next time!");

        String json = objectMapper.writeValueAsString(customerDto);

       MvcResult mvcResult = this.mockMvc.perform(
                        post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                ).andReturn();

       String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        customerDto.setId(id);
        customerDto.setName("Car Person B");
        json = objectMapper.writeValueAsString(customerDto);

        this.mockMvc.perform(
                        put("/customers/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((customerDto.getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.note")
                                .value((customerDto.getNote()))
                );

        // check if customer exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/customer?page=0")
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
                                .value("Customer Update")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("PUT")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("CUSTOMER")
                );
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer_Status200() throws Exception {
        CustomerDto customerDto = new CustomerDto();

        customerDto.setName("Car Person A");
        customerDto.setNote("Give them a discount next time!");

        String json = objectMapper.writeValueAsString(customerDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        customerDto.setId(id);

        this.mockMvc.perform(
                        delete("/customers/" + id)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((customerDto.getName()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.note")
                                .value((customerDto.getNote()))
                );

        // check if customer exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/customer?page=0")
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
                                .value("Customer Deletion")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("DELETE")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("CUSTOMER")
                );
    }
}
