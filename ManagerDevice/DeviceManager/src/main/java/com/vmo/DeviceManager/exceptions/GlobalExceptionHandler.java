package com.vmo.DeviceManager.exceptions;

import com.vmo.DeviceManager.exceptions.model.*;
import com.vmo.DeviceManager.models.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(DepartmentException.class)
    public ResponseEntity<Object> departmentExceptionHandler(DepartmentException ex, HttpServletRequest request) {
        log.warn(buildLogMessage("Department", ex.getMessage(), request));
        return createResponseDetail(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeviceException.class)
    public ResponseEntity<Object> deviceExceptionHandler(DeviceException ex, HttpServletRequest request) {
        log.warn(buildLogMessage("Device", ex.getMessage(), request));
        return createResponseDetail(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<Object> requestExceptionHandler(RequestException ex, HttpServletRequest request) {
        log.warn(buildLogMessage("Request", ex.getMessage(), request));
        return createResponseDetail(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> userExceptionHandler(UserException ex, HttpServletRequest request) {
        log.warn(buildLogMessage("User", ex.getMessage(), request));
        return createResponseDetail(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PagingException.class)
    public ResponseEntity<Object> PagingExceptionHandler(PagingException ex, HttpServletRequest request) {
        log.warn(buildLogMessage("Paging", ex.getMessage(), request));
        return createResponseDetail(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<Object> categoryExceptionHandler(CategoryException ex, HttpServletRequest request) {
        log.warn(buildLogMessage("Category", ex.getMessage(), request));
        return createResponseDetail(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    private ResponseEntity<Object> createResponseDetail(String message, HttpStatus status) {
        Map<String, String> detail = new HashMap<>();
        detail.put("error", message);
        detail.put("time_stamp", LocalDateTime.now().toString());

        return new ResponseEntity<>(detail, status);
    }
    private String buildLogMessage(String failedActionName, String exceptionMessage, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return String.format("%s error: %s - Uri: %s - Method: %s - IP address: %s - Current user: %s", failedActionName, exceptionMessage, request.getRequestURI(), request.getMethod(), request.getRemoteAddr(), currentUser.getUserId() == -1 ? "Anonymous" : currentUser.getUserId());
    }
}
