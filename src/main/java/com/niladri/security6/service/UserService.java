package com.niladri.security6.service;

import com.niladri.security6.dto.SignUpRequest;
import com.niladri.security6.dto.UserResponse;
import com.niladri.security6.entity.UserEntity;
import com.niladri.security6.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder,
                      TokenService tokenService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserResponse signUp(SignUpRequest signUpRequest) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(signUpRequest.getEmail());
        if (existingUser.isPresent()) {
            System.out.println(existingUser.toString());
            throw new IllegalArgumentException("User already exists");
        }
        System.out.println("User not found");
        UserEntity userEntity = modelMapper.map(signUpRequest, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        return modelMapper.map(userRepository.save(userEntity), UserResponse.class);
    }

    public UserResponse getUserById(Long id) {
        return modelMapper.map(userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found")), UserResponse.class);
    }


}
