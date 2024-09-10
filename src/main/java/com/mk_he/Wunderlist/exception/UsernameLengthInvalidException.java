package com.mk_he.Wunderlist.exception;

public class UsernameLengthInvalidException extends BaseException {
    public static final String ERROR_KEY = "error.username-length-invalid";
    public static final String ERROR_MESSAGE = "Username length should be grater than 6";

    public UsernameLengthInvalidException(Object... args) {
        super(ERROR_KEY, ERROR_MESSAGE, args);
    }
}
