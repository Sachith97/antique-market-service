package com.sac.antiquemarketservice.model;

import com.sac.antiquemarketservice.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "MARKET_REQUEST")
public class MarketRequest implements Serializable {

    protected static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "IS_ACTIVE", columnDefinition = "BIT DEFAULT 1", nullable = false)
    private Boolean active;

    @Column(name = "USER_WALLET_HASH", length = 1000)
    private String userWalletHash;

    @Column(name = "ARTIFACT_NAME")
    private String artifactName;

    @Column(name = "ARTIFACT_DESC", length = 5000)
    private String artifactDescription;

    @Column(name = "IMAGE_ONE_ADD", length = 1000)
    private String imageOneAddress;

    @Column(name = "IMAGE_TWO_ADD", length = 1000)
    private String imageTwoAddress;

    @Column(name = "IMAGE_THREE_ADD", length = 1000)
    private String imageThreeAddress;

    @Column(name = "IMAGE_FOUR_ADD", length = 1000)
    private String imageFourAddress;

    @Column(name = "IMAGE_FIVE_ADD", length = 1000)
    private String imageFiveAddress;

    @Column(name = "VIDEO_ADD", length = 1000)
    private String videoAddress;

    @Column(name = "REQUEST_HASH", length = 1000)
    private String requestHash;

    @Column(name = "NFT_MARKET_ADD", length = 1000)
    private String nftMarketAddress;

    @Column(name = "NFT_TOKEN_ID")
    private String nftTokenId;

    @Column(name = "MARKET_METHOD")
    private String marketMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "APPROVAL_STATUS", nullable = false)
    private ApprovalStatus approvalStatus;

    @Column(name = "APPROVED_DATE")
    private LocalDateTime approvedDate;

    @Column(name = "REJECTED_DATE")
    private LocalDateTime rejectedDate;

    @ManyToOne
    @JoinColumn(name = "FK_APPROVED_USER")
    private User approvedUser;

    @ManyToOne
    @JoinColumn(name = "FK_REJECTED_USER")
    private User rejectedUser;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        MarketRequest other = (MarketRequest) object;
        return !((this.getId() == null && other.getId() != null) ||
                (this.getId() != null && !this.getId().equals(other.getId())));
    }
}
