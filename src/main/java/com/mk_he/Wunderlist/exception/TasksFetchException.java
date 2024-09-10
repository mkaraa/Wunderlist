package com.mk_he.Wunderlist.exception;

public class TasksFetchException extends BaseException {
    public static final String ERROR_KEY = "error.tasks-not-fetch";
    public static final String ERROR_MESSAGE = "Tasks cannot be fetched";

    public TasksFetchException(Object... args) {
        super(ERROR_KEY, ERROR_MESSAGE, args);
    }
}
