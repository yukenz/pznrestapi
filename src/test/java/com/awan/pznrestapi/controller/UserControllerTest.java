package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.model.RegisterUserRequest;
import com.awan.pznrestapi.model.WebResponse;
import com.awan.pznrestapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Order(1)
    @Test
    public void firstOfAll() {
        userRepository.deleteAll();
    }

    @Order(2)
    @Test
    void testRegisterBlank() throws Exception {

        RegisterUserRequest form = new RegisterUserRequest().builder()
                .build();

        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form))
        ).andExpectAll(
                status().isUnprocessableEntity(),
                jsonPath("$.errors", Matchers.any(String.class))
        ).andDo(result -> {
            String contentBody = result.getResponse().getContentAsString();
            WebResponse webResponse = objectMapper.readValue(contentBody, WebResponse.class);
            String[] violations = webResponse.getErrors().split(",");
            assertEquals(3, violations.length);

        });

    }

    @Test
    @Order(3)
    void testRegisterSuccessAndAntiDup() throws Exception {

        RegisterUserRequest form = new RegisterUserRequest().builder()
                .username("awan")
                .password("theravian")
                .name("Yuyun Purniawan")
                .build();

        //Success Perform
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.data", Matchers.any(String.class))
        ).andDo(result -> {
            String contentBody = result.getResponse().getContentAsString();
            WebResponse webResponse = objectMapper.readValue(contentBody, WebResponse.class);
            assertEquals("OK", webResponse.getData());
        });


        //Duplicate Perform
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form))
        ).andExpectAll(
                status().isUnprocessableEntity(),
                jsonPath("$.errors", Matchers.any(String.class))
        ).andDo(result -> {
            String contentBody = result.getResponse().getContentAsString();
            WebResponse webResponse = objectMapper.readValue(contentBody, WebResponse.class);
            assertEquals("422 UNPROCESSABLE_ENTITY \"Username sudah ada\"", webResponse.getErrors());
        });

    }

}
