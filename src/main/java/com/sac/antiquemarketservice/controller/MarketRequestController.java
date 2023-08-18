package com.sac.antiquemarketservice.controller;

import com.sac.antiquemarketservice.dao.MarketRequestDao;
import com.sac.antiquemarketservice.exception.CommonResponse;
import com.sac.antiquemarketservice.service.MarketRequestService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/market-request")
public class MarketRequestController {

    private final MarketRequestService marketRequestService;

    public MarketRequestController(MarketRequestService marketRequestService) {
        this.marketRequestService = marketRequestService;
    }

    @GetMapping(path = "/{status}", produces = {"application/json"})
    public CommonResponse getMarketRequestList(@PathVariable("status") String status) {
        return marketRequestService.getMarketRequestList(status);
    }

    @PostMapping(path = "/create", produces = {"application/json"}, consumes = {"application/json"})
    public CommonResponse createMarketRequest(@RequestBody MarketRequestDao marketRequest) {
        return marketRequestService.createMarketRequest(marketRequest);
    }

    @PostMapping(path = "/approve", produces = {"application/json"}, consumes = {"application/json"})
    public CommonResponse approveMarketRequest(@RequestBody MarketRequestDao marketRequest) {
        return marketRequestService.approveMarketRequest(marketRequest);
    }
}
