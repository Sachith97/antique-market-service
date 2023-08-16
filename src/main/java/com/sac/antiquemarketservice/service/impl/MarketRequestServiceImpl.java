package com.sac.antiquemarketservice.service.impl;

import com.sac.antiquemarketservice.dao.MarketRequestDao;
import com.sac.antiquemarketservice.enums.ApprovalStatus;
import com.sac.antiquemarketservice.enums.Response;
import com.sac.antiquemarketservice.enums.UserRole;
import com.sac.antiquemarketservice.exception.CommonResponse;
import com.sac.antiquemarketservice.model.MarketRequest;
import com.sac.antiquemarketservice.model.User;
import com.sac.antiquemarketservice.repository.MarketRequestRepository;
import com.sac.antiquemarketservice.service.MarketRequestService;
import com.sac.antiquemarketservice.service.UserService;
import com.sac.antiquemarketservice.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
@Service
public class MarketRequestServiceImpl implements MarketRequestService {

    private final MarketRequestRepository marketRequestRepository;
    private final UserService userService;

    public MarketRequestServiceImpl(MarketRequestRepository marketRequestRepository, UserService userService) {
        this.marketRequestRepository = marketRequestRepository;
        this.userService = userService;
    }

    @Override
    public CommonResponse createMarketRequest(MarketRequestDao marketRequest) {
        if (!ValidationUtil.stringHasContent(marketRequest.getUserWalletHash()) ||
                !ValidationUtil.stringHasContent(marketRequest.getArtifactName()) ||
                !ValidationUtil.stringHasContent(marketRequest.getArtifactDescription()) ||
                !ValidationUtil.stringHasContent(marketRequest.getImageOneAddress()) ||
                !ValidationUtil.stringHasContent(marketRequest.getImageTwoAddress()) ||
                !ValidationUtil.stringHasContent(marketRequest.getImageThreeAddress()) ||
                !ValidationUtil.stringHasContent(marketRequest.getVideoAddress())) {
            return new CommonResponse(Response.NOT_FOUND);
        }
        String requestHash = generateHash(marketRequest);
        Optional<MarketRequest> dbRequest = this.marketRequestRepository.findByRequestHash(requestHash);
        if (dbRequest.isPresent()) {
            return CommonResponse.builder()
                    .isOk(Boolean.FALSE)
                    .responseCode(111)
                    .responseMessage("Request already available")
                    .build();
        }
        this.marketRequestRepository.save(
                MarketRequest.builder()
                        .userWalletHash(marketRequest.getUserWalletHash())
                        .artifactName(marketRequest.getArtifactName())
                        .artifactDescription(marketRequest.getArtifactDescription())
                        .imageOneAddress(marketRequest.getImageOneAddress())
                        .imageTwoAddress(marketRequest.getImageTwoAddress())
                        .imageThreeAddress(marketRequest.getImageThreeAddress())
                        .imageFourAddress(marketRequest.getImageFourAddress())
                        .imageFiveAddress(marketRequest.getImageFiveAddress())
                        .videoAddress(marketRequest.getVideoAddress())
                        .approvalStatus(ApprovalStatus.PENDING)
                        .requestHash(requestHash)
                        .build()
        );
        return new CommonResponse(Response.SUCCESS, requestHash);
    }

    public static String generateHash(MarketRequestDao marketRequest) {
        String combinedString = marketRequest.getUserWalletHash() +
                marketRequest.getArtifactName() +
                marketRequest.getArtifactDescription() +
                marketRequest.getImageOneAddress() +
                marketRequest.getImageTwoAddress() +
                marketRequest.getImageThreeAddress() +
                marketRequest.getImageFourAddress() +
                marketRequest.getImageFiveAddress() +
                marketRequest.getVideoAddress();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combinedString.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    @Override
    public CommonResponse approveMarketRequest(MarketRequestDao marketRequest) {
        Optional<MarketRequest> dbRequest = this.marketRequestRepository.findByRequestHash(marketRequest.getRequestHash());
        if (!dbRequest.isPresent()) {
            return new CommonResponse(Response.NOT_FOUND);
        }
        Optional<User> loggedInUser = this.userService.getLoggedInUser();
        if (!loggedInUser.isPresent() || !loggedInUser.get().getRole().equals(UserRole.APPROVER.name())) {
            return new CommonResponse(Response.FORBIDDEN);
        }
        dbRequest.get().setApprovalStatus(ApprovalStatus.APPROVED);
        dbRequest.get().setApprovedUser(loggedInUser.get());
        this.marketRequestRepository.save(dbRequest.get());
        return new CommonResponse(Response.SUCCESS);
    }
}
