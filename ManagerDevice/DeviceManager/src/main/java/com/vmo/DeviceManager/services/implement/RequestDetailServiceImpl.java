package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.RequestDetail;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.repositories.RequestDetailRepository;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestDetailService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public void saveRequestDetail(Request request, RequestDto requestDto) {
        List<Device> devices = new ArrayList<>();
        for(Integer deviceId: requestDto.getDeviceIds()){
            devices.add(deviceService.getDeviceById(deviceId));
        }
        for(Device device : devices){
            RequestDetail requestDetail = new RequestDetail();
            requestDetail.setRequest(request);
            requestDetail.setDevice(device);
            requestDetail.setStartTime(requestDto.getStart());
            requestDetail.setEndTime(requestDto.getEnd());
            requestDetailRepository.save(requestDetail);
        }
    }
}
