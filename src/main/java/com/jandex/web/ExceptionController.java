package com.jandex.web;

import com.jandex.dto.ErrorDTO;
import com.jandex.exception.IncorrectDataException;
import com.jandex.exception.NotFoundDataException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IncorrectDataException.class)
    public ResponseEntity<ErrorDTO> incorrectData(IncorrectDataException exception) {
        return new ResponseEntity<>(
                ErrorDTO.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Невалидная схема документа или входные данные не верны.")
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundDataException.class)
    public ResponseEntity<ErrorDTO> notFoundDataException(NotFoundDataException exception) {
        return new ResponseEntity<>(
                ErrorDTO.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message("Категория/товар не найден.")
                        .build(),
                HttpStatus.NOT_FOUND);
    }
}
