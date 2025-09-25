package com.example.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "email is required")
    private String password;
}
