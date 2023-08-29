package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.example.SecurityServiceApplication;
import org.example.dto.request.SignUpRequest;
import org.example.dto.request.SigninRequest;
import org.example.dto.response.JwtAuthenticationResponse;
import org.example.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes=SecurityServiceApplication.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void testSignup() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("Username", "Lastname", "test@gmail.com", "password");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse("token");

        when(authenticationService.signup(signUpRequest)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signUpRequest)))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).signup(signUpRequest);
    }

    @Test
    void testSignin() throws Exception {
        SigninRequest signinRequest = new SigninRequest("username", "password");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse("token");

        when(authenticationService.signin(signinRequest)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signinRequest)))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).signin(signinRequest);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
