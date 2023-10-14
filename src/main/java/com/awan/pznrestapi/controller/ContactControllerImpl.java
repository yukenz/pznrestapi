package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.*;
import com.awan.pznrestapi.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContactControllerImpl implements ContactController {


    @Autowired
    private ContactService contactService;

    @Override
    public WebResponse<ContactResponse> create(User user, CreateContactRequest form) {

        ContactResponse contactResponse = contactService.create(user, form);

        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    @Override
    public WebResponse<ContactResponse> get(User user, String id) {

        ContactResponse contactResponse = contactService.get(user, id);

        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    @Override
    public WebResponse<List<ContactResponse>> search(User user, String name, String phone, String email, Integer page, Integer size) {

        SearchContactRequest form = SearchContactRequest.builder()
                .name(name)
                .phone(phone)
                .email(email)
                .page(page).size(size)
                .build();

        Page<ContactResponse> contactResponse = contactService.search(user, form);

        return WebResponse.<List<ContactResponse>>builder()
                .data(contactResponse.getContent())
                .paging(PagingResponse.builder()
                        .toltalPage(contactResponse.getTotalPages())
                        .currentPage(contactResponse.getNumber())
                        .sizePerPage(contactResponse.getSize())
                        .contentSize(contactResponse.getNumberOfElements())
                        .build()
                )
                .build();
    }

    @Override
    public WebResponse<String> update(User user, String id, UpdateContactRequest form) {

        contactService.update(user, id, form);

        return WebResponse.<String>builder()
                .data("OK")
                .build();
    }

    @Override
    public WebResponse<String> delete(User user, String id) {

        contactService.delete(user, id);

        return WebResponse.<String>builder()
                .data("OK")
                .build();
    }
}
