package com.sac.antiquemarketservice.service.impl;

import com.sac.antiquemarketservice.dao.MarketCreateRequestDao;
import com.sac.antiquemarketservice.dao.MarketCreateResponseDao;
import com.sac.antiquemarketservice.enums.ApprovalStatus;
import com.sac.antiquemarketservice.enums.Response;
import com.sac.antiquemarketservice.enums.UserRole;
import com.sac.antiquemarketservice.exception.CommonResponse;
import com.sac.antiquemarketservice.model.MarketRequest;
import com.sac.antiquemarketservice.model.User;
import com.sac.antiquemarketservice.repository.MarketRequestRepository;
import com.sac.antiquemarketservice.service.FileHandleService;
import com.sac.antiquemarketservice.service.MarketRequestService;
import com.sac.antiquemarketservice.service.UserService;
import com.sac.antiquemarketservice.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
@Service
public class MarketRequestServiceImpl implements MarketRequestService {

    private final MarketRequestRepository marketRequestRepository;
    private final UserService userService;
    private final FileHandleService fileHandleService;

    public MarketRequestServiceImpl(MarketRequestRepository marketRequestRepository, UserService userService, FileHandleService fileHandleService) {
        this.marketRequestRepository = marketRequestRepository;
        this.userService = userService;
        this.fileHandleService = fileHandleService;
    }

    @Override
    public CommonResponse getMarketRequestList(String status) {
        List<MarketRequest> requestList = status != null ? marketRequestRepository.findByApprovalStatus(ApprovalStatus.valueOf(status)) :
                marketRequestRepository.findAll();
        List<MarketCreateResponseDao> responseList = requestList.stream()
                .map(this::migrateToResponse)
                .collect(Collectors.toList());
        return new CommonResponse(Response.SUCCESS, responseList);
    }

    private MarketCreateResponseDao migrateToResponse(MarketRequest marketRequest) {
        return MarketCreateResponseDao.builder()
                .userWalletHash(marketRequest.getUserWalletHash())
                .artifactName(marketRequest.getArtifactName())
                .artifactDescription(marketRequest.getArtifactDescription())
                .imageOneAddress(marketRequest.getImageOneAddress())
                .imageTwoAddress(marketRequest.getImageTwoAddress())
                .imageThreeAddress(marketRequest.getImageThreeAddress())
                .imageFourAddress(marketRequest.getImageFourAddress())
                .imageFiveAddress(marketRequest.getImageFiveAddress())
                .videoAddress(marketRequest.getVideoAddress())
                .approvedStatus(marketRequest.getApprovalStatus().getName())
                .requestHash(marketRequest.getRequestHash())
                .build();
    }

    @Override
    public CommonResponse createMarketRequest(MarketCreateRequestDao marketRequest) {
        if (!allFieldsHaveContent(marketRequest)) {
            return new CommonResponse(Response.NOT_FOUND);
        }
        MarketRequest newMarketRequest = convertToMarketRequest(marketRequest);
        String requestHash = generateHash(newMarketRequest);
        if (marketRequestRepository.findByRequestHash(requestHash).isPresent()) {
            return CommonResponse.builder()
                    .isOk(Boolean.FALSE)
                    .responseCode(111)
                    .responseMessage("Request already available")
                    .build();
        }
        newMarketRequest.setRequestHash(requestHash);
        marketRequestRepository.save(newMarketRequest);
        return new CommonResponse(Response.SUCCESS, migrateToResponse(newMarketRequest));
    }

    private boolean allFieldsHaveContent(MarketCreateRequestDao marketRequest) {
        return Stream.of(
                marketRequest.getUserWalletHash(),
                marketRequest.getArtifactName(),
                marketRequest.getArtifactDescription()
        ).allMatch(ValidationUtil::stringHasContent) &&
                marketRequest.getImageOne() != null && marketRequest.getImageTwo() != null &&
                marketRequest.getImageThree() != null && marketRequest.getVideo() != null;
    }

    private MarketRequest convertToMarketRequest(MarketCreateRequestDao marketRequestDao) {
        return MarketRequest.builder()
                .userWalletHash(marketRequestDao.getUserWalletHash())
                .artifactName(marketRequestDao.getArtifactName())
                .artifactDescription(marketRequestDao.getArtifactDescription())
                .imageOneAddress(fileHandleService.getFileURL(marketRequestDao.getImageOne()))
                .imageTwoAddress(fileHandleService.getFileURL(marketRequestDao.getImageTwo()))
                .imageThreeAddress(fileHandleService.getFileURL(marketRequestDao.getImageThree()))
                .imageFourAddress(marketRequestDao.getImageFour() != null ? fileHandleService.getFileURL(marketRequestDao.getImageFour()) : null)
                .imageFiveAddress(marketRequestDao.getImageFive() != null ? fileHandleService.getFileURL(marketRequestDao.getImageFive()) : null)
                .videoAddress(fileHandleService.getFileURL(marketRequestDao.getVideo()))
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
    }


    public static String generateHash(MarketRequest marketRequest) {
        String combinedString = marketRequest.getUserWalletHash() +
                marketRequest.getArtifactName() +
                marketRequest.getArtifactDescription() +
                marketRequest.getImageOneAddress() +
                marketRequest.getImageTwoAddress() +
                marketRequest.getImageThreeAddress() +
                (marketRequest.getImageFourAddress() != null ? marketRequest.getImageFourAddress() : "") +
                (marketRequest.getImageFiveAddress() != null ? marketRequest.getImageFiveAddress() : "") +
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
    public CommonResponse approveMarketRequest(MarketCreateRequestDao marketRequest) {
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
