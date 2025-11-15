package com.example.bankcards.util;

import org.springframework.stereotype.Component;

import com.example.bankcards.repository.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UserWithIdExistValidator implements ConstraintValidator<UserWithIdExist, Integer> {
    private final UserRepository userRepo;

    public UserWithIdExistValidator(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext ) {
        return value == null || userRepo.existsById(value);
    }
}
