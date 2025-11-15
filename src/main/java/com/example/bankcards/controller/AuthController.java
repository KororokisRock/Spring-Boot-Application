package com.example.bankcards.controller;

import javax.naming.AuthenticationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.dto.JwtAuthenticationDTO;
import com.example.bankcards.dto.RefreshTokenDTO;
import com.example.bankcards.dto.UserCredentialsDTO;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.ValidateBindingResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication and token management APIs")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/sing-in")
    @Operation(summary = "User authentication", description = "Authenticate user and return JWT tokens")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
    @ApiResponse(responseCode = "403", description = "Authentication failed")
    @ValidateBindingResult
    public ResponseEntity<?> singIn(@RequestBody UserCredentialsDTO userCredentialsDTO) throws AuthenticationException {
        JwtAuthenticationDTO jwtAuthenticationDTO = userService.singIn(userCredentialsDTO);
        return ResponseEntity.ok(jwtAuthenticationDTO);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Refresh JWT token using refresh token")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid refresh token")
    @ValidateBindingResult
    public JwtAuthenticationDTO refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) throws Exception {
        return userService.refreshToken(refreshTokenDTO);
    }
}
