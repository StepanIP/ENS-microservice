package com.example.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class WriterRequest {

    private List<String> contacts = new ArrayList<>();

    private String ownerName;

    private static final Logger LOGGER = LoggerFactory.getLogger(WriterRequest.class);

    public void getFromFile(MultipartFile file) throws IOException {
        List<String> writeRequests = new ArrayList<>();

        if (!file.isEmpty() && (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx"))) {
            try (InputStream inputStream = file.getInputStream()) {
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    contacts.add(row.getCell(0).getStringCellValue());
                }
            }
            LOGGER.info("Contacts got successfully.");
        } else {
            LOGGER.error("Invalid file or format.");
            throw new IllegalArgumentException("Invalid file or format.");
        }
    }
}
