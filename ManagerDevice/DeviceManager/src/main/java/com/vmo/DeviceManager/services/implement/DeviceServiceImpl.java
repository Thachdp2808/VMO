package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.models.mapper.UserMapper;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.services.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> getDeviceByStatus(int status) {

        return null;
    }

    @Override
    public List<Device> getAllDevice() {
        List<Device> listDevice = deviceRepository.findAll();
        return listDevice;
    }

    @Override
    public List<Device> getDeviceById(int id) {
        //Sửa lại
        List<Device> listDevice = deviceRepository.findAll();
        return null;
    }

    @Override
    public Page<Device> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 2);
        deviceRepository.findAll(pageable);
        return null;
    }

    @Override
    public Page<Device> pageAndSearch(List<Device> listDevice, String keyword, String type, Integer pageNo) {
        List<Device> list = new ArrayList<>();
        int size = 0;
        switch (type) {
            case "name":
                list = search(listDevice, device -> device.getDeviceName().contains(keyword.toUpperCase()));
                size = list.size();
                break;
            case "category":
                list = search(listDevice, device -> device.getCategory().getCategoryId() == Integer.parseInt(keyword));
                size = list.size();
                break;
            case "type":
                list = search(listDevice, device -> device.getCategory().getType().contains(keyword.toUpperCase()));
                size = list.size();
                break;
        }
        
        Pageable pageable = PageRequest.of(pageNo-1,2);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<Device>(list, pageable, size);
    }
    private List<Device> search(List<Device> listDevice, Predicate<Device> predicate) {

        List<Device> resultList = new ArrayList<>();
        for (Device device : listDevice) {
            if (predicate.test(device)) {
                resultList.add(device);
            }
        }
        return resultList;
    }
}
