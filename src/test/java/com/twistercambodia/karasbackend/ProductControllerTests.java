package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.UserRole;
import com.twistercambodia.karasbackend.config.TestSecurityConfig;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductRequestDto;
import com.twistercambodia.karasbackend.storage.config.MinioConfig;
import com.twistercambodia.karasbackend.storage.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
@TestPropertySource(locations="classpath:application.properties")
public class ProductControllerTests {
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

    private SubcategoryRequestDto subcategoryRequestDto;

    private UserDto userDto;

    @Value("${minio.url}")
    private String minioUrl;

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
    }

    @Test
    void createProduct_shouldReturnNewProduct_status200() throws Exception {
        ProductRequestDto productRequestDto = new ProductRequestDto();

        productRequestDto.setName("Twister Engine Oil A");
        productRequestDto.setSubcategoryId(this.subcategoryRequestDto.getId());

        String json = objectMapper.writeValueAsString(productRequestDto);
        MockMultipartFile file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );

        this.mockMvc.perform(
                        multipart("/products")
                                .file(file)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.subcategory.id")
                                .value((productRequestDto.getSubcategoryId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((productRequestDto.getName()))
                );

        this.mockMvc.perform(
                get("/subcategories/" + subcategoryRequestDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
        )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productCount")
                                .value(1)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.products[0].name")
                                .value(productRequestDto.getName())
                );

        // check if product exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/product?page=0")
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
                                .value("Product Creation")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("POST")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("PRODUCT")
                );
    }

    @Test
    void updateProduct_ShouldUpdateProduct_status200() throws Exception {
        ProductRequestDto productRequestDto = new ProductRequestDto();

        productRequestDto.setName("Twister Engine Oil A");
        productRequestDto.setSubcategoryId(this.subcategoryRequestDto.getId());

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
        productRequestDto.setName("Twister Engine Oil B");

        json = objectMapper.writeValueAsString(productRequestDto);

        file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );

        this.mockMvc.perform(
                        multipart("/products/" + id)
                                .file(file)
                                .with(req -> { req.setMethod("PUT"); return req; })
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.subcategory.id")
                                .value((productRequestDto.getSubcategoryId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((productRequestDto.getName()))
                );

        // check if product exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/product?page=0")
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
                                .value("Product Update")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("PUT")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("PRODUCT")
                );
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_status200() throws Exception {
        ProductRequestDto productRequestDto = new ProductRequestDto();

        productRequestDto.setName("Twister Engine Oil A");
        productRequestDto.setSubcategoryId(this.subcategoryRequestDto.getId());

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

        this.mockMvc.perform(
                        delete("/products/" + id)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.subcategory.id")
                                .value((productRequestDto.getSubcategoryId()))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((productRequestDto.getName()))
                );

        this.mockMvc.perform(
                        get("/subcategories/" + subcategoryRequestDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productCount")
                                .value(0)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.products")
                                .isEmpty()
                );

        // check if product exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/product?page=0")
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
                                .value("Product Deletion")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("DELETE")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("PRODUCT")
                );
    }

//    @Test
//    void createAndUpdateProduct_ShouldUploadImage_status200() throws Exception {
//        ProductRequestDto productRequestDto = new ProductRequestDto();
//
//        productRequestDto.setName("Twister Engine Oil A");
//        productRequestDto.setSubcategoryId(this.subcategoryRequestDto.getId());
//
//        String json = objectMapper.writeValueAsString(productRequestDto);
//        MockMultipartFile data = new MockMultipartFile(
//                "data",
//                json,
//                String.valueOf(MediaType.APPLICATION_JSON),
//                json.getBytes()
//        );
//
//        // assert image existence
//        ClassPathResource imageResource = new ClassPathResource("256x256.jpg");
//        byte[] imageBytes = StreamUtils.copyToByteArray(imageResource.getInputStream());
//
//        assertNotNull(imageBytes);
//        assertTrue(imageBytes.length > 0);
//
//        // put it in a file
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "256x256.jpg",
//                "image/jpeg",
//                getClass().getClassLoader().getResourceAsStream("256x256.jpg")
//        );
//
//        // send post request
//        json = this.mockMvc.perform(
//                multipart("/products")
//                        .file(file)
//                        .file(data)
//                        .with(TestSecurityConfig.testJwt(userDto.getId(), "USER", "ADMIN"))
//        )
//                .andExpect(status().isOk())
//                .andExpect(
//                        MockMvcResultMatchers.jsonPath("$.subcategory.id")
//                                .value((productRequestDto.getSubcategoryId()))
//                )
//                .andExpect(
//                        MockMvcResultMatchers.jsonPath("$.name")
//                                .value((productRequestDto.getName()))
//                )
//                .andExpect(
//                        MockMvcResultMatchers.jsonPath("$.img").isNotEmpty()
//                )
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        String imageUrl = minioUrl + JsonPath.read(json, "$.img");
//
//        System.out.println(imageUrl);
//
//        // Then fetch the actual image
//        byte[] downloadedImage = mockMvc.perform(get(imageUrl))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
//                .andReturn()
//                .getResponse()
//                .getContentAsByteArray();
//
//        // Compare with original
//        byte[] originalImage = new ClassPathResource("256x256.jpg").getInputStream().readAllBytes();
//        assertArrayEquals(originalImage, downloadedImage);
//    }
}
