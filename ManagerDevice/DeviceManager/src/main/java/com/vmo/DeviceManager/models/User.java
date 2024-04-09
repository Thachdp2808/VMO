package com.vmo.DeviceManager.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "users")
public class User{
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

    private String address;

    private String password;
    @Column(name = "job_title")
    private String jobTitle;
    @Enumerated(EnumType.STRING)
    private Erole role;
    private String otp;
    @Column(name = "otp_time")
    private Date otpTime;

    @ManyToOne
    @JoinColumn( name = "apartment_id")
    private Apartment apartment;
    @OneToMany(mappedBy = "userCreated")
    private List<Request> requestCreated;
    @OneToMany(mappedBy = "user")
    private List<ImageUser> images;



}
