package controller;

import com.example.common.CommonApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.request.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = CommonApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ControllerTestClass {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void addAuthUser() throws Exception {

        SignUpRequest loginRequest = SignUpRequest.builder()
                .firstName("Test")
                .lastName("Test")
                .username("test@gmail.com")
                .password("5b2h1k")
                .build();

        String requestBody = asJsonString(loginRequest);

        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
