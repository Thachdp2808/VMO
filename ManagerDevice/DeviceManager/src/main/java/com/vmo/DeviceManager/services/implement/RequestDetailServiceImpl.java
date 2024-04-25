package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.RequestDetail;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import com.vmo.DeviceManager.repositories.RequestDetailRepository;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestDetailService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
@Service
public class RequestDetailServiceImpl implements RequestDetailService {
    private final RequestDetailRepository requestDetailRepository;
    private final DeviceService deviceService;

    public RequestDetailServiceImpl(RequestDetailRepository requestDetailRepository, DeviceService deviceService) {
        this.requestDetailRepository = requestDetailRepository;
        this.deviceService = deviceService;
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
    public String getDurationDay(int deviceid) {
        int time = requestDetailRepository.getDurationDay(deviceid);
        if(time == 0 ){
            return "Device does not borrow " + deviceid;
        }
        return "Duration day of device: " + time + " day";
    }
}
