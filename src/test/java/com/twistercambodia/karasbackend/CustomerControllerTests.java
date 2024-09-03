package com.twistercambodia.karasbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twistercambodia.karasbackend.configuration.MockConfig;
import com.twistercambodia.karasbackend.customer.controller.CustomerController;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.repository.CustomerRepository;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KarasBackendApplication.class})
@WebAppConfiguration
public class CustomerControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
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
    }
}
