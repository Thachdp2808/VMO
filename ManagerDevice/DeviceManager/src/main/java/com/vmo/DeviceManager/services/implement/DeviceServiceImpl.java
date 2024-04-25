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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

    private final CategoryRepository categoryRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, CategoryRepository categoryRepository) {
        this.deviceRepository = deviceRepository;
        this.categoryRepository = categoryRepository;
    }


    // Kiểm tra dữ liệu đầu vào
    private void validateDeviceData(DeviceDto deviceDto) {
        String deviceName = deviceDto.getDeviceName();
        if (deviceName == null || deviceName.isBlank()) {
            throw new RuntimeException("Device name cannot be empty");
        }
        if (!deviceName.matches("[a-zA-Z0-9\\s]+")) {
            throw new RuntimeException("Device name contains invalid characters");
        }

        double price = deviceDto.getPrice();
        if (price <= 0) {
            throw new RuntimeException("Price must be a positive number");
        }

        String description = deviceDto.getDescription();
        if (description == null || description.isBlank()) {
            throw new RuntimeException("Description cannot be empty");
        }
    }

    @Override
    public String addDevice(DeviceDto deviceDto) {
        // Kiểm tra category tồn tại hay không
        Category category = categoryRepository.findById(deviceDto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category does not exist"));

        // Kiểm tra dữ liệu đầu vào
        validateDeviceData(deviceDto);

        // Tạo mới thiết bị và lưu vào cơ sở dữ liệu
        Device device = new Device();
        device.setDeviceName(deviceDto.getDeviceName().toUpperCase());
        device.setCategory(category);
        device.setPrice(deviceDto.getPrice());
        device.setStatus(EstatusDevice.Availability);
        device.setDescription(deviceDto.getDescription());
        deviceRepository.save(device);
        return "Device added successfully";
    }

    @Override
    @Transactional
    public String updateDevice(int id, DeviceDto deviceDto) {
        Device exitsDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device does not exist"));

        // Kiểm tra dữ liệu đầu vào
        validateDeviceData(deviceDto);

        // Kiểm tra category tồn tại hay không
        Category category = categoryRepository.findById(deviceDto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category does not exist"));

        // Cập nhật thông tin thiết bị
        exitsDevice.setDeviceName(deviceDto.getDeviceName().toUpperCase());
        exitsDevice.setCategory(category);
        exitsDevice.setPrice(deviceDto.getPrice());
        exitsDevice.setDescription(deviceDto.getDescription());
        exitsDevice.setStatus(deviceDto.getStatus());
        deviceRepository.save(exitsDevice);
        return "Update successful";
    }



    @Override
    public List<Device> getAllDevice() {
        List<Device> devices = deviceRepository.findAll();
        if (devices.isEmpty()) {
            throw new RuntimeException("No devices found");
        }
        return devices;
    }

    @Override
    public List<Device> getMyDevice() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        List<Device> list =  deviceRepository.getMyDevices(currentUser.getUserId());
        if(list.isEmpty()){
            throw new RuntimeException("No devices found");
        }
        return list;
    }

    @Override
    public Device getDeviceById(int id) {
        return deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device does not exits"));
    }

    @Override
    public Page<Device> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 2);
        return deviceRepository.findAll(pageable);
    }

    @Override
    public Page<Device> pageAndSearch(List<Device> listDevice, String keyword, String type, Integer pageNo, Integer pageSize) {
        if (pageNo == null || pageNo <= 0) {
            throw new IllegalArgumentException("Invalid page number");
        }
        if (pageSize == null || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page size");
        }
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
            default:
                throw new IllegalArgumentException("Invalid search type");
        }

        int totalPages = (int) Math.ceil((double) size / pageSize);
        if (pageNo > totalPages) {
            throw new IllegalArgumentException("Page number exceeds total pages");
        }

        // Phân trang và trả về kết quả
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageSize), size);
        list = list.subList(start, end);
        return new PageImpl<>(list, pageable, size);
    }
    private List<Device> search(List<Device> listDevice, Predicate<Device> predicate) {
        return listDevice.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
