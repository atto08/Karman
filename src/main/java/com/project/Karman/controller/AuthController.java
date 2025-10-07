package com.project.Karman.controller;

import com.project.Karman.dto.request.LoginRequestDto;
import com.project.Karman.dto.request.SignupRequestDto;
import com.project.Karman.dto.response.ApiResponse;
import com.project.Karman.dto.response.JwtTokenDto;
import com.project.Karman.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.project.Karman.exception.SuccessMessage.LOGIN;
import static com.project.Karman.exception.SuccessMessage.SIGNUP;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        authService.signup(signupRequestDto);
        return ResponseEntity.status(SIGNUP.getHttpStatus()).body(ApiResponse.success(SIGNUP.getMessage()));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtTokenDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        JwtTokenDto token = authService.login(loginRequestDto);
        return ResponseEntity.status(LOGIN.getHttpStatus()).body(ApiResponse.success(LOGIN.getMessage(), token));
    }
}
