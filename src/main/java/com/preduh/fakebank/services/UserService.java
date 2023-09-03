package com.preduh.fakebank.services;

import com.preduh.fakebank.dtos.UserLoginRequestRecordDto;
import com.preduh.fakebank.dtos.UserLoginResponseRecordDto;
import com.preduh.fakebank.dtos.UserRegisterRequestRecordDto;
import com.preduh.fakebank.entities.User;
import com.preduh.fakebank.infra.security.TokenService;
import com.preduh.fakebank.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {
    public final TokenService tokenService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public User createUser(@RequestBody UserRegisterRequestRecordDto userRegisterRequestRecordDto) {
        var usernameAlreadyExists = this.findUserByUsername(userRegisterRequestRecordDto.username()) != null;

        if (usernameAlreadyExists) {
            throw new RuntimeException("This username already exists");
        }

        var emailAlreadyExists = this.findUserByEmail(userRegisterRequestRecordDto.email()) != null;

        if (emailAlreadyExists) {
            throw new RuntimeException("This email already exists");
        }

        var user = new User();
        BeanUtils.copyProperties(userRegisterRequestRecordDto, user);
        user.setPassword(new BCryptPasswordEncoder().encode(userRegisterRequestRecordDto.password()));

        return this.userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public UserDetails findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserLoginResponseRecordDto login(@RequestBody UserLoginRequestRecordDto userLoginRequestRecordDto) {
        boolean usernameExists = this.findUserByUsername(userLoginRequestRecordDto.username()) != null;

        if (!usernameExists) {
            throw new RuntimeException("This username does not exist");
        }

        Authentication usernamePassword = new UsernamePasswordAuthenticationToken(userLoginRequestRecordDto.username(), userLoginRequestRecordDto.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        return new UserLoginResponseRecordDto(tokenService.generateToken((User) auth.getPrincipal()));
    }
}
