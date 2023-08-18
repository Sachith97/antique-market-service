package com.sac.antiquemarketservice.service;

import com.sac.antiquemarketservice.dao.MarketRequestDao;
import com.sac.antiquemarketservice.exception.CommonResponse;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
public interface MarketRequestService {

    CommonResponse getMarketRequestList(String status);

    CommonResponse createMarketRequest(MarketRequestDao marketRequest);

    CommonResponse approveMarketRequest(MarketRequestDao marketRequest);
}
