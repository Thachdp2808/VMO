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
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int departmentId;
    @Column(nullable = false, name = "department_name")
    private String departmentName;
    private String address;
    private String description;
    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JsonIgnore
    private List<User> users;

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
