package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.model.LoginUserRequest;
import com.awan.pznrestapi.model.TokenResponse;
import com.awan.pznrestapi.model.WebResponse;
import com.awan.pznrestapi.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginAndLogout() throws Exception {

        LoginUserRequest form = LoginUserRequest.builder()
                .username("awan").password("theravian").build();


        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.data.token", Matchers.any(String.class)),
                jsonPath("$.data.expiredAt", Matchers.any(Number.class))
        ).andDo(result -> {

            String responseContentAuth = result.getResponse().getContentAsString();

            WebResponse<TokenResponse> authResponse = objectMapper.readValue(responseContentAuth, new TypeReference<WebResponse<TokenResponse>>() {
            });

            mockMvc.perform(
                    delete("/api/auth/logout")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-API-TOKEN", authResponse.getData().getToken())
            ).andExpectAll(
                    status().isOk(),
                    jsonPath("$.data", Matchers.any(String.class))
            );

        });

    }

    @Test
    void logoutWithoutToken() throws Exception {

        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized(),
                jsonPath("$.errors", Matchers.any(String.class))
        ).andDo(result -> System.out.println(result.getResponse().getContentAsString()));

    }

}
