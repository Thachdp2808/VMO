package com.vmo.DeviceManager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.models.enumEntity.EstatusRequest;
import com.vmo.DeviceManager.models.event.RequestEvent;
import com.vmo.DeviceManager.models.event.RequestReturnEvent;
import com.vmo.DeviceManager.models.mapper.UserMapper;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

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
@EntityListeners(Request.RequestEntityListener.class)
public class Request {
    @Id
    private int requestId;
    @Column(nullable = false, name = "created_date")
    private Date createdDate;
    @Column( name = "resolve_date")
    private Date resolveDate;
    @Column( name = "actual_end_time")
    private Date actualEndTime;
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
                ", actualEndTime=" + actualEndTime +
                ", status=" + status +
                ", userCreated=" + userCreated.getUserId() +
                ", userResolve=" + userResolve +
                ", requestDetails=" + requestDetails +
                ", reason='" + reason + '\'' +
                '}';
    }
    public static class RequestEntityListener {
        @Autowired
        private ApplicationEventPublisher eventPublisher;

        @PostUpdate
        //để phát hiện khi trạng thái của một thực thể thay đổi và phát sự kiện Spring.
        public void onPostUpdate(Request request) {
            boolean statusChanged = request.getStatus() == EstatusRequest.Approved;
            boolean endTimeChanged = request.getActualEndTime() != null;

            if (statusChanged && !endTimeChanged) {
                int total = request.getRequestDetails().size();
                System.out.println("Total request details: " + total);
                eventPublisher.publishEvent(new RequestEvent(request.getUserCreated(), total));
            } else if (endTimeChanged) {
                int total = request.getRequestDetails().size();
                System.out.println("Total request details: " + total);
                eventPublisher.publishEvent(new RequestReturnEvent(request.getUserCreated(), total));
            }
        }
    }
}
