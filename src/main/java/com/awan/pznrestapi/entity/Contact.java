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
@Table(name = "contacts")
public class Contact {

    @Id
    @Size(max = 100)
    private String id;

    @Column(name = "first_name")
    @Size(max = 100)
    private String firstName;

    @Column(name = "last_name")
    @Size(max = 100)
    private String lastName;

    @Size(max = 100)
    private String phone;

    @Size(max = 100)
    private String email;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "username")
    private User user;

    @OneToMany(mappedBy = "contact")
    List<Address> addresses;

}
