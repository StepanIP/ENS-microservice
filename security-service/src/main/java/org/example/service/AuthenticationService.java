package org.example.service;


import org.example.dto.request.SignUpRequest;
import org.example.dto.request.SigninRequest;
import org.example.dto.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}