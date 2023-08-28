package com.sac.antiquemarketservice.service;

import com.sac.antiquemarketservice.dao.MarketCreateRequestDao;
import com.sac.antiquemarketservice.dao.MarketRequestDao;
import com.sac.antiquemarketservice.enums.ApprovalStatus;
import com.sac.antiquemarketservice.exception.CommonResponse;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
public interface MarketRequestService {

    CommonResponse getMarketRequestList(String status);

    CommonResponse createMarketRequest(MarketCreateRequestDao marketRequest);

    CommonResponse withdrawMarketRequest(MarketRequestDao marketRequest);

    CommonResponse approveMarketRequest(MarketRequestDao marketRequest, ApprovalStatus approvalStatus);

    CommonResponse saveNFTInfo(MarketRequestDao marketRequest);
}
