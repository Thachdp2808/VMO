package com.vmo.DeviceManager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "requestDetails",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"request_id", "device_id"})})
public class RequestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "request_id")
    @JsonIgnore
    private Request request;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
    @Column( name = "start_time")
    private Date startTime;
    @Column( name = "end_time")
    private Date endTime;


    @Override
    public String toString() {
        return "RequestDetail{" +
                "request=" + request.getRequestId() +
                ", device=" + device.getDeviceId() +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
