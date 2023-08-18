package com.sac.antiquemarketservice.service;

import com.sac.antiquemarketservice.dao.MarketCreateRequestDao;
import com.sac.antiquemarketservice.exception.CommonResponse;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
public interface MarketRequestService {

    CommonResponse getMarketRequestList(String status);

    CommonResponse createMarketRequest(MarketCreateRequestDao marketRequest);

    CommonResponse approveMarketRequest(MarketCreateRequestDao marketRequest);
}
