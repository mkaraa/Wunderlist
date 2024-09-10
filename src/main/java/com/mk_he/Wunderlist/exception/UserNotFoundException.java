package com.mk_he.Wunderlist.exception;

public class UserNotFoundException extends BaseException {
    public static final String ERROR_KEY = "error.user-not-found";
    public static final String ERROR_MESSAGE = "User not found.";

    public UserNotFoundException(Object... args) {
        super(ERROR_KEY, ERROR_MESSAGE, args);
    }
}
