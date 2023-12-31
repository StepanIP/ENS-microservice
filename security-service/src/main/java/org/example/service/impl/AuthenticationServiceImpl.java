package org.example.service.impl;

import com.example.common.model.User;
import com.example.common.repository.RoleRepository;
import com.example.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.SignUpRequest;
import org.example.dto.request.SigninRequest;
import org.example.dto.response.JwtAuthenticationResponse;
import org.example.service.AuthenticationService;
import org.example.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        User user = User.builder().name(request.getFirstName()).surname(request.getLastName())
                .email(request.getUsername()).password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findByName("USER").get()).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByEmail(request.getUsername());
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}