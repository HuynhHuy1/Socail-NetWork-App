package com.example.code.controller;



import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.example.code.dto.ResponseDTO;
import com.example.code.exception.ExistException;
import com.example.code.exception.IncorrectException;
import com.example.code.exception.NotFoundException;
import com.example.code.exception.NullException;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger logger = LogManager.getLogger(ExceptionController.class);

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        logger.error("DataAccessException occurred: {}", ex.getMessage());

        ResponseDTO responseDTO = new ResponseDTO("Failed", "INTERNAL SERVER ERROR", null);
        return ResponseEntity.ok().body(responseDTO);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseDTO> handleSQLException(SQLException ex) {
        logger.error("SQLException occurred: {}", ex.getMessage());
        ResponseDTO responseDTO = new ResponseDTO("Failed", "INTERNAL SERVER ERROR" + "", null);
        return ResponseEntity.ok().body(responseDTO);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO> handleException(HttpMessageNotReadableException ex){
        logger.error("Exception occurred: {}", ex.getMessage());
        ResponseDTO responseDTO = new ResponseDTO("Failed", "BAD REQUEST", null);
        return ResponseEntity.ok().body(responseDTO);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDTO> handleException(HttpRequestMethodNotSupportedException ex) {
        logger.error("Exception occurred: {}", ex.getMessage());
        ResponseDTO responseDTO = new ResponseDTO("Failed", "NOT FOUND", null);
        return ResponseEntity.ok().body(responseDTO);
    }
    
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseDTO> handleException(MultipartException ex) {
        logger.error("Exception occurred: {}", ex.getMessage());
        ResponseDTO responseDTO = new ResponseDTO("Failed", "BAD REQUEST", null);
        return ResponseEntity.ok().body(responseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleException(Exception ex) {
        logger.error("Exception occurred: {}", ex.getMessage());
        ResponseDTO responseDTO = new ResponseDTO("Failed", "INTERNAL SERVER ERROR", null);
        return ResponseEntity.ok().body(responseDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());

        ResponseDTO responseDTO = new ResponseDTO("Failed", "Valid exception", null);
        return ResponseEntity.ok().body(responseDTO);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDTO> handleNotFoundException(NotFoundException ex) {
        logger.error("NotFoundException occurred: {}", ex.error);

        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.ok().body(responseDTO);
    }

    @ExceptionHandler(ExistException.class)
    public ResponseEntity<ResponseDTO> handleExistException(ExistException ex) {
        logger.error("ExistException occurred: {}", ex.error);

        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.ok().body(responseDTO);
    }

    @ExceptionHandler(IncorrectException.class)
    public ResponseEntity<ResponseDTO> handleIncorrectException(IncorrectException ex) {
        logger.error("IncorrectException occurred: {}", ex.error);

        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.ok().body(responseDTO);
    }

    @ExceptionHandler(NullException.class)
    public ResponseEntity<ResponseDTO> handleNullException(NullException ex) {
        logger.error("NullException occurred: {}", ex.error);
        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.ok().body(responseDTO);
    }
}
