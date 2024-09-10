package com.mk_he.Wunderlist.config.validator;

import com.mk_he.Wunderlist.exception.PasswordLengthInvalidException;

public class PasswordLengthValidator extends Validator<String> {
    @Override
    public void validate(String password) {
        if (password.length() < 8 || password.length() > 20) {
            throw new PasswordLengthInvalidException();
        }
        checkNext(password);
    }
}
