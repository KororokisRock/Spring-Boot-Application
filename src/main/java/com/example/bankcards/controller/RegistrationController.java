package com.example.bankcards.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.dto.RegistrationDTO;
import com.example.bankcards.exception.AuthenticationFailedException;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.ValidateBindingResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/register")
@Tag(name = "Registration", description = "User registration APIs")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Register new user", description = "Create a new user account")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Validation error or passwords don't match")
    @ValidateBindingResult
    public ResponseEntity<?> setRegistration(@RequestBody @Valid RegistrationDTO registerDTO, BindingResult result) throws AuthenticationFailedException {
        userService.registerUser(registerDTO);
        return ResponseEntity.ok("Registration complete");
    }
}
