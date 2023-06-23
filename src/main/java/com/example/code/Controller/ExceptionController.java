package com.example.code.controller;

import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.code.dto.ResponseDTO;
import com.example.code.exception.ExistedException;
import com.example.code.exception.IncorrectException;
import com.example.code.exception.NotFoundException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        ResponseDTO responseDTO = new ResponseDTO("Failed", "DataAccessException", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseDTO> handleSQLException(SQLException ex) {
        ResponseDTO responseDTO = new ResponseDTO("Failed", "SQL Exception", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentNotValidException() {
        ResponseDTO responseDTO = new ResponseDTO("Failed", "Valid exception", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleException(Exception ex) {
        ResponseDTO responseDTO = new ResponseDTO("Failed", "Exception", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDTO> handleNotFoundException(NotFoundException ex) {
        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDTO> handleExistException(ExistedException ex) {
        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }

    @ExceptionHandler(IncorrectException.class)
    public ResponseEntity<ResponseDTO> handleIncorrectException(IncorrectException ex) {
        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
}
