package com.jackob101.rekrutacja.service.definition;

import com.jackob101.rekrutacja.exception.StatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validator;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseService<T> {


    private final SmartValidator smartValidator;
    private final String objectName;

    public BaseService(Validator validator, String objectName) {
        this.smartValidator = new SpringValidatorAdapter(validator);
        this.objectName = objectName;
    }

    protected void validate(T entity, Object... groups) {

        if (entity == null) {
            log.warn("Operation failed");
            throw new StatusException(objectName + " can't be null", HttpStatus.BAD_REQUEST);
        }

        MapBindingResult errors = new MapBindingResult(new HashMap<>(), objectName);

        smartValidator.validate(entity, errors, groups);

        if (errors.hasErrors()) {

            String errorMessage = errors.getFieldErrors()
                    .stream()
                    .map(fieldError -> " <-> " + fieldError.getDefaultMessage()).collect(Collectors.joining());

            log.warn("Operation failed");
            throw new StatusException("Validation for " + objectName + " failed on fields: " + errorMessage, HttpStatus.BAD_REQUEST);
        }

    }

    protected void checkId(Long id) {
        if (id == null) {
            log.warn("Operation failed");
            throw new StatusException("ID cannot be null", HttpStatus.BAD_REQUEST);
        }
    }

}
