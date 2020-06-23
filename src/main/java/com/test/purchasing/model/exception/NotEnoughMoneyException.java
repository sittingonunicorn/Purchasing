package com.test.purchasing.model.exception;

import java.math.BigDecimal;

public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(BigDecimal balance, BigDecimal sum) {
        super("Not enough money.Balance: " + balance + ", order sum: " + sum);
    }
}
