package com.awan.pznrestapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @Size(max = 100)
    private String id;

    @Size(max = 200)
    private String street;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String province;

    @Size(max = 100)
    private String country;


    @Column(name = "postal_code")
    private String postalCode;

    /*Non-Primitive*/
    @ManyToOne()
    @JoinColumn(name = "contact", referencedColumnName = "id")
    private Contact contact;


}
