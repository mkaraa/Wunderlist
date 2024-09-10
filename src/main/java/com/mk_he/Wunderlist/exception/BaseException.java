package com.mk_he.Wunderlist.exception;

public abstract class BaseException extends RuntimeException {

    private final String key;
    private final String message;
    private Object[] args;


    public BaseException(String key, String message, Object[] args) {
        this.key = key;
        this.message = message;
        this.args = args;
    }

    public BaseException(String key, String message) {
        this.key = key;
        this.message = message;
    }


}
