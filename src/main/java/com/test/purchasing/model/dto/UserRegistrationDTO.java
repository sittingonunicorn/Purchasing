package com.test.purchasing.model.dto;

import com.test.purchasing.controller.utility.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.Pattern;

import static com.test.purchasing.controller.utility.RegexContainer.EMAIL_REGEX;
import static com.test.purchasing.controller.utility.RegexContainer.PASSWORD_REGEX;

@Data
@PasswordMatches
public class UserRegistrationDTO {

    @Pattern(regexp = EMAIL_REGEX, message = "wrongEmailFormat")
    private String email;

    @Pattern(regexp = PASSWORD_REGEX, message = "wrongPasswordFormat")
    private String password;

    @Pattern(regexp = PASSWORD_REGEX, message = "wrongPasswordFormat")
    private String confirmPassword;

}
