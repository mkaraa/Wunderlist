package com.mk_he.Wunderlist.exception;

public class UserAlreadyExistsException extends BaseException {
    public static final String ERROR_KEY = "error.user-already-exists";
    public static final String ERROR_MESSAGE = "User cannot register because already exists.";

    public UserAlreadyExistsException(Object... args) {
        super(ERROR_KEY, ERROR_MESSAGE, args);
    }
}
