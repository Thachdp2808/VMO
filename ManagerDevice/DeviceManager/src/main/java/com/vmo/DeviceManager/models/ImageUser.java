package com.vmo.DeviceManager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private User user;
    private String name;

    @Override
    public String toString() {
        return "ImageUser{" +
                "imageid=" + imageid +
                ", imageLink='" + imageLink + '\'' +
                ", user=" + user.getUserId() +
                ", name='" + name + '\'' +
                '}';
    }
}
