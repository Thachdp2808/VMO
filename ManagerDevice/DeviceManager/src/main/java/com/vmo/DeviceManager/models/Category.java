package com.vmo.DeviceManager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    @Column(nullable = false, name = "category_name")
    private String categoryName;
    @Column(nullable = false)
    private String type;
    private String description;
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Device> devices;


}
