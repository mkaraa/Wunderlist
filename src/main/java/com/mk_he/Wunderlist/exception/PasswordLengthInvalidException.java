package com.mk_he.Wunderlist.exception;

public class PasswordLengthInvalidException extends BaseException {
    public static final String ERROR_KEY = "error.password-length-invalid";
    public static final String ERROR_MESSAGE = "Password length should be between 8 and 20";

    public PasswordLengthInvalidException(Object... args) {
        super(ERROR_KEY, ERROR_MESSAGE, args);
    }
}
