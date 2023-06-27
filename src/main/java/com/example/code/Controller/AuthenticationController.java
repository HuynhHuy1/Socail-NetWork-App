package com.example.code.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.EmailDTO;
import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
import com.example.code.service.AuthenticationService;
import com.example.code.staticmessage.ErrorMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("signUp")
    public ResponseEntity<ResponseDTO> signUp(@Valid @RequestBody UserDTO userDto,BindingResult bindingResult) {
        if(bindingResult.hasErrors() || userDto.getName().equals("") || userDto.getName() == null ){
            List<String> listError = new ArrayList<>();
            if(userDto.getName() == null || userDto.getName().equals("")){
                String fieldError = "name : required ";
                listError.add(fieldError);
            }
            bindingResult.getFieldErrors().forEach( error -> {
                String fieldError = error.getField() + " : " + error.getDefaultMessage() ;
                listError.add(fieldError);
            });
            return ResponseEntity.badRequest().body(new ResponseDTO("Failed", listError.toString(),""));
        }
        ResponseDTO responseDTO = authenticationService.signUp(userDto);
        if(responseDTO.getStatus().equals("Success")){
            logger.info(userDto.getName() + "Sign up");
            return ResponseEntity.ok().body(responseDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
    }

    @PostMapping("login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody UserDTO userDto,BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            List<String> listError = new ArrayList<>();
            bindingResult.getFieldErrors().forEach( error -> {
                String fieldError = error.getField() + " : " + error.getDefaultMessage() ;
                listError.add(fieldError);
            });
            return ResponseEntity.badRequest().body(new ResponseDTO("Failed", listError.toString(),""));
        }

        ResponseDTO response = authenticationService.login(userDto);
        if(response.getStatus().equals("Success")){
            logger.info(userDto.getName() + "signup");
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("forgot-password")
    public ResponseEntity<ResponseDTO> forgotPassword(@Valid @RequestBody EmailDTO emailDto,BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            FieldError fieldError  = bindingResult.getFieldError();
            String error =  fieldError.getDefaultMessage();
            String field = fieldError.getField();
            String fieldErrorMessage = field + " : " + error;
            return ResponseEntity.badRequest().body(new ResponseDTO("Failed", fieldErrorMessage,""));
        }
        String email = emailDto.getEmail();
        if(authenticationService.sendConfirmationEmail(email)){
            logger.info("Send email to " + email);
            return ResponseEntity.ok().body(new ResponseDTO("Success", "Đã gửi mail xác nhận", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("Failed", ErrorMessage.EMAIL_NOT_EXISTS,""));
    }

    @PostMapping("check-key-number")
    public ResponseEntity<ResponseDTO> checkKeyNumber(@RequestBody Map<String, Integer> mapKeyNumber) {
        if(mapKeyNumber.size() == 0 || mapKeyNumber.get("keyNumber") == null){
            String errorMessage = "KeyNumber required";
            return ResponseEntity.badRequest().body(new ResponseDTO("Failed", errorMessage,""));
        }
        int keyNumber = mapKeyNumber.get("keyNumber");
        ResponseDTO responseDTO = authenticationService.generateTokenResetPassword(keyNumber);
        if(responseDTO.getStatus().equals("Success")){
            logger.info("Send token reset password : " + keyNumber) ;
            return ResponseEntity.ok().body(responseDTO);
        }
        return ResponseEntity.badRequest().body(responseDTO);
        }
        
    @PostMapping("reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody Map<String, String> mapPassword,
            @RequestAttribute("keyNumber") int keyNumber) {
            if(mapPassword.size() == 0 || mapPassword.get("password") == null){
                    String errorMessage = "Password required";
                    return ResponseEntity.badRequest().body(new ResponseDTO("Failed", errorMessage,""));
            }
            String password = mapPassword.get("password");  
            authenticationService.resetPassword(keyNumber, password);
            logger.info("Reset password : " + password ) ;
            return ResponseEntity.ok().body(new ResponseDTO("True", "Cập nhật mậu khẩu thành công", ""));
    }
}
