package com.example.bankcards.dto;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User credentials for authentication")
public class UserCredentialsDTO {
    @Schema(description = "Username", example = "Kororok", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message="Username is required")
    @Size(min=2, max=50, message="Username must be between 2 and 50 characters")
    private String username;
    @Schema(description = "Password", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message="Password is required")
    @Size(min=3, message="Password must be longer then 3 character")
    private String password;


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserCredentialsDTO(username=" + username + ", password=" + password + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCredentialsDTO that = (UserCredentialsDTO) o;
        return Objects.equals(username, that.getUsername()) &&
            Objects.equals(password, that.getPassword());
    }
}
