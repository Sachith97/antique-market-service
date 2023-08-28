package com.sac.antiquemarketservice.service.impl;

import com.sac.antiquemarketservice.dao.MarketCreateRequestDao;
import com.sac.antiquemarketservice.dao.MarketRequestDao;
import com.sac.antiquemarketservice.enums.ApprovalStatus;
import com.sac.antiquemarketservice.enums.MarketMethod;
import com.sac.antiquemarketservice.enums.Response;
import com.sac.antiquemarketservice.enums.UserRole;
import com.sac.antiquemarketservice.exception.CommonResponse;
import com.sac.antiquemarketservice.model.MarketRequest;
import com.sac.antiquemarketservice.model.User;
import com.sac.antiquemarketservice.repository.MarketRequestRepository;
import com.sac.antiquemarketservice.service.FileHandleService;
import com.sac.antiquemarketservice.service.MarketRequestService;
import com.sac.antiquemarketservice.service.UserService;
import com.sac.antiquemarketservice.util.DateUtil;
import com.sac.antiquemarketservice.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<ApprovalStatus> ignoreStatusList = new ArrayList<>();
        ignoreStatusList.add(ApprovalStatus.REJECTED);
        ignoreStatusList.add(ApprovalStatus.COMPLETED);
        List<MarketRequest> requestList = status != null ? marketRequestRepository.findByActiveAndApprovalStatus(Boolean.TRUE, ApprovalStatus.valueOf(status)) :
                marketRequestRepository.findByActiveAndApprovalStatusNotIn(Boolean.TRUE, ignoreStatusList);
        List<MarketRequestDao> responseList = requestList.stream()
                .map(this::migrateToResponse)
                .collect(Collectors.toList());
        return new CommonResponse(Response.SUCCESS, responseList);
    }

    private MarketRequestDao migrateToResponse(MarketRequest marketRequest) {
        return MarketRequestDao.builder()
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
                .approvedDate(marketRequest.getApprovedDate() != null ? DateUtil.getStringFormat(marketRequest.getApprovedDate()) : null)
                .rejectedDate(marketRequest.getRejectedDate() != null ? DateUtil.getStringFormat(marketRequest.getRejectedDate()) : null)
                .requestHash(marketRequest.getRequestHash())
                .marketMethod(marketRequest.getMarketMethod())
                .build();
    }

    @Override
    public CommonResponse createMarketRequest(MarketCreateRequestDao marketRequest) {
        if (!allFieldsHaveContent(marketRequest)) {
            return new CommonResponse(Response.NOT_FOUND);
        }
        MarketRequest newMarketRequest = convertToMarketRequest(marketRequest);
        String requestHash = generateHash(newMarketRequest);
        if (marketRequestRepository.findByActiveAndRequestHash(Boolean.TRUE, requestHash).isPresent()) {
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
                .active(Boolean.TRUE)
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
    public CommonResponse withdrawMarketRequest(MarketRequestDao marketRequest) {
        Optional<MarketRequest> dbRequest = this.marketRequestRepository.findByActiveAndRequestHash(Boolean.TRUE, marketRequest.getRequestHash());
        if (!dbRequest.isPresent()) {
            return new CommonResponse(Response.NOT_FOUND);
        }
        dbRequest.get().setActive(Boolean.FALSE);
        this.marketRequestRepository.save(dbRequest.get());
        return new CommonResponse(Response.SUCCESS);
    }

    @Override
    public CommonResponse approveMarketRequest(MarketRequestDao marketRequest, ApprovalStatus approvalStatus) {
        Optional<MarketRequest> dbRequest = this.marketRequestRepository.findByActiveAndRequestHash(Boolean.TRUE, marketRequest.getRequestHash());
        if (!dbRequest.isPresent()) {
            return new CommonResponse(Response.NOT_FOUND);
        }
        Optional<User> loggedInUser = this.userService.getLoggedInUser();
        if (!loggedInUser.isPresent() || !loggedInUser.get().getRole().equals(UserRole.APPROVER.name())) {
            return new CommonResponse(Response.FORBIDDEN);
        }
        dbRequest.get().setApprovalStatus(approvalStatus);
        if (approvalStatus.equals(ApprovalStatus.APPROVED)) {
            dbRequest.get().setApprovedDate(LocalDateTime.now());
            dbRequest.get().setApprovedUser(loggedInUser.get());

        } else if (approvalStatus.equals(ApprovalStatus.REJECTED)) {
            dbRequest.get().setRejectedDate(LocalDateTime.now());
            dbRequest.get().setRejectedUser(loggedInUser.get());
        }
        this.marketRequestRepository.save(dbRequest.get());
        return new CommonResponse(Response.SUCCESS);
    }

    @Override
    public CommonResponse saveNFTInfo(MarketRequestDao marketRequest) {
        Optional<MarketRequest> dbRequest = this.marketRequestRepository.findByActiveAndRequestHash(Boolean.TRUE, marketRequest.getRequestHash());
        if (!dbRequest.isPresent() && marketRequest.getMarketMethod().equals(MarketMethod.ANTIQUE_ARTIFACT.getName())) {
            return new CommonResponse(Response.NOT_FOUND);
        }
        if (dbRequest.isPresent() && marketRequest.getMarketMethod().equals(MarketMethod.ANTIQUE_ARTIFACT.getName())) {
            dbRequest.get().setNftMarketAddress(marketRequest.getNftMarketAddress());
            dbRequest.get().setNftTokenId(marketRequest.getNftTokenId());
            dbRequest.get().setMarketMethod(marketRequest.getMarketMethod());
            dbRequest.get().setApprovalStatus(ApprovalStatus.COMPLETED);
            this.marketRequestRepository.save(dbRequest.get());
            return new CommonResponse(Response.SUCCESS);
        }
        // create new request
        MarketRequest newMarketRequest = MarketRequest.builder()
                .active(Boolean.TRUE)
                .userWalletHash(marketRequest.getUserWalletHash())
                .artifactName(marketRequest.getArtifactName())
                .artifactDescription(marketRequest.getArtifactDescription())
                .nftMarketAddress(marketRequest.getNftMarketAddress())
                .nftTokenId(marketRequest.getNftTokenId())
                .marketMethod(marketRequest.getMarketMethod())
                .approvalStatus(ApprovalStatus.COMPLETED)
                .build();
        newMarketRequest.setRequestHash(generateHash(newMarketRequest));
        this.marketRequestRepository.save(newMarketRequest);
        return new CommonResponse(Response.SUCCESS);
    }
}
