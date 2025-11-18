package com.bankingsystem.exception;

import com.bankingsystem.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.stream.Collectors;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleNotFound(AccountNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse<>(404, ex.getMessage(),
                null), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({InvalidAmountException.class,
            InsufficientBalanceException.class})
    public ResponseEntity<ApiResponse<Object>>
    handleBadRequest(RuntimeException ex) {
        return new ResponseEntity<>(new ApiResponse<>(400, ex.getMessage(),
                null), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(err-> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(new ApiResponse<>(400, msg, null),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleOther(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(500, ex.getMessage(),
                null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
