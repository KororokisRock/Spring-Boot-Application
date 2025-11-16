package com.example.bankcards.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.bankcards.dto.JwtAuthenticationDTO;
import com.example.bankcards.dto.RefreshTokenDTO;
import com.example.bankcards.dto.UserCredentialsDTO;
import com.example.bankcards.service.UserService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void singIn_ValidCredentials_ReturnsJwtTokens() throws Exception {
        UserCredentialsDTO credentials = new UserCredentialsDTO();
        credentials.setUsername("testuser");
        credentials.setPassword("password");

        JwtAuthenticationDTO expectedResponse = new JwtAuthenticationDTO();
        expectedResponse.setToken("accessToken");
        expectedResponse.setRefreshToken("refreshToken");

        when(userService.singIn(any(UserCredentialsDTO.class))).thenReturn(expectedResponse);

        JwtAuthenticationDTO response = authController.singIn(credentials);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(userService).singIn(credentials);
    }

    @Test
    void singIn_ServiceThrowsException_PropagatesException() throws Exception {
        UserCredentialsDTO credentials = new UserCredentialsDTO();
        credentials.setUsername("testuser");
        credentials.setPassword("password");

        when(userService.singIn(any(UserCredentialsDTO.class))).thenThrow(new RuntimeException("Auth failed"));

        assertThrows(RuntimeException.class, () -> authController.singIn(credentials));
        verify(userService).singIn(credentials);
    }

    @Test
    void refresh_ValidRefreshToken_ReturnsNewTokens() throws Exception {
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenDTO.setRefreshToken("validRefreshToken");

        JwtAuthenticationDTO expectedResponse = new JwtAuthenticationDTO();
        expectedResponse.setToken("newAccessToken");
        expectedResponse.setRefreshToken("validRefreshToken");

        when(userService.refreshToken(any(RefreshTokenDTO.class))).thenReturn(expectedResponse);

        JwtAuthenticationDTO response = authController.refresh(refreshTokenDTO);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(userService).refreshToken(refreshTokenDTO);
    }

    @Test
    void refresh_InvalidRefreshToken_PropagatesException() throws Exception {
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenDTO.setRefreshToken("invalidRefreshToken");

        when(userService.refreshToken(any(RefreshTokenDTO.class))).thenThrow(new RuntimeException("Invalid token"));

        assertThrows(RuntimeException.class, () -> authController.refresh(refreshTokenDTO));
        verify(userService).refreshToken(refreshTokenDTO);
    }

    @Test
    void refresh_NullRefreshToken_PropagatesException() throws Exception {
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenDTO.setRefreshToken(null);

        when(userService.refreshToken(any(RefreshTokenDTO.class))).thenThrow(new RuntimeException("Null token"));

        assertThrows(RuntimeException.class, () -> authController.refresh(refreshTokenDTO));
        verify(userService).refreshToken(refreshTokenDTO);
    }
}
