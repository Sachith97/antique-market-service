package com.sac.antiquemarketservice.service.impl;

import com.sac.antiquemarketservice.service.FileHandleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Sachith Harshamal
 * @created 2023-08-18
 */
@Service
public class FileHandleServiceImpl implements FileHandleService {

    @Value("${file.upload.path}")
    private String FILE_UPLOAD_PATH;

    @Value("${server.application.path}")
    private String APP_PATH;

    @Value("${server.port}")
    private String SERVER_PORT;

    @Value("${server.servlet.context-path}")
    private String CONTEXT_PATH;

    @Override
    public String getFileURL(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            assert originalFileName != null;
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // Generate a new filename using the current datetime with milliseconds
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            String fileName = now.format(formatter) + fileExtension;

            String filePath = FILE_UPLOAD_PATH + fileName;
            file.transferTo(new File(filePath));

            // return generated url
            return APP_PATH + ":" + SERVER_PORT + CONTEXT_PATH + "/files/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
