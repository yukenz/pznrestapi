package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.entity.Contact;
import com.awan.pznrestapi.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    //Print Response Body
    final private ResultHandler printResult = (result) -> {
        String content = result.getResponse().getContentAsString(Charset.defaultCharset());
        System.out.println(content);
    };

    //Form Login
    final private LoginUserRequest formLogin = LoginUserRequest.builder()
            .username("awan").password("theravian")
            .build();

    //Do Login
    private ResultActions login;

    final String contactId = "99375412-3ca9-4a4a-85fb-b7f1a3ec5d5a";

    @BeforeEach
    void setUp() throws Exception {

        login = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formLogin))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.data.token", Matchers.any(String.class))
        ).andDo(printResult);

    }

    public ContactControllerTest() throws Exception {
    }

    @Test
    void getWithoutLogin() throws Exception {
        mockMvc.perform(
                get("/api/contacts/" + contactId)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
//                jsonPath("$.data.errors", Matchers.any(String.class))
        ).andDo(printResult);
    }

    @Test
    void loginAndGet() throws Exception {

        login.andDo(result -> {

            String content = result.getResponse().getContentAsString(Charset.defaultCharset());

            WebResponse<TokenResponse> webResponse = objectMapper.readValue(content, new TypeReference<WebResponse<TokenResponse>>() {
            });

            //GetFound
            mockMvc.perform(
                    get("/api/contacts/" + contactId)
                            .header("X-API-TOKEN", webResponse.getData().getToken())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(
                    status().isOk(),
                    jsonPath("$.data.id", Matchers.containsString(contactId))
            ).andDo(printResult);//GetFound

            //GetNotFound
            mockMvc.perform(
                    get("/api/contacts/" + 5662)
                            .header("X-API-TOKEN", webResponse.getData().getToken())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(
                    status().isNotFound(),
                    jsonPath("$.errors", Matchers.any(String.class))
            ).andDo(printResult);

        });

    }

    @Test
    void testLoginGetAndUpdate() throws Exception {

        //GetContact
        login.andDo(result -> {

            String content = result.getResponse().getContentAsString(Charset.defaultCharset());
            WebResponse<TokenResponse> webResponse = objectMapper.readValue(content, new TypeReference<WebResponse<TokenResponse>>() {
            });

            //GetFound
            ResultActions getContact = mockMvc.perform(
                    get("/api/contacts/" + contactId)
                            .header("X-API-TOKEN", webResponse.getData().getToken())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(
                    status().isOk(),
                    jsonPath("$.data.id", Matchers.containsString(contactId))
            ).andDo(printResult);

            /*Do After Get Contact*/
            getContact.andDo(resultC -> {
                String contentC = resultC.getResponse().getContentAsString();
                ContactResponse contactResponse = objectMapper.readValue(contentC, new TypeReference<WebResponse<ContactResponse>>() {
                }).getData();

                UpdateContactRequest form = UpdateContactRequest.builder()
                        .firstName(contactResponse.getFirstName())
                        .lastName(contactResponse.getLastName())
                        .email(contactResponse.getEmail())
                        .phone(contactResponse.getPhone())
                        .build();

                mockMvc.perform(
                        put("/api/contacts/" + contactId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(form))
                                .header("X-API-TOKEN", webResponse.getData().getToken())
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpectAll(
                        status().isOk(),
                        jsonPath("$.data", Matchers.containsString("OK"))
                ).andDo(printResult);

            });
        });


    }

    @Test
    void testLoginGetAndDelete() throws Exception {

        login.andDo(result -> {

                    String content = result.getResponse().getContentAsString(Charset.defaultCharset());
                    WebResponse<TokenResponse> webResponse = objectMapper.readValue(content, new TypeReference<WebResponse<TokenResponse>>() {
                    });

                    //Get Contact
                    ResultActions getContact = mockMvc.perform(
                            get("/api/contacts/" + contactId)
                                    .header("X-API-TOKEN", webResponse.getData().getToken())
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpectAll(
                            status().isOk(),
                            jsonPath("$.data.id", Matchers.containsString(contactId))
                    ).andDo(printResult);

                    //Delete Contact
                    getContact.andDo(resultContact -> {
                        String contentResultContact = resultContact.getResponse().getContentAsString();

                        ContactResponse contactResponse = objectMapper.readValue(contentResultContact, new TypeReference<WebResponse<ContactResponse>>() {
                        }).getData();

                        //Delete Success
                        mockMvc.perform(
                                delete("/api/contacts/" + contactResponse.getId())
                                        .header("X-API-TOKEN", webResponse.getData().getToken())
                                        .accept(MediaType.APPLICATION_JSON)
                        ).andExpectAll(
                                status().isOk(),
                                jsonPath("$.data", Matchers.containsString("OK"))
                        );

                        //Delete Failed
                        mockMvc.perform(
                                delete("/api/contacts/" + contactResponse.getId())
                                        .header("X-API-TOKEN", webResponse.getData().getToken())
                                        .accept(MediaType.APPLICATION_JSON)
                        ).andExpectAll(
                                status().isNotFound(),
                                jsonPath("$.errors", Matchers.any(String.class))
                        );

                    });
                }
        );

    }
}
