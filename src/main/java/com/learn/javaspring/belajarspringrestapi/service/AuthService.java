package com.learn.javaspring.belajarspringrestapi.service;

import com.learn.javaspring.belajarspringrestapi.entity.User;
import com.learn.javaspring.belajarspringrestapi.model.LoginUserRequest;
import com.learn.javaspring.belajarspringrestapi.model.TokenResponse;
import com.learn.javaspring.belajarspringrestapi.repository.UserRepository;
import com.learn.javaspring.belajarspringrestapi.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);
        User user = userRepository.findById(request.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password Wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            userRepository.save(user);

            return TokenResponse.builder().token(user.getToken()).build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password Wrong");
        }
    }
}
