package com.example.code.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int id;
    @Email(message = "Invalid format")
    @NotEmpty(message = "required")
    private String email;
    private String name;
    private String avatar;
    private String address; 
    private String phone;
    @NotEmpty(message = "required")
    private String password;
    private int follower;
    private int following;
}
