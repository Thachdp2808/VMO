package com.vmo.DeviceManager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.enumEntity.EstatusUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

@Entity
@Table(name = "users")
public class User implements UserDetails {
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

    private String token;

    private String password;
    @Column(name = "job_title")
    private String jobTitle;
    private EstatusUser status;
    private Erole role;
    private String otp;
    @Column(name = "otp_time")
    private LocalDateTime otpTime;

    @ManyToOne
    @JoinColumn( name = "department_id")
    private Department department;
    @OneToMany(mappedBy = "userCreated", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JsonIgnore
    private List<Request> requestCreated;
    private String images;
    @Column(name = "total_device")
    private int total_device;

    public User(int i, String username, String mail, String firstName, String lastName) {
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", department=" + department.getDepartmentId() +
                ", requestCreated=" + requestCreated +
                ", images=" + images +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
