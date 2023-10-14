package com.awan.pznrestapi.service;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.ContactResponse;
import com.awan.pznrestapi.model.CreateContactRequest;
import com.awan.pznrestapi.model.SearchContactRequest;
import com.awan.pznrestapi.model.UpdateContactRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface ContactService {

    @Transactional
    ContactResponse create(User user, CreateContactRequest form);

    @Transactional(readOnly = true)
    ContactResponse get(User user, String id);

    @Transactional
    void update(User user, String id, UpdateContactRequest form);

    @Transactional
    void delete(User user, String id);

    @Transactional(readOnly = true)
    Page<ContactResponse> search(User user, SearchContactRequest form);

}
