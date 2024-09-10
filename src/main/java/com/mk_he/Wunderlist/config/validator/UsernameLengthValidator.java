package com.mk_he.Wunderlist.config.validator;

import com.mk_he.Wunderlist.exception.UsernameLengthInvalidException;

public class UsernameLengthValidator extends Validator<String> {
    @Override
    public void validate(String username) {
        if (username.length() < 6) {
            throw new UsernameLengthInvalidException(username);
        }
        checkNext(username);
    }
}
