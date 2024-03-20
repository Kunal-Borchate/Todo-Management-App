package com.qsp.todo.service.impl;

import com.qsp.todo.dto.JwtAuthResponse;
import com.qsp.todo.dto.LoginDto;
import com.qsp.todo.dto.RegisterDto;
import com.qsp.todo.entity.Role;
import com.qsp.todo.entity.User;
import com.qsp.todo.exception.TodoAPIException;
import com.qsp.todo.repository.RoleRepository;
import com.qsp.todo.repository.UserRepository;
import com.qsp.todo.security.JwtTokenProvider;
import com.qsp.todo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public String register(RegisterDto registerDto) {

        //check username is already present in the database
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new TodoAPIException(HttpStatus.BAD_REQUEST,"Username already exists!");
        }

        //check email exists in the database or not

        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw  new TodoAPIException(HttpStatus.BAD_REQUEST,"Email already exists!");
        }

        User user=new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role>roles=new HashSet<>();
        Role userRole=roleRepository.findByName("ROLE_USER");
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return "User Registered Successfully!";
    }

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {

        Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=jwtTokenProvider.generateToken(authentication);

        Optional<User>userOptional =userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(),loginDto.getUsernameOrEmail());
        String role=null;
        if(userOptional.isPresent()){
            User loggedInUser=userOptional.get();
            Optional<Role>optionalRole=loggedInUser.getRoles().stream().findFirst();
            if (optionalRole.isPresent()){
                Role userRole=optionalRole.get();
                role=userRole.getName();
            }
        }

        JwtAuthResponse jwtAuthResponse=new JwtAuthResponse();
        jwtAuthResponse.setRole(role);
        jwtAuthResponse.setAccessToken(token);
        return jwtAuthResponse;
    }
}
