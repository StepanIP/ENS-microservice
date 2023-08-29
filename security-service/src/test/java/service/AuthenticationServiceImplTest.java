package service;

import com.example.common.model.Role;
import com.example.common.model.User;
import com.example.common.repository.RoleRepository;
import com.example.common.repository.UserRepository;
import org.example.SecurityServiceApplication;
import org.example.dto.request.SignUpRequest;
import org.example.dto.request.SigninRequest;
import org.example.dto.response.JwtAuthenticationResponse;
import org.example.service.JwtService;
import org.example.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes= SecurityServiceApplication.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void testSignup() {
        SignUpRequest signUpRequest = new SignUpRequest("John", "Doe", "johndoe@example.com", "password");

        Role role = new Role();
        role.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(java.util.Optional.of(role));

        User savedUser = new User();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        when(jwtService.generateToken(any(User.class))).thenReturn("generated-token");

        JwtAuthenticationResponse response = authenticationService.signup(signUpRequest);

        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    public void testSignin() {
        SigninRequest signinRequest = new SigninRequest("johndoe@example.com", "password");
        Role role = new Role();
        role.setName("USER");

        User user = new User("Name", "Surname",
                "johndoe@example.com", "encoded-password", role);

        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user);

        authenticationService.signin(signinRequest);

        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken("johndoe@example.com", "password"));
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
    }
}
