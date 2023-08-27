package controller;

import com.example.common.CommonApplication;
import com.example.common.model.Contact;
import com.example.common.request.ContactRequest;
import com.example.common.service.ContactService;
import com.example.common.service.UserService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.SecurityServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CommonApplication.class, SecurityServiceApplication.class})
@AutoConfigureMockMvc
@Transactional
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class ContactControllerTest extends ControllerTestClass{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Autowired
    UserService userService;

    @Test
    @WithMockUser(username = "test@gmail.com", password = "5b2h1k", roles = "USER")
    public void testAddContacts_ValidFile() throws Exception {
        File tempFile = createTempExcelFile();

        try (InputStream inputStream = Files.newInputStream(tempFile.toPath())) {
            MockMultipartFile file = new MockMultipartFile("file", "users.xlsx",
                    MediaType.MULTIPART_FORM_DATA_VALUE, inputStream);

            mockMvc.perform(multipart("/api/v1//ENS-Ukraine/contact/add")
                            .file(file))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "5b2h1k", roles = "USER")
    void testDeleteContact() throws Exception {
        ContactRequest contactRequest = new ContactRequest("12345");

        when(contactService.readByContact(contactRequest.getContact())).thenReturn(new Contact());

        mockMvc.perform(delete("/api/v1//ENS-Ukraine/contact/delete")
                        .content(asJsonString(contactRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(contactService, times(1)).delete(any(Contact.class));
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "5b2h1k", roles = "USER")
    void testEditContact() throws Exception {
        ContactRequest contactRequest = new ContactRequest("12345");

        when(contactService.readByContact(contactRequest.getContact())).thenReturn(new Contact());

        mockMvc.perform(put("/api/v1//ENS-Ukraine/contact/edit")
                        .content(asJsonString(contactRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(contactService, times(1)).update(any(Contact.class));
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "5b2h1k", roles = "USER")
    public void testAddContacts_InvalidFileFormat() throws Exception {
        File tempFile = File.createTempFile("file", ".txt");
        try (InputStream inputStream = Files.newInputStream(tempFile.toPath())) {
            MockMultipartFile file = new MockMultipartFile("file", "users.txt",
                    MediaType.MULTIPART_FORM_DATA_VALUE, inputStream);

            mockMvc.perform(multipart("/api/v1//ENS-Ukraine/contact/add")
                            .file(file))
                    .andExpect(status().isBadRequest())
                    .andExpect(view().name("error"))
                    .andExpect(model().attribute("code", "400 / Bad Request"))
                    .andExpect(model().attribute("message", "Invalid file or format."));
        }
    }


    @Test
    @WithMockUser(username = "test@gmail.com", password = "5b2h1k", roles = "USER")
    public void testAddContacts_EmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "empty.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/v1//ENS-Ukraine/contact/add")
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("code", "400 / Bad Request"))
                .andExpect(model().attribute("message", "Invalid file or format."));
    }


    private File createTempExcelFile() throws Exception {
        File tempFile = File.createTempFile("file", ".xlsx");
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Users");

                Row headerRow = sheet.createRow(0);
                Cell headerCell1 = headerRow.createCell(0);
                headerCell1.setCellValue("Email");

                Row row1 = sheet.createRow(1);
                Cell cell1 = row1.createCell(0);
                cell1.setCellValue("john.doe@example.com");

                Row row2 = sheet.createRow(2);
                Cell cell4 = row2.createCell(0);
                cell4.setCellValue("jane.smith@example.com");

                workbook.write(fileOutputStream);
            }
        }
        return tempFile;
    }
}
