package com.test.purchasing.controller.utility;

public interface RegexContainer {
    String PASSWORD_REGEX = "[a-zA-Z0-9]{8,20}";
    String EMAIL_REGEX = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
}
