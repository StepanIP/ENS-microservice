package com.example.untitled3.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class AuthResponse {
    private String username;
    private String accessToken;
}