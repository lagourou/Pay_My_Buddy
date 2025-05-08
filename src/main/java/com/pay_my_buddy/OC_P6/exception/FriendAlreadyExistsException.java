package com.pay_my_buddy.OC_P6.exception;

public class FriendAlreadyExistsException extends RuntimeException {
    public FriendAlreadyExistsException(String message) {
        super(message);
    }
}