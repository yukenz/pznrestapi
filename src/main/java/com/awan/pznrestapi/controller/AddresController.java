package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.AddressResponse;
import com.awan.pznrestapi.model.CreateAddressRequest;
import com.awan.pznrestapi.model.UpdateAddressRequest;
import com.awan.pznrestapi.model.WebResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public interface AddresController {

    @PostMapping(
            path = "/api/contacts/{idContact}/addresses",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    WebResponse<AddressResponse> create(
            User user,
            @PathVariable(name = "idContact") String idContact,
            @RequestBody CreateAddressRequest form
    );

    @GetMapping(
            path = "/api/contacts/{idContact}/addresses/{idAddress}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<AddressResponse> get(
            User user,
            @PathVariable(name = "idContact") String idContact,
            @PathVariable(name = "idAddress") String idAddress
    );

    @PatchMapping(
            path = "/api/contacts/{idContact}/addresses/{idAddress}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<String> update(
            User user,
            @PathVariable(name = "idContact") String idContact,
            @PathVariable(name = "idAddress") String idAddress,
            @RequestBody UpdateAddressRequest form
    );

    @DeleteMapping(
            path = "/api/contacts/{idContact}/addresses/{idAddress}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<String> delete(
            User user,
            @PathVariable(name = "idContact") String idContact,
            @PathVariable(name = "idAddress") String idAddress
    );

    @GetMapping(
            path = "/api/contacts/{idContact}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<List<AddressResponse>> getAll(
            User user,
            @PathVariable(name = "idContact") String idContact
    );


}
