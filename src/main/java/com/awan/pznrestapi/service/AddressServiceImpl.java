package com.awan.pznrestapi.service;

import com.awan.pznrestapi.entity.Address;
import com.awan.pznrestapi.entity.Contact;
import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.AddressResponse;
import com.awan.pznrestapi.model.CreateAddressRequest;
import com.awan.pznrestapi.model.UpdateAddressRequest;
import com.awan.pznrestapi.repository.AddressRepository;
import com.awan.pznrestapi.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidatorService validatorService;

    @Override
    public Address create(User user, CreateAddressRequest form) {


        //Throw violations if check fail
        validatorService.validateObject(form);

        //Is idContact found?
        Contact contact = contactRepository.findById(form.getIdContact())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact tidak ditemukan"));

        Address address = Address.builder()
                .contact(contact)
                .id(UUID.randomUUID().toString())
                .street(form.getStreet())
                .city(form.getCity())
                .province(form.getProvince())
                .country(form.getCountry())
                .postalCode(form.getPostalCode())
                .build();

        return addressRepository.save(address);

    }

    @Override
    public void update(User user, UpdateAddressRequest form) {

        //Find Contact
        Contact contact = contactRepository.findFirstByUserAndId(user, form.getIdContact())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Tidak Ditemukan"));

        //Find Address
        Address address = addressRepository.findFirstByContactAndId(contact, form.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address Tidak Ditemukan"));


        //Update Field

        if (Objects.nonNull(form.getStreet())) {
            address.setStreet(form.getStreet());
        }

        if (Objects.nonNull(form.getCity())) {
            address.setCity(form.getCity());
        }

        if (Objects.nonNull(form.getProvince())) {
            address.setProvince(form.getProvince());
        }

        if (Objects.nonNull(form.getCountry())) {
            address.setCountry(form.getCountry());
        }

        if (Objects.nonNull(form.getPostalCode())) {
            address.setPostalCode(form.getPostalCode());
        }

        addressRepository.save(address);

    }

    @Override
    public Address get(User user, String idContact, String idAddress) {

        //Find Contact
        Contact contact = contactRepository.findFirstByUserAndId(user, idContact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Tidak Ditemukan"));

        //Find Address
        Address address = addressRepository.findFirstByContactAndId(contact, idAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address Tidak Ditemukan"));

        return address;
    }

    @Override
    public List<AddressResponse> getAll(User user, String idContact) {

        //Find Contact
        Contact contact = contactRepository.findFirstByUserAndId(user, idContact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Tidak Ditemukan"));

        //findAllAddressAndMap
        List<AddressResponse> addresses
                = addressRepository.findAllByContact(contact)
                .stream().map(address -> AddressResponse.builder()
                        .id(address.getId())
                        .street(address.getStreet())
                        .city(address.getCity())
                        .province(address.getProvince())
                        .country(address.getCountry())
                        .postalCode(address.getPostalCode())
                        .build()
                ).toList();

        return addresses;

    }

    @Override
    public void delete(User user, String idContact, String idAddress) {

        //Find Contact
        Contact contact = contactRepository.findFirstByUserAndId(user, idContact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Tidak Ditemukan"));

        //Find Address
        Address address = addressRepository.findFirstByContactAndId(contact, idAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address Tidak Ditemukan"));

        addressRepository.delete(address);
    }


}
