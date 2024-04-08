package com.vmo.DeviceManager.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(nullable = false, name = "first_name")
    private String firstName;
    @Column(nullable = false, name = "last_name")
    private String lastName;

    private int phone;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "job_title")
    private String jobTitle;

    @ManyToOne
    @JoinColumn(nullable = false, name = "apartment_id")
    private Apartment apartment;
    @OneToMany(mappedBy = "userCreated")
    private List<Request> requestCreated;
    @OneToMany(mappedBy = "userResolve")
    private List<Request> requestResolve;

    public User(){

    }


}
