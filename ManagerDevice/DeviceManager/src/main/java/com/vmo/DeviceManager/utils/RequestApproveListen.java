package com.vmo.DeviceManager.utils;

import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.event.RequestEvent;
import com.vmo.DeviceManager.models.event.RequestReturnEvent;
import com.vmo.DeviceManager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RequestApproveListen {
    @Autowired
    private UserRepository userRepository;

    @EventListener
    public void handleRequestApprovedEvent(RequestEvent event) {
        // Lấy thông tin người dùng từ sự kiện hoặc từ cơ sở dữ liệu
        User user = (User) event.getSource(); // Giả sử sự kiện cung cấp thông tin người dùng
        // Lấy giá trị hiện tại của total_device của người dùng
        int count = user.getTotal_device();
        int total = event.getTotal();

        // Tăng giá trị total_device lên một đơn vị
        count = count + total;
        // Gán giá trị mới cho total_device của người dùng
        user.setTotal_device(count);
        userRepository.save(user);
        // In thông báo ra console
        System.out.println("Request is approved! Total device count: " + count);
    }

    @EventListener
    public void handleRequestReturnEvent(RequestReturnEvent event) {
        // Lấy thông tin người dùng từ sự kiện hoặc từ cơ sở dữ liệu
        User user = (User) event.getSource(); // Giả sử sự kiện cung cấp thông tin người dùng
        // Lấy giá trị hiện tại của total_device của người dùng
        int count = user.getTotal_device();
        int total = event.getTotal();
        // Tăng giá trị total_device lên một đơn vị
        count = count - total;
        // Gán giá trị mới cho total_device của người dùng
        user.setTotal_device(count);
        userRepository.save(user);
        // In thông báo ra console
        System.out.println("Request is return! Total device count: " + count);
    }
}
