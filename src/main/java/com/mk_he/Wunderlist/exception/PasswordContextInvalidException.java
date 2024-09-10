package com.mk_he.Wunderlist.exception;

public class PasswordContextInvalidException extends BaseException {
    public static final String ERROR_KEY = "error.password-context-invalid";
    public static final String ERROR_MESSAGE = "Password should containt at least 1 lower and 1 upper case letter";

    public PasswordContextInvalidException(Object... args) {
        super(ERROR_KEY, ERROR_MESSAGE, args);
    }
}
