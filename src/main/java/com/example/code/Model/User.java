package com.example.code.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int UserID;
    private String UserName, Avatar, UserAddress, UserPhone, Email, PASSWORD;
    
}
