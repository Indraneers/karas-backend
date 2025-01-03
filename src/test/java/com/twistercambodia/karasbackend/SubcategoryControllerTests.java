package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
public class SubcategoryControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private JwtDecoder jwtDecoder;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void createCategory_shouldCreateNewSubcategory_status200() throws Exception {
        SubcategoryDto subcategoryDto = new SubcategoryDto();

        subcategoryDto.setName("Engine Oil");

        String json = objectMapper.writeValueAsString(subcategoryDto);

        this.mockMvc.perform(
                        post("/subcategories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((subcategoryDto.getName()))
                );
    }

    @Test
    void createCategory_DuplicateSubcategoryException_Status400() throws Exception {
        SubcategoryDto subcategoryDto = new SubcategoryDto();

        subcategoryDto.setName("Gasoline Engine Oils");

        String json = objectMapper.writeValueAsString(subcategoryDto);

        this.mockMvc.perform(
                post("/subcategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        this.mockMvc.perform(
                        post("/subcategories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value("Invalid Data")
                );
    }

    @Test
    void updateCategory_ShouldUpdateSubcategory_Status200() throws Exception {
        SubcategoryDto subcategoryDto = new SubcategoryDto();

        subcategoryDto.setName("Gasoline Engine Oils");

        String json = objectMapper.writeValueAsString(subcategoryDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/subcategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        subcategoryDto.setId(id);
        subcategoryDto.setName("Transmission Fluid");

        json = objectMapper.writeValueAsString(subcategoryDto);

        this.mockMvc.perform(
                        put("/subcategories/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((subcategoryDto.getName()))
                );
    }

    @Test
    void deleteCategory_ShouldDeleteSubcategory_Status200() throws Exception {
        SubcategoryDto subcategoryDto = new SubcategoryDto();

        subcategoryDto.setName("Gasoline Engine Oils");

        String json = objectMapper.writeValueAsString(subcategoryDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/subcategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        subcategoryDto.setId(id);

        this.mockMvc.perform(
                        delete("/subcategories/" + id)
                )
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value((subcategoryDto.getName()))
                );
    }
}
