package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.exceptions.model.CategoryException;
import com.vmo.DeviceManager.exceptions.model.DeviceException;
import com.vmo.DeviceManager.exceptions.model.PagingException;
import com.vmo.DeviceManager.models.Category;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.DeviceDto;
import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import com.vmo.DeviceManager.repositories.CategoryRepository;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestDetailService;
import com.vmo.DeviceManager.services.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
            throw new DeviceException("Device name cannot be empty");
        }
        if (!deviceName.matches("[a-zA-Z0-9\\s]+")) {
            throw new DeviceException("Device name contains invalid characters");
        }

        double price = deviceDto.getPrice();
        if (price <= 0) {
            throw new DeviceException("Price must be a positive number");
        }

        String description = deviceDto.getDescription();
        if (description == null || description.isBlank()) {
            throw new DeviceException("Description cannot be empty");
        }
    }

    @Override
    public String addDevice(DeviceDto deviceDto) {
        // Kiểm tra category tồn tại hay không
        Category category = categoryRepository.findById(deviceDto.getCategory())
                .orElseThrow(() -> new CategoryException(deviceDto.getCategory()));

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
                .orElseThrow(() -> new DeviceException(id));

        // Kiểm tra dữ liệu đầu vào
        validateDeviceData(deviceDto);

        // Kiểm tra category tồn tại hay không
        Category category = categoryRepository.findById(deviceDto.getCategory())
                .orElseThrow(() -> new CategoryException(deviceDto.getCategory()));

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
            throw new DeviceException("No devices found");
        }
        return devices;
    }

    @Override
    public List<Device> getMyDevice() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        List<Device> list =  deviceRepository.getMyDevices(currentUser.getUserId());
        if(list.isEmpty()){
            throw new DeviceException("No devices found");
        }
        return list;
    }

    @Override
    public Device getDeviceById(int id) {
        return deviceRepository.findById(id).orElseThrow(() -> new DeviceException(id));
    }

    @Override
    public Page<Device> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 2);
        return deviceRepository.findAll(pageable);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public String getDashboard() {
        int countAvail = 0, countUltil = 0, countMainten = 0;
        List<Device> devices = deviceRepository.findAll();
        List<Object[]> categoryDeviceList = deviceRepository.countDeviceByCategory();
        List<Object[]> typeDeviceList = deviceRepository.countDeviceByType();
        StringBuilder dashboard = new StringBuilder();
        String categoryName;
        Long countCategory ;
        dashboard.append("====== Category Device ======").append("\n");
        for (Object[] result : categoryDeviceList) {
            categoryName = (String) result[0];
            countCategory = (Long) result[1];
            dashboard.append("Number of ").append(categoryName).append(" devices: ").append(countCategory).append("\n");
        }

        String typeName;
        Long countType ;
        dashboard.append("====== Type Device ======").append("\n");
        for (Object[] result : typeDeviceList) {
            typeName = (String) result[0];
            countType = (Long) result[1];
            dashboard.append("Number of ").append(typeName).append(" devices: ").append(countType).append("\n");
        }
        dashboard.append("====== Status Device ======").append("\n");
        if (devices.isEmpty()) {
            throw new DeviceException("No devices found");
        }
        for(Device device: devices){
            if(device.getStatus() == EstatusDevice.Availability){
                countAvail++;
            }
            if(device.getStatus() == EstatusDevice.Utilized){
                countUltil++;
            }
            if(device.getStatus() == EstatusDevice.Maintenance){
                countMainten++;
            }
        }
        dashboard.append("Number of availability devices: ").append(countAvail).append("\n")
                .append("Number of utilized devices: ").append(countUltil).append("\n")
                .append("Number of maintenance devices: ").append(countMainten);
        return dashboard.toString();
    }




    @Override
    public Page<Device> pageAndSearch(String keyword,List<String> status, List<String> category, List<String> type, Integer pageNo, Integer pageSize) {
        List<Device> listDevice = getAllDevice();
        if (pageNo == null || pageNo <= 0) {
            throw new PagingException("Invalid page number");
        }
        if (pageSize == null || pageSize <= 0) {
            throw new PagingException("Invalid page size");
        }
        int size;
        // Tạo ra Predicate dựa trên keyword, category và type
        Predicate<Device> predicate = device -> {
            String word = keyword.trim();
            boolean matchStatus = status == null || status.isEmpty() || status.contains(device.getStatus().name());
            boolean matchKeyword = word.isEmpty() || device.getDeviceName().toLowerCase().contains(word.toLowerCase());
            //Kiểm tra xem categoryId có tồn tại trong category hay không
            boolean matchCategory = category == null || category.isEmpty() || category.contains(device.getCategory().getCategoryName());
            boolean matchType = type == null || type.isEmpty() || type.contains(device.getCategory().getType());
            return matchStatus & matchKeyword && matchCategory && matchType;
        };

        // Áp dụng Predicate vào danh sách thiết bị
        List<Device> filteredDevices = search(listDevice, predicate);
        size = filteredDevices.size();

        int totalPages = (int) Math.ceil((double) size / pageSize);
        if (pageNo > totalPages) {
            throw new PagingException("Page number exceeds total pages");
        }

        // Phân trang và trả về kết quả
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageSize), size);
        filteredDevices = filteredDevices.subList(start, end);
        return new PageImpl<>(filteredDevices, pageable, size);
    }
    private List<Device> search(List<Device> listDevice, Predicate<Device> predicate) {
        return listDevice.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
