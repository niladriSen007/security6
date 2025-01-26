package com.niladri.security6.dto;

import lombok.Data;

@Data
public class LoginRequest {
    String email;
    String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
