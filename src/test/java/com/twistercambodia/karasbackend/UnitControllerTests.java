package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.UserRole;
import com.twistercambodia.karasbackend.config.TestSecurityConfig;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitRequestDto;
import com.twistercambodia.karasbackend.storage.config.MinioConfig;
import com.twistercambodia.karasbackend.storage.service.StorageService;
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
public class UnitControllerTests {
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

    SubcategoryRequestDto subcategoryRequestDto;

    private ProductRequestDto productRequestDto;
    
    private UserDto userDto;

    @BeforeEach
    public void setup() throws Exception {
        this.objectMapper = new ObjectMapper();
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
        ).andReturn();

        id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

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
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andReturn();

        id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        subcategoryRequestDto.setId(id);

        productRequestDto = new ProductRequestDto();

        productRequestDto.setSubcategoryId(subcategoryRequestDto.getId());
        productRequestDto.setName("Twister Engine Oil A");

        String productDtoJson = objectMapper.writeValueAsString(productRequestDto);
        file = new MockMultipartFile(
                "data",
                productDtoJson,
                String.valueOf(MediaType.APPLICATION_JSON),
                productDtoJson.getBytes()
        );

        mvcResult = this.mockMvc.perform(
                multipart("/products")
                        .file(file)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
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
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
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
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.unitCount")
                                .value(1)
                );

        // check if subcategory exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/unit?page=0")
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
                                .value("Unit Creation")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("POST")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("UNIT")
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
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        unitRequestDto.setId(id);
        unitRequestDto.setName("2L");
        unitRequestDto.setProductId(this.productRequestDto.getId());
        unitRequestDto.setToBaseUnit(1);
        unitRequestDto.setPrice(200);
        unitRequestDto.setQuantity(25);

        json = objectMapper.writeValueAsString(unitRequestDto);

        this.mockMvc.perform(
                        put("/units/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
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

        // check if unit exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/unit?page=0")
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
                                .value("Unit Update")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("PUT")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("UNIT")
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
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        this.mockMvc.perform(
                        delete("/units/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
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
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.unitCount")
                                .value(0)
                );

        // check if unit exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/unit?page=0")
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
                                .value("Unit Deletion")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("DELETE")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("UNIT")
                );
    }
}
