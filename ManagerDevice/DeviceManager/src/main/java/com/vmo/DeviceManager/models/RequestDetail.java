package com.vmo.DeviceManager.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "requestDetails")
public class RequestDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
    @Id
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;


}
