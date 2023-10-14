package com.awan.pznrestapi.service;

import com.awan.pznrestapi.entity.Address;
import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.AddressResponse;
import com.awan.pznrestapi.model.CreateAddressRequest;
import com.awan.pznrestapi.model.UpdateAddressRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface AddressService {

    @Transactional
    Address create(
            User user,
            CreateAddressRequest form
    );

    @Transactional
    void update(User user, UpdateAddressRequest form);

    @Transactional(readOnly = true)
    Address get(User user, String idContact, String idAddress);

    @Transactional(readOnly = true)
    List<AddressResponse> getAll(User user, String idContact);

    @Transactional()
    void delete(User user, String idContact, String idAddress);


}
