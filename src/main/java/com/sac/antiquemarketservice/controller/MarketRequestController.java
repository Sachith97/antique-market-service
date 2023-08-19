package com.sac.antiquemarketservice.controller;

import com.sac.antiquemarketservice.dao.MarketCreateRequestDao;
import com.sac.antiquemarketservice.dao.MarketRequestDao;
import com.sac.antiquemarketservice.exception.CommonResponse;
import com.sac.antiquemarketservice.service.MarketRequestService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping(path = {"/list", "/list/{status}"}, produces = {"application/json"})
    public CommonResponse getMarketRequestList(@PathVariable(required = false, name = "status") String status) {
        return marketRequestService.getMarketRequestList(status);
    }

    @PostMapping(path = "/create", produces = {"application/json"})
    public CommonResponse createMarketRequest(
            @RequestParam("userWalletHash") String userWalletHash,
            @RequestParam("artifactName") String artifactName,
            @RequestParam("artifactDescription") String artifactDescription,
            @RequestParam("imageOne") MultipartFile imageOne,
            @RequestParam("imageTwo") MultipartFile imageTwo,
            @RequestParam("imageThree") MultipartFile imageThree,
            @RequestParam(required = false, name = "imageFour") MultipartFile imageFour,
            @RequestParam(required = false, name = "imageFive") MultipartFile imageFive,
            @RequestParam("video") MultipartFile video) {
        return marketRequestService.createMarketRequest(MarketCreateRequestDao.builder()
                .userWalletHash(userWalletHash)
                .artifactName(artifactName)
                .artifactDescription(artifactDescription)
                .imageOne(imageOne)
                .imageTwo(imageTwo)
                .imageThree(imageThree)
                .imageFour(imageFour)
                .imageFive(imageFive)
                .video(video)
                .build()
        );
    }

    @PostMapping(path = "/approve", produces = {"application/json"}, consumes = {"application/json"})
    public CommonResponse approveMarketRequest(@RequestBody MarketRequestDao marketRequest) {
        return marketRequestService.approveMarketRequest(marketRequest);
    }

    @PostMapping(path = "/save-nft", produces = {"application/json"}, consumes = {"application/json"})
    public CommonResponse saveNFTInfo(@RequestBody MarketRequestDao marketRequest) {
        return marketRequestService.saveNFTInfo(marketRequest);
    }
}
