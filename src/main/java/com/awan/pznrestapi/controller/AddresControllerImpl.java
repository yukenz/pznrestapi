package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.entity.Address;
import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.AddressResponse;
import com.awan.pznrestapi.model.CreateAddressRequest;
import com.awan.pznrestapi.model.UpdateAddressRequest;
import com.awan.pznrestapi.model.WebResponse;
import com.awan.pznrestapi.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddresControllerImpl implements AddresController {

    @Autowired
    private AddressService addressService;

    @Override
    public WebResponse<AddressResponse> create(User user, String idContact, CreateAddressRequest form) {

        form.setIdContact(idContact);
        Address address = addressService.create(user, form);

        //Entity to Response
        AddressResponse addressResponse = AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();

        return WebResponse.<AddressResponse>builder()
                .data(addressResponse)
                .build();

    }

    @Override
    public WebResponse<AddressResponse> get(User user, String idContact, String idAddress) {

        Address address = addressService.get(user, idContact, idAddress);

        return WebResponse.<AddressResponse>builder()
                .data(AddressResponse.builder()
                        .id(address.getId())
                        .street(address.getStreet())
                        .city(address.getCity())
                        .province(address.getProvince())
                        .country(address.getCountry())
                        .postalCode(address.getPostalCode())
                        .build())
                .build();
    }

    @Override
    public WebResponse<String> update(User user, String idContact, String idAddress, UpdateAddressRequest form) {

        form.setId(idAddress);
        form.setIdContact(idContact);
        addressService.update(user, form);

        return WebResponse.<String>builder().data("OK").build();
    }

    @Override
    public WebResponse<String> delete(User user, String idContact, String idAddress) {
        addressService.delete(user, idContact, idAddress);
        return WebResponse.<String>builder().data("OK").build();

    }

    @Override
    public WebResponse<List<AddressResponse>> getAll(User user, String idContact) {

        List<AddressResponse> addresses = addressService.getAll(user, idContact);

        return WebResponse.<List<AddressResponse>>builder()
                .data(addresses)
                .build();
    }
}
