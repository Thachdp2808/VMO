package com.vmo.DeviceManager.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int apartmentId;
    @Column(nullable = false, name = "apartment_name")
    private String apartmentName;
    private String address;
    private String description;
    @OneToMany(mappedBy = "apartment")
    private List<User> users;


}
