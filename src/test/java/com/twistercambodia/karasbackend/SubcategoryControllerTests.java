package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryRequestDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
@TestPropertySource(locations="classpath:application.properties")
public class SubcategoryControllerTests {
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

    private CategoryDto categoryDto;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.objectMapper = new ObjectMapper();

        categoryDto = new CategoryDto();
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
    }

    @Test
    public void createSubcategory_shouldCreateNewSubcategory_status200() throws Exception {
        SubcategoryRequestDto subcategoryRequestDto = new SubcategoryRequestDto();

        subcategoryRequestDto.setName("Gasoline Engine Oil");
        subcategoryRequestDto.setCategoryId(categoryDto.getId());

        String json = objectMapper.writeValueAsString(subcategoryRequestDto);
        MockMultipartFile file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );

        this.mockMvc.perform(
                        multipart("/subcategories")
                                .file(file)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((subcategoryRequestDto.getName()))
                );

        this.mockMvc.perform(
                        get("/categories/" + categoryDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.subcategoryCount")
                                .value(1)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.subcategories[0].name")
                                .value(subcategoryRequestDto.getName())
                );

        // check if subcategory exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/subcategory?page=0")
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
                                .value("Subcategory Creation")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("POST")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("SUBCATEGORY")
                );
    }

    @Test
    void createSubcategory_DuplicateSubcategoryException_Status400() throws Exception {
        SubcategoryRequestDto subcategoryRequestDto = new SubcategoryRequestDto();

        subcategoryRequestDto.setName("Gasoline Engine Oil");
        subcategoryRequestDto.setCategoryId(categoryDto.getId());

        String json = objectMapper.writeValueAsString(subcategoryRequestDto);
        MockMultipartFile file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );

        this.mockMvc.perform(
                multipart("/subcategories")
                        .file(file)
        );

        this.mockMvc.perform(
                        multipart("/subcategories")
                                .file(file)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value("Invalid Data")
                );

        this.mockMvc.perform(
                        get("/categories/" + categoryDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.subcategoryCount")
                                .value(1)
                );
    }

    @Test
    void updateSubcategory_ShouldUpdateSubcategory_Status200() throws Exception {
        SubcategoryRequestDto subcategoryRequestDto = new SubcategoryRequestDto();

        subcategoryRequestDto.setName("Gasoline Engine Oil");
        subcategoryRequestDto.setCategoryId(categoryDto.getId());

        String json = objectMapper.writeValueAsString(subcategoryRequestDto);
        MockMultipartFile file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );

        MvcResult mvcResult = this.mockMvc.perform(
                multipart("/subcategories")
                        .file(file)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        subcategoryRequestDto.setId(id);
        subcategoryRequestDto.setName("Diesel Engine Oil");

        json = objectMapper.writeValueAsString(subcategoryRequestDto);
        file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );


        this.mockMvc.perform(
                        multipart("/subcategories/" + id)
                                .file(file)
                                .with(req -> { req.setMethod("PUT"); return req; })
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((subcategoryRequestDto.getName()))
                );

        this.mockMvc.perform(
                        get("/categories/" + categoryDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.subcategoryCount")
                                .value(1)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.subcategories[0].name")
                                .value(subcategoryRequestDto.getName())
                );

        // check if subcategory exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/subcategory?page=0")
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
                                .value("Subcategory Update")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("PUT")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("SUBCATEGORY")
                );
    }

    @Test
    void deleteSubcategory_ShouldDeleteSubcategory_Status200() throws Exception {
        SubcategoryRequestDto subcategoryRequestDto = new SubcategoryRequestDto();

        subcategoryRequestDto.setName("Gasoline Engine Oil");
        subcategoryRequestDto.setCategoryId(categoryDto.getId());

        String json = objectMapper.writeValueAsString(subcategoryRequestDto);
        MockMultipartFile file = new MockMultipartFile(
                "data",
                json,
                String.valueOf(MediaType.APPLICATION_JSON),
                json.getBytes()
        );

        MvcResult mvcResult = this.mockMvc.perform(
                multipart("/subcategories")
                        .file(file)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        subcategoryRequestDto.setId(id);

        this.mockMvc.perform(
                        delete("/subcategories/" + id)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((subcategoryRequestDto.getName()))
                );

        this.mockMvc.perform(
                        get("/categories/" + categoryDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.subcategoryCount")
                                .value(0)
                );

        // check if subcategory exists in audit
        this.mockMvc.perform(
                        get("/audits/audit-service/subcategory?page=0")
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
                                .value("Subcategory Deletion")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].httpMethod")
                                .value("DELETE")
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].service")
                                .value("SUBCATEGORY")
                );
    }
}
