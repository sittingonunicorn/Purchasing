package com.test.purchasing.model.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
//@PasswordMatches
public class UserRegistrationDTO {

//    @Pattern(regexp = EMAIL_REGEX, message = "wrongEmailFormat")
    private String email;

 //   @Pattern(regexp = PASSWORD_REGEX, message = "wrongPasswordFormat")
    private String password;

//    @Pattern(regexp = PASSWORD_REGEX, message = "wrongPasswordFormat")
//    private String confirmPassword;

}
