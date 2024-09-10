package com.mk_he.Wunderlist.service.impl;

import com.mk_he.Wunderlist.config.validator.PasswordContextValidator;
import com.mk_he.Wunderlist.config.validator.PasswordLengthValidator;
import com.mk_he.Wunderlist.config.validator.UsernameLengthValidator;
import com.mk_he.Wunderlist.config.validator.Validator;
import com.mk_he.Wunderlist.service.ValidationService;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {
    private final Validator<String> usernameValidatorChain;
    private final Validator<String> passwordValidatorChain;

    public ValidationServiceImpl() {
        this.usernameValidatorChain = new UsernameLengthValidator();

        this.passwordValidatorChain = new PasswordLengthValidator()
                .linkWith(new PasswordContextValidator());

    }

    @Override
    public void validateUsername(String username) {
        usernameValidatorChain.validate(username);
    }

    @Override
    public void validatePassword(String password) {
        passwordValidatorChain.validate(password);
    }

}
