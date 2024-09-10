package com.mk_he.Wunderlist.exception;

public class UsersFetchDataBusinessException extends BaseException {
    public static final String ERROR_KEY = "error.user-only-fetch-own-data";
    public static final String ERROR_MESSAGE = "Authenticated users only fetch own data";

    public UsersFetchDataBusinessException(Object... args) {
        super(ERROR_KEY, ERROR_MESSAGE, args);
    }
}
