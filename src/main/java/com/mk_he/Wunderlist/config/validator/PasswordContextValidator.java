package com.mk_he.Wunderlist.config.validator;

import com.mk_he.Wunderlist.exception.PasswordContextInvalidException;

public class PasswordContextValidator extends Validator<String> {
    @Override
    public void validate(String password) {
        if (!password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*")) {
            throw new PasswordContextInvalidException();
        }
        checkNext(password);
    }
}
