package com.example.code.controller;



import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseDTO> handleSQLException(SQLException ex) {
        logger.error("SQLException occurred: {}", ex.getMessage());

        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.getErrorCode() + "", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());

        ResponseDTO responseDTO = new ResponseDTO("Failed", "Valid exception", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleException(Exception ex) {
        logger.error("Exception occurred: {}", ex.getMessage());

        ResponseDTO responseDTO = new ResponseDTO("Failed", "Exception", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDTO> handleNotFoundException(NotFoundException ex) {
        logger.error("NotFoundException occurred: {}", ex.error);

        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }

    @ExceptionHandler(ExistException.class)
    public ResponseEntity<ResponseDTO> handleExistException(ExistException ex) {
        logger.error("ExistException occurred: {}", ex.error);

        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }

    @ExceptionHandler(IncorrectException.class)
    public ResponseEntity<ResponseDTO> handleIncorrectException(IncorrectException ex) {
        logger.error("IncorrectException occurred: {}", ex.error);

        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }

    @ExceptionHandler(NullException.class)
    public ResponseEntity<ResponseDTO> handleNullException(NullException ex) {
        logger.error("NullException occurred: {}", ex.error);
        ResponseDTO responseDTO = new ResponseDTO("Failed", ex.error, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
}
