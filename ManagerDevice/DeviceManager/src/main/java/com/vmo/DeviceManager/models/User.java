package com.vmo.DeviceManager.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "user_id")
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
    @ManyToOne
    @JoinColumn(name = "user_role")
    private Role role;
    private String otp;
    @Column(name = "otp_time")
    private Date otpTime;

    @ManyToOne
    @JoinColumn(nullable = false, name = "apartment_id")
    private Apartment apartment;
    @OneToMany(mappedBy = "userCreated")
    private List<Request> requestCreated;
    @OneToMany(mappedBy = "user")
    private List<ImageUser> images;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
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