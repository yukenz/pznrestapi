package com.awan.pznrestapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @Size(max = 50)
    private String username;

    @Size(max = 100)
    private String password;

    @Size(max = 100)
    private String name;

    @Column(unique = true)
    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpired;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    List<Contact> contacts;

}
