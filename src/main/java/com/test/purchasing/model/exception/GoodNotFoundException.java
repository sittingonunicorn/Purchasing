package com.test.purchasing.model.exception;

public class GoodNotFoundException extends Exception {
    public GoodNotFoundException(Long goodId) {
        super("Good with id " + goodId + " not found");
    }
}
