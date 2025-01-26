package com.niladri.security6.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SignUpRequest {
    String email;
    String password;
    String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
