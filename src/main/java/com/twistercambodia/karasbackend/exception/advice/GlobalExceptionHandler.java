package com.twistercambodia.karasbackend.exception.advice;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.exception.dto.ExceptionResponse;
import com.twistercambodia.karasbackend.exception.dto.ExceptionType;
import com.twistercambodia.karasbackend.inventory.exception.InvalidVariableProduct;
import com.twistercambodia.karasbackend.inventory.exception.InvalidVariableUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        this.logger.error("Throwing DataIntegrityViolationException with message={}", exception.getMessage());

        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ExceptionType.DataIntegrityViolation,
                "Invalid Data"
        );
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundException exception) {
        this.logger.error("Throwing NotFoundException with message={}", exception.getMessage());
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND.value(),
                ExceptionType.DataNotFound,
                exception.getMessage()
        );
    }

    @ExceptionHandler(value = InvalidVariableProduct.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidVariableProduct(InvalidVariableProduct exception) {
        this.logger.error("Throwing InvalidVariableProductException with message={}", exception.getMessage());

        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ExceptionType.InvalidVariableProduct,
                "Invalid Data"
        );
    }


    @ExceptionHandler(value = InvalidVariableUnit.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidVariableUnit(InvalidVariableUnit exception) {
        this.logger.error("Throwing InvalidVariableUnitException with message={}", exception.getMessage());

        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ExceptionType.InvalidVariableUnit,
                "Invalid Data"
        );
    }
}
