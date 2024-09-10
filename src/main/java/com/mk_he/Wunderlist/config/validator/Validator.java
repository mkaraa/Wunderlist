package com.mk_he.Wunderlist.config.validator;

public abstract class Validator<T> {
    private Validator<T> nextValidator;

    public Validator<T> linkWith(Validator<T> nextValidator) {
        this.nextValidator = nextValidator;
        return nextValidator;
    }

    public abstract void validate(T input);

    protected void checkNext(T input) {
        if (nextValidator != null) {
            nextValidator.validate(input);
        }
    }
}
