package com.mk_he.Wunderlist.exception;

public class TaskNotFoundException extends BaseException {
    public static final String ERROR_KEY = "error.task-not-found";
    public static final String ERROR_MESSAGE = "Task not found.";

    public TaskNotFoundException(Object... args) {
        super(ERROR_KEY, ERROR_MESSAGE, args);
    }
}
