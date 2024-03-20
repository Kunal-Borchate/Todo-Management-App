package com.qsp.todo.controller;

import com.qsp.todo.dto.JwtAuthResponse;
import com.qsp.todo.dto.LoginDto;
import com.qsp.todo.dto.RegisterDto;
import com.qsp.todo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response=authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        JwtAuthResponse jwtAuthResponse=authService.login(loginDto);

        return new ResponseEntity<>(jwtAuthResponse,HttpStatus.OK);
    }
}
