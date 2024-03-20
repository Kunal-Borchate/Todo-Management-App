package com.qsp.todo.service;

import com.qsp.todo.dto.JwtAuthResponse;
import com.qsp.todo.dto.LoginDto;
import com.qsp.todo.dto.RegisterDto;

public interface AuthService {

    String register(RegisterDto registerDto);

    JwtAuthResponse login(LoginDto loginDto);
}
