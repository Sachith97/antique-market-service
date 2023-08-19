package com.sac.antiquemarketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
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
    private String nftMarketAddress;
    private String nftTokenId;
    private String approvedStatus;
    private String approvedDate;
    private String requestHash;
}
