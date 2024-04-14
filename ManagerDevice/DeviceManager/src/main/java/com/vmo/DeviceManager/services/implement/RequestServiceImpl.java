package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.enumEntity.EstatusRequest;
import com.vmo.DeviceManager.repositories.RequestRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.RequestService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Request> getAllByStatus() {
        List<Integer> statuses = Arrays.asList(1, 2, 3);
        return requestRepository.findAllByStatusIn(statuses);
    }

    @Override
    public Request findByRequestId(int id) {
        return requestRepository.findByRequestId(id);
    }

    @Override
    public List<Request> getRequestByCreatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return requestRepository.findByUserCreated(currentUser);
    }

    @Override
    public int addRequest(Request request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        request.setUserCreated(currentUser);
        LocalDate currentDate = LocalDate.now();
        Date currentDateSql = Date.valueOf(currentDate);
        request.setCreatedDate(currentDateSql);
        request.setStatus(EstatusRequest.Pending);
        requestRepository.save(request);
        return request.getRequestId();
    }

    @Override
    public void sendRequest(int id) {
        requestRepository.sendRequest(id);

    }
}
