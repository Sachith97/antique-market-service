package com.sac.antiquemarketservice.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sachith Harshamal
 * @created 2023-08-18
 */
public interface FileHandleService {

    String getFileURL(MultipartFile file);
}
