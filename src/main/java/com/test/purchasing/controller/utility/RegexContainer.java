package com.test.purchasing.controller.utility;

public interface RegexContainer {
    String PASSWORD_REGEX = "[a-zA-Z0-9]{8,20}";
    String EMAIL_REGEX = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    String NAME_REGEX = "[A-ZА-ЩҐЄІЇЮЯ][a-zа-щґєіїьюя']{1,20}";
}
