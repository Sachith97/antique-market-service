package com.sac.antiquemarketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@Data
@Builder
@AllArgsConstructor
public class MarketCreateRequestDao {

    private String userWalletHash;
    private String artifactName;
    private String artifactDescription;
    private MultipartFile imageOne;
    private MultipartFile imageTwo;
    private MultipartFile imageThree;
    private MultipartFile imageFour;
    private MultipartFile imageFive;
    private MultipartFile video;
    private String approvedStatus;
    private String requestHash;
}
