package com.danilo.zeparceiros.exceptions;

import com.danilo.zeparceiros.dtos.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleException(Exception e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), "500");
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(PartnerNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handlePartnerNotFoundException(PartnerNotFoundException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), "404");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AreaNotCoveredException.class)
    public ResponseEntity<ExceptionDTO> handleAreaNotCoveredException(AreaNotCoveredException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), "404");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }
}
