package com.sac.antiquemarketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@Data
@Builder
@AllArgsConstructor
public class MarketRequestDao {

    private String userWalletHash;
    private String artifactName;
    private String artifactDescription;
    private String imageOneAddress;
    private String imageTwoAddress;
    private String imageThreeAddress;
    private String imageFourAddress;
    private String imageFiveAddress;
    private String videoAddress;
    private String approvedStatus;
    private String requestHash;
}
