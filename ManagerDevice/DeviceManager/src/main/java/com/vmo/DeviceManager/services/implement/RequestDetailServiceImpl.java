package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.RequestDetail;
import com.vmo.DeviceManager.repositories.RequestDetailRepository;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestDetailService;
import com.vmo.DeviceManager.services.StorageService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
public class RequestDetailServiceImpl implements RequestDetailService {
    private final RequestDetailRepository requestDetailRepository;
    private final DeviceService deviceService;
    private final StorageService service;
    public RequestDetailServiceImpl(RequestDetailRepository requestDetailRepository, DeviceService deviceService, StorageService service) {
        this.requestDetailRepository = requestDetailRepository;
        this.deviceService = deviceService;
        this.service = service;
    }


    @Override
    public void saveRequestDetail(Request request, List<Device> listDevice,Date start, Date end) {

        for(Device device : listDevice){
            RequestDetail requestDetail = new RequestDetail();
            requestDetail.setRequest(request);
            requestDetail.setDevice(device);
            requestDetail.setStartTime(start);
            requestDetail.setEndTime(end);
            requestDetailRepository.save(requestDetail);
        }
    }

    @Override
    public String getDurationDay() {
        List<Object[]> durationDayList = requestDetailRepository.getDurationDay();
        int deviceId;
        String deviceName;
        BigDecimal duraDay;
        StringBuilder dashboard = new StringBuilder();

        // Append headers for CSV
        dashboard.append("Device ID,Device Name,Duration Days").append("\n");
        for (Object[] result : durationDayList) {
            deviceId = (Integer) result[0];
            deviceName = (String) result[1];
            duraDay = (BigDecimal) result[2];
            dashboard.append(deviceId).append(",").append(deviceName).append(",").append(duraDay).append("\n");
        }
        return dashboard.toString();
    }

    @Override
    public String uploadFileDashboard() {
        String contentDashboard = deviceService.getDashboard();
        String content = getDurationDay();
        String fileContent = contentDashboard + "\n" + content; // Concatenate the strings

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = currentTime.format(formatter);
        String fileName = "Dashboard_" + timestamp + ".csv"; // Tên file mới
        // Tạo và lưu trữ file trên AWS S3
        String fileUrl = service.UploadFileDashboard(fileName, fileContent);


        // Trả về URL của file đã lưu trữ
        return  fileUrl;
    }
}
