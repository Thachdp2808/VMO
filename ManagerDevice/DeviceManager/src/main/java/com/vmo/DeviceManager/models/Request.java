package com.vmo.DeviceManager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.models.enumEntity.EstatusRequest;
import com.vmo.DeviceManager.models.mapper.UserMapper;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;
    @Column(nullable = false, name = "created_date")
    private Date createdDate;
    @Column( name = "resolve_date")
    private Date resolveDate;
    private EstatusRequest status;
    @ManyToOne
    @JoinColumn(name = "user_created")
    private User userCreated;
    private Integer userResolve;
    @OneToMany(mappedBy = "request", fetch = FetchType.EAGER)

    private List<RequestDetail> requestDetails;
    @Column(nullable = false)
    private String reason;

    @Override
    public String toString() {
        return "Request{" +
                "requestId=" + requestId +
                ", createdDate=" + createdDate +
                ", resolveDate=" + resolveDate +
                ", status=" + status +
                ", userCreated=" + userCreated.getUserId() +
                ", userResolve=" + userResolve +
                ", requestDetails=" + requestDetails +
                ", reason='" + reason + '\'' +
                '}';
    }
}
