package com.paymentgateway.infra;

import com.paymentgateway.dtos.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity treateDuplicateEntry(DataIntegrityViolationException exception) {
    ExceptionDTO exceptionDTO = new ExceptionDTO("Usuário já cadastrado", "400");
    return ResponseEntity.badRequest().body(exceptionDTO);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity treate404(EntityNotFoundException exception) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity treateGeneralException(Exception exception) {
    ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), "500");
    return ResponseEntity.internalServerError().body(exceptionDTO);
  }

}
