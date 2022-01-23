package com.jackob101.rekrutacja.service.definition;

import org.springframework.validation.MapBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validator;
import java.util.HashMap;
import java.util.stream.Collectors;

public abstract class IBaseService<T> {


    private final SmartValidator smartValidator;
    private final String objectName;

    public IBaseService(Validator validator, String objectName) {
        this.smartValidator = new SpringValidatorAdapter(validator);
        this.objectName = objectName;
    }

    protected void validate(T entity, Object... groups) {

        if (entity == null)
            throw new RuntimeException(objectName + " can't be null");

        MapBindingResult errors = new MapBindingResult(new HashMap<>(), objectName);

        smartValidator.validate(entity, errors, groups);

        if (errors.hasErrors()) {

            String errorMessage = errors.getFieldErrors().stream().map(fieldError -> "\n-> " + fieldError.getDefaultMessage()).collect(Collectors.joining());

            throw new RuntimeException("Validation for" + objectName + " failed on fields: " + errorMessage);
        }


    }

     protected void checkId(Long id) {
        if (id == null)
            throw new RuntimeException("ID cannot be null");
    }

}
