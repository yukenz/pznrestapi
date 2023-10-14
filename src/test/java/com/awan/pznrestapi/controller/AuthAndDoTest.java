package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.*;
import com.awan.pznrestapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthAndDoTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testGetWithoutToken() throws Exception {

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized(),
                jsonPath("$.errors", Matchers.any(String.class))
        ).andDo(result -> {

            String rawBodyResponse = result.getResponse().getContentAsString();
            WebResponse webResponse = objectMapper.readValue(rawBodyResponse, new TypeReference<WebResponse>() {
            });

            System.out.println(rawBodyResponse);

        });

    }

    @Test
    void testGetWithNotFoundToken() throws Exception {

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "notfound")
        ).andExpectAll(
                status().isUnauthorized(),
                jsonPath("$.errors", Matchers.any(String.class))
        ).andDo(result -> {

            String rawBodyResponse = result.getResponse().getContentAsString();
            WebResponse webResponse = objectMapper.readValue(rawBodyResponse, new TypeReference<WebResponse>() {
            });

            System.out.println(rawBodyResponse);

        });

    }

    @Test
    void testAuthAndGet() throws Exception {

        LoginUserRequest form = LoginUserRequest.builder()
                .username("awan").password("theravian")
                .build();

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.data.token", Matchers.any(String.class))
        ).andDo(resultLogin -> {

            String resultLoginRawBody = resultLogin.getResponse().getContentAsString();

            WebResponse<TokenResponse> bodyResponseResultLogin = objectMapper.readValue(resultLoginRawBody, new TypeReference<WebResponse<TokenResponse>>() {
            });

            mockMvc.perform(
                    get("/api/users/current")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-API-TOKEN", bodyResponseResultLogin.getData().getToken())
            ).andExpectAll(
                    status().isOk(),
                    jsonPath("$.data.username", Matchers.any(String.class)),
                    jsonPath("$.data.name", Matchers.any(String.class)),
                    jsonPath("$.data.tokenExpiredAt", Matchers.any(Number.class))
            ).andDo(resultGetCurrentUser -> {

                String getCurrentUserRawBody = resultGetCurrentUser.getResponse().getContentAsString();

                UserResponse userResponse = objectMapper.readValue(getCurrentUserRawBody, new TypeReference<WebResponse<UserResponse>>() {
                }).getData();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm.SSS");
                Date tokenExpiredDateTime = new Date(userResponse.getTokenExpiredAt());
                String timeFormatReady = sdf.format(tokenExpiredDateTime);

                System.out.println(timeFormatReady);

                Assertions.assertEquals("Yuyun Purniawan", userResponse.getName());
                Assertions.assertEquals("awan", userResponse.getUsername());


            });
        });

    }

    @Test
    void testGetWithTokenExpired() throws Exception {

        //Setup Token Invalid Now-60m
        User user = userRepository.findById("awan")
                .orElseThrow(() -> new TestAbortedException("User not found"));

        long nowMillisMinus60m = System.currentTimeMillis() - Duration.of(60, ChronoUnit.MINUTES).toMillis();

        user.setTokenExpired(nowMillisMinus60m);
        userRepository.save(user);

        //Perform
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
                status().isUnauthorized(),
                jsonPath("$.errors", Matchers.containsString("Durasi Token Tidak Valid"))
        ).andDo(resultGetCurrentUser -> {

            System.out.println(resultGetCurrentUser.getResponse().getContentAsString());

        });
    }

    @Test
    void testAuthAndUpdate() throws Exception {
        LoginUserRequest formLogin = LoginUserRequest.builder()
                .username("awan").password("theravian").build();


        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formLogin))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.data.token", Matchers.any(String.class)),
                jsonPath("$.data.expiredAt", Matchers.any(Number.class))
        ).andDo(authResult -> {

            String contentAsString = authResult.getResponse().getContentAsString();
            WebResponse<TokenResponse> webResponse = objectMapper.readValue(contentAsString, new TypeReference<WebResponse<TokenResponse>>() {
            });

            UpdateUserRequest formUpdate = new UpdateUserRequest().builder()
                    .name("Yuyun Purniawan")
                    .build();

            mockMvc.perform(
                    patch("/api/users/current")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(formUpdate))
                            .header("X-API-TOKEN", webResponse.getData().getToken())
            ).andExpectAll(
                    status().isOk(),
                    jsonPath("$.data", Matchers.containsString("OK"))
            ).andDo(updateResult -> System.out.println(updateResult.getResponse().getContentAsString()));

        });
    }

    @Test
    void testUpdateWithoutAuth() throws Exception {

        UpdateUserRequest formUpdate = new UpdateUserRequest().builder()
                .name("Yuyun Purniawan")
                .build();

        mockMvc.perform(
                patch("/api/users/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formUpdate))
        ).andExpectAll(
                status().isUnauthorized(),
                jsonPath("$.errors", Matchers.any(String.class))
        ).andDo(updateResult -> System.out.println(updateResult.getResponse().getContentAsString()));


    }

    @Test
    void testAuthAndCreateContact() throws Exception {

        LoginUserRequest formLogin = LoginUserRequest.builder()
                .username("awan").password("theravian")
                .build();

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formLogin))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.data.token", Matchers.any(String.class))
        ).andDo(resultLogin -> {

            String resultLoginRawBody = resultLogin.getResponse().getContentAsString();

            WebResponse<TokenResponse> bodyResponseResultLogin = objectMapper.readValue(resultLoginRawBody, new TypeReference<WebResponse<TokenResponse>>() {
            });

            CreateContactRequest formCreateContact = CreateContactRequest.builder()
                    .email("yuyun.purniawan@gmail.com")
                    .firstName("Yuyun")
                    .lastName("Purniawan")
                    .phone("+6285156107536")
                    .build();

            mockMvc.perform(
                    post("/api/contacts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-API-TOKEN", bodyResponseResultLogin.getData().getToken())
                            .content(objectMapper.writeValueAsString(formCreateContact))
            ).andExpectAll(
                    status().isOk()
            );

        });

    }

    @Test
    void testCreateContactWithoutAuth() throws Exception {

        CreateContactRequest formCreateContact = CreateContactRequest.builder()
                .email("yuyun.purniawan@gmail.com")
                .firstName("Yuyun")
                .lastName("Purniawan")
                .phone("+6285156107536")
                .build();

        mockMvc.perform(
                post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formCreateContact))
        ).andExpectAll(
                status().isUnauthorized()
        );
    }
}
