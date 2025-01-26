package com.niladri.security6.controller;

import com.niladri.security6.dto.SignUpRequest;
import com.niladri.security6.dto.UserResponse;
import com.niladri.security6.entity.UserEntity;
import com.niladri.security6.service.AuthService;
import com.niladri.security6.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private final UserService userService;
    private final AuthService authService;

    public SecurityController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
        String token = authService.login(signUpRequest);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/hi")
    public ResponseEntity<String> hi() {
        UserResponse principal = (UserResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok("Hello, " + principal.getName() + principal.getEmail());
    }
}
