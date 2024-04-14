package com.vmo.DeviceManager.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "image_users")
public class ImageUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageid;
    @Column(nullable = false, name = "image_link")
    private String imageLink;
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    private String name;
}
