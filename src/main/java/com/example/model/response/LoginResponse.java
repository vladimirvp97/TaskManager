package com.example.model.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class LoginResponse {
    private final String email;
    private final String token;

    public LoginResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
