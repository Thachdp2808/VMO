package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.models.Category;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.DeviceDto;
import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import com.vmo.DeviceManager.repositories.CategoryRepository;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.services.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

    private final CategoryRepository categoryRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, CategoryRepository categoryRepository) {
        this.deviceRepository = deviceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Device> getDeviceByStatus(int status) {
        return null;
    }

    @Override
    public int addDevice(DeviceDto deviceDto) {
        Category category = categoryRepository.findById(deviceDto.getCategory()).orElseThrow(() -> new RuntimeException("Category does not exits"));
        Device device = new Device();
        device.setDeviceName(deviceDto.getDeviceName().toUpperCase());
        device.setCategory(category);
        device.setPrice(deviceDto.getPrice());
        device.setStatus(EstatusDevice.Availability);
        device.setDescription(deviceDto.getDescription());
        deviceRepository.save(device);
        return device.getDeviceId();
    }

    @Override
    @Transactional
    public void updateDevice(int id, DeviceDto deviceDto) {
        Device exitsDevice = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device does not exits"));
        deviceDto.setDeviceName(deviceDto.getDeviceName().toUpperCase());
        BeanUtils.copyProperties(deviceDto,exitsDevice);
        deviceRepository.save(exitsDevice);
    }

    @Override
    @Transactional
    public void deleteDevice(int id) {

    }

    @Override
    public List<Device> getAllDevice() {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> getMyDevice() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return deviceRepository.getMyDevices(currentUser.getUserId());
    }

    @Override
    public Device getDeviceById(int id) {
        //Sửa lại
        return deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device does not exits"));
    }

    @Override
    public Page<Device> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 2);
        deviceRepository.findAll(pageable);
        return null;
    }

    @Override
    public Page<Device> pageAndSearch(List<Device> listDevice, String keyword, String type, Integer pageNo, Integer PageSize) {
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
        
        Pageable pageable = PageRequest.of(pageNo-1,PageSize);
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
