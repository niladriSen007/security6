package com.niladri.security6.controller;

import com.niladri.security6.dto.LoginResponse;
import com.niladri.security6.dto.SignUpRequest;
import com.niladri.security6.dto.UserResponse;
import com.niladri.security6.service.AuthService;
import com.niladri.security6.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private final UserService userService;
    private final AuthService authService;

    @Value("${deploy.env}")
    private String currentEnv;

    public SecurityController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
        LoginResponse tokens = authService.login(signUpRequest);
        Cookie cookie = new Cookie("refreshToken", tokens.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(currentEnv.equals("prod"));
        response.addCookie(cookie);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/hi")
    public ResponseEntity<String> hi() {
        UserResponse principal = (UserResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok("Hello, " + principal.getName() + principal.getEmail());
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found")
                );
        LoginResponse tokens = authService.refresh(refreshToken);
        Cookie cookie = new Cookie("refreshToken", tokens.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(tokens);
    }
}
