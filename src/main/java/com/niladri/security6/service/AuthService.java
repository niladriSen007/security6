package com.niladri.security6.service;

import com.niladri.security6.dto.LoginResponse;
import com.niladri.security6.dto.SignUpRequest;
import com.niladri.security6.dto.UserResponse;
import com.niladri.security6.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final ModelMapper modelMapper;



    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService, UserService userService, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public LoginResponse login(SignUpRequest signUpRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword()));
        UserEntity credentials = (UserEntity) auth.getPrincipal();
        System.out.println(credentials);
        String accessToken = tokenService.generateAccessToken(credentials);
        String refreshToken = tokenService.generateRefreshToken(credentials);
        return new LoginResponse(credentials.getId(), accessToken, refreshToken);
    }

    public LoginResponse refresh(String refreshToken) {
        Long userId = tokenService.getUserIdFromToken(refreshToken);
        UserResponse user = userService.getUserById(userId);
        String accessToken = tokenService.generateAccessToken(modelMapper.map(user, UserEntity.class));
        return new LoginResponse(user.getId(), accessToken, refreshToken);
    }
}
