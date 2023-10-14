package com.awan.pznrestapi.service;

import com.awan.pznrestapi.entity.Contact;
import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.ContactResponse;
import com.awan.pznrestapi.model.CreateContactRequest;
import com.awan.pznrestapi.model.SearchContactRequest;
import com.awan.pznrestapi.model.UpdateContactRequest;
import com.awan.pznrestapi.repository.ContactRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidatorService validatorService;

    @Override
    public ContactResponse create(User user, CreateContactRequest form) {

        validatorService.validateObject(form);

        Contact contact = Contact.builder()
                .id(UUID.randomUUID().toString())
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .phone(form.getPhone())
                .email(form.getEmail())
                .user(user)
                .build();

        //Save
        Contact savedContact = contactRepository.save(contact);

        ContactResponse contactResponse = ContactResponse.builder()
                .id(savedContact.getId())
                .firstName(savedContact.getFirstName())
                .lastName(savedContact.getLastName())
                .phone(savedContact.getPhone())
                .email(savedContact.getEmail())
                .build();

        return contactResponse;
    }

    @Override
    public ContactResponse get(User user, String id) {
        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kontak tidak ditemukan"));

        ContactResponse contactResponse = ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();

        return contactResponse;
    }

    @Override
    public void update(User user, String id, UpdateContactRequest form) {

        form.setId(id);
        validatorService.validateObject(form);

        Contact contact = contactRepository.findFirstByUserAndId(user, form.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kontak tidak ditemukan"));

        //Update By Form
        contact.setFirstName(form.getFirstName());
        contact.setLastName(form.getLastName());
        contact.setEmail(form.getEmail());
        contact.setPhone(form.getPhone());

        contactRepository.save(contact);

    }

    @Override
    public void delete(User user, String id) {

        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kontak tidak ditemukan"));

        contactRepository.delete(contact);

    }

    @Override
    public Page<ContactResponse> search(User user, SearchContactRequest form) {

        //Declare Search Predicate Spesification
        Specification<Contact> specification = (root, query, builder) -> {
            List<Predicate> predicates = new LinkedList<Predicate>();
            predicates.add(builder.equal(root.get("user"), user));

            //Name Matcher
            if (Objects.nonNull(form.getName())) {
                predicates.addAll(
                        List.of(
                                builder.like(
                                        root.get("firstName"),
                                        "%" + form.getName() + "%"
                                ), builder.like(
                                        root.get("lastName"),
                                        "%" + form.getName() + "%"
                                )
                        )
                );
            }

            //Email Matcher
            if (Objects.nonNull(form.getEmail())) {
                predicates.add(
                        builder.like(
                                root.get("email"),
                                "%" + form.getEmail() + "%"
                        )
                );
            }

            //Phone Matcher
            if (Objects.nonNull(form.getPhone())) {
                predicates.add(
                        builder.like(
                                root.get("phone"),
                                "%" + form.getPhone() + "%"
                        )
                );
            }

            return query
                    .where(predicates.toArray(Predicate[]::new))
                    .getRestriction();

        };

        //Pageable Specification
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());

        //findAll With Specification and Pageable
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);

        //Result findAll Collect to List with Stream
        List<ContactResponse> contactResponseList = contacts.getContent().stream().map(contact -> {
            return ContactResponse.builder()
                    .id(contact.getId())
                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .email(contact.getEmail())
                    .phone(contact.getPhone())
                    .build();
        }).toList();

        //Return Pageable Impl
        return new PageImpl<ContactResponse>(contactResponseList, pageable, contacts.getTotalElements());
    }
}
