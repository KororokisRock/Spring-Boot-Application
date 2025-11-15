package com.example.bankcards.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.bankcards.repository.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private final UserRepository userRepo;

    private static final Logger logger = LoggerFactory.getLogger(UniqueUsernameValidator.class);

    public UniqueUsernameValidator(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext ) {
        if (value == null) {
            return true;
        }
        logger.info("Current field: " + value);
        boolean existsInDb = userRepo.existsByUsername(value);
        logger.info("Exist in db: " + existsInDb);
        return !existsInDb;
    }
}
