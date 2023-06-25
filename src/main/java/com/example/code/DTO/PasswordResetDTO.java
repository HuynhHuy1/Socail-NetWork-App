package com.example.code.dto;

import jakarta.validation.constraints.Email;

public class PasswordResetDTO {
    @Email
    String Email;
    int KeyNumber;
}
