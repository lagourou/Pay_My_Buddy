package com.pay_my_buddy.OC_P6.exception;

public class SelfTransferredAmountException extends RuntimeException {
    public SelfTransferredAmountException(String message) {
        super(message);
    }
}
