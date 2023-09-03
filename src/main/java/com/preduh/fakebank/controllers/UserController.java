package com.preduh.fakebank.controllers;

import com.preduh.fakebank.dtos.UserLoginRequestRecordDto;
import com.preduh.fakebank.dtos.UserLoginResponseRecordDto;
import com.preduh.fakebank.dtos.UserRegisterRequestRecordDto;
import com.preduh.fakebank.entities.User;
import com.preduh.fakebank.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();

            errors.put("timestamp", Instant.now());
            errors.put("status", HttpStatus.BAD_REQUEST.value());
            errors.put("error", "Bad request");
            errors.put("message", errorMessage);
            errors.put("path", request.getRequestURI());
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, Object> handleValidationExceptions(RuntimeException ex, HttpServletRequest request) {
        Map<String, Object> errors = new HashMap<>();

        String errorMessage = ex.getMessage();

        errors.put("timestamp", Instant.now());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Bad request");
        errors.put("message", errorMessage);
        errors.put("path", request.getRequestURI());

        return errors;
    }


    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody @Valid UserRegisterRequestRecordDto userRegisterRequestRecordDto) {
        var user = userService.createUser(userRegisterRequestRecordDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUsers() {
        var users = userService.findAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseRecordDto> login(@RequestBody @Valid UserLoginRequestRecordDto userLoginRequestRecordDto) {
        var user = userService.login(userLoginRequestRecordDto);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
