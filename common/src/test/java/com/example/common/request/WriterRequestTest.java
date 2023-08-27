package com.example.common.request;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WriterRequestTest {

    @Test
    void testGetFromFile_ValidFile() throws Exception {
        MockMultipartFile file = this.createTempExcelFile();

        WriterRequest writerRequest = new WriterRequest();
        writerRequest.getFromFile(file);

        List<String> expectedContacts = Arrays.asList("john.doe@example.com", "jane.smith@example.com");
        assertEquals(expectedContacts, writerRequest.getContacts());
        assertNull(writerRequest.getOwnerName());
    }

    @Test
    void testGetFromFile_InvalidFileFormat() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE, getClass().getResourceAsStream("/test.txt"));

        WriterRequest writerRequest = new WriterRequest();

        assertThrows(IllegalArgumentException.class, () -> writerRequest.getFromFile(file));
    }

    private MockMultipartFile createTempExcelFile() throws Exception {
        File tempFile;
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            Row row1 = sheet.createRow(1);
            Cell cell1 = row1.createCell(0);
            cell1.setCellValue("john.doe@example.com");

            Row row2 = sheet.createRow(2);
            Cell cell4 = row2.createCell(0);
            cell4.setCellValue("jane.smith@example.com");

            tempFile = File.createTempFile("file", ".xlsx");
            try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
                workbook.write(fileOutputStream);
            }
        }
        return new MockMultipartFile("file", "test.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE, new FileInputStream(tempFile));
    }
}

