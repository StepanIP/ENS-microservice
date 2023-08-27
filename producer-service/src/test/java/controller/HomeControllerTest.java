package controller;

import com.example.common.CommonApplication;
import com.example.common.model.Contact;
import com.example.common.model.Notification;
import com.example.common.model.Role;
import com.example.common.model.User;
import com.example.common.request.DataRequest;
import com.example.common.service.ContactService;
import com.example.common.service.NotificationService;
import com.example.common.service.RoleService;
import com.example.common.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.SecurityServiceApplication;
import org.example.configuration.email.MailConfiguration;
import org.example.service.DataRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CommonApplication.class, SecurityServiceApplication.class})
@AutoConfigureMockMvc
public class HomeControllerTest extends ControllerTestClass{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ContactService contactService;

    @Autowired
    private MailConfiguration mailConfiguration;

    @MockBean
    private DataRequestService dataRequestService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;


    @Test
    @Transactional
    @WithMockUser(username = "test@gmail.com", password = "5b2h1k", roles = "USER")
    public void homeControllerTest_Get() throws Exception {
        List<Notification> notifications = Arrays.asList(
                new Notification("1", "Notification 1"),
                new Notification("2", "Notification 2")
        );

        Role role = new Role("USER");
        roleService.create(role);
        User user =  new User("Test", "Test", "test1@gmail.com", "5b2h1k1", role);

        List<Contact> contacts = Arrays.asList(
                new Contact(mailConfiguration.getUsername(),user),
                new Contact(mailConfiguration.getUsername(),user)
        );

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("notifications", notifications);
        expectedResponse.put("users", contacts);

        when(notificationService.getAll()).thenReturn(notifications);
        when(contactService.getAll()).thenReturn(contacts);

        mockMvc.perform(get("/api/v1//ENS-Ukraine"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(expectedResponse)));
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "5b2h1k", roles = "USER")
    public void sendMessageSuccessful_Post() throws Exception {
        DataRequest validDataRequest = new DataRequest("scrupnichuk@gmail.com", "Test");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(validDataRequest);

        requestBody = requestBody.replaceAll("[\\[\\]]", "");

        mockMvc.perform(post("/api/v1//ENS-Ukraine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "5b2h1k", roles = "USER")
    public void sendMessageServerError_Post() throws Exception {
        DataRequest invalidDataRequest = new DataRequest();

        mockMvc.perform(post("/api/v1//ENS-Ukraine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDataRequest)))
                .andExpect(status().is5xxServerError());

        verify(dataRequestService, never()).sendDataRequest(any(DataRequest.class));
    }
}
