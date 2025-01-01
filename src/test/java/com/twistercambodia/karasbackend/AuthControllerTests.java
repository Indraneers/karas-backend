package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.auth.entity.UserRole;
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

import javax.print.attribute.standard.Media;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin", roles={"USER", "ADMIN"})
public class AuthControllerTests {
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
    public void createUser_shouldCreateNewUser_status200() throws Exception {
        UserDto userDto = new UserDto();

        userDto.setUsername("User A");
        userDto.setRole(UserRole.ADMIN);

        String json = objectMapper.writeValueAsString(userDto);

        this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isOk())
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.username")
                        .value(userDto.getUsername()),
                        MockMvcResultMatchers.jsonPath("$.role")
                        .value(userDto.getRole().toString())
                );
    }

    @Test
    void createUser_DuplicateUserException_Status400() throws Exception {
        UserDto userDto = new UserDto();

        userDto.setUsername("User A");
        userDto.setRole(UserRole.ADMIN);

        String json = objectMapper.writeValueAsString(userDto);

        this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        this.mockMvc.perform(
                        post("/users")
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
    void createUser_EmptyUserRoleException_Status400() throws Exception {
        UserDto userDto = new UserDto();

        userDto.setUsername("User A");

        String json = objectMapper.writeValueAsString(userDto);

        this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value("Invalid Data")
                );    }

    @Test
    void updateUser_ShouldUpdateUser_Status200() throws Exception {
        UserDto userDto = new UserDto();

        userDto.setUsername("User A");
        userDto.setRole(UserRole.ADMIN);

        String json = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        userDto.setId(id);
        userDto.setUsername("User B");

        json = objectMapper.writeValueAsString(userDto);

        this.mockMvc.perform(
                        put("/users/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.username")
                                .value(userDto.getUsername()),
                        MockMvcResultMatchers.jsonPath("$.role")
                                .value(userDto.getRole().toString())
                );
    }

    @Test
    void deleteUser_ShouldDeleteUser_Status200() throws Exception {
        UserDto userDto = new UserDto();

        userDto.setUsername("User A");
        userDto.setRole(UserRole.ADMIN);

        String json = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        userDto.setId(id);

        this.mockMvc.perform(
                        delete("/users/" + id)
                )
                .andExpect(status().isOk())
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.username")
                                .value(userDto.getUsername()),
                        MockMvcResultMatchers.jsonPath("$.role")
                                .value(userDto.getRole().toString())
                );
    }


}
