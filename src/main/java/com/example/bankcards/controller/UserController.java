package com.example.bankcards.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.dto.UsernameDTO;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.ValidateBindingResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/user")
@Tag(name = "Users", description = "User management APIs (ADMIN only)")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all users (ADMIN only)", description = "Retrieve list of all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @ValidateBindingResult
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsersAsDTO();
    }

    @PostMapping("/delete")
    @Operation(summary = "Delete user (ADMIN only)", description = "Delete user by username")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "400", description = "User not found")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @ValidateBindingResult
    public ResponseEntity<?> deleteUser(@RequestBody UsernameDTO username, BindingResult result) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok("User delete successfully");
    }
}
