package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.ContactResponse;
import com.awan.pznrestapi.model.CreateContactRequest;
import com.awan.pznrestapi.model.UpdateContactRequest;
import com.awan.pznrestapi.model.WebResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public interface ContactController {

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<ContactResponse> create(
            User user,
            @RequestBody CreateContactRequest form
    );

    @GetMapping(
            path = "/api/contacts/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<ContactResponse> get(
            User user,
            @PathVariable String id
    );

    @GetMapping(
            path = "/api/contacts",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<List<ContactResponse>> search(
            User user,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    );

    @PutMapping(
            path = "/api/contacts/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<String> update(
            User user,
            @PathVariable String id,
            @RequestBody UpdateContactRequest form
    );

    @DeleteMapping(
            path = "/api/contacts/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<String> delete(
            User user,
            @PathVariable String id
    );

}
