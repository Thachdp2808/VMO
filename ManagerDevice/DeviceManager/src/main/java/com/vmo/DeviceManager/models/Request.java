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
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;
    @Column(nullable = false, name = "created_date")
    private Date createdDate;
    @Column(nullable = false, name = "resolve_date")
    private Date resolveDate;
    private int status;
    @ManyToOne
    @JoinColumn(name = "user_created")
    private User userCreated;
    private User userResolve;
    @OneToMany(mappedBy = "request")
    private List<RequestDetail> requestDetails;

    private String reason;


}
