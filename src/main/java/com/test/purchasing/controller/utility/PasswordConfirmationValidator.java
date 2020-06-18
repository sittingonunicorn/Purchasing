package com.test.purchasing.controller.utility;

import com.test.purchasing.model.dto.UserRegistrationDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmationValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        UserRegistrationDTO user = (UserRegistrationDTO) o;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}
