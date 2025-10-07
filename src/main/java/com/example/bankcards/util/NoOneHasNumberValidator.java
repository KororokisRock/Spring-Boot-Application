package com.example.bankcards.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.bankcards.repository.CardRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class NoOneHasNumberValidator implements ConstraintValidator<NoOneHasNumber, String> {
    @Autowired
    private CardRepository cardRepo;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !cardRepo.existsByCardNumber(value);
    }
    
}
