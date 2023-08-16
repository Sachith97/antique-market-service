package com.sac.antiquemarketservice.model;

import com.sac.antiquemarketservice.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "APPROVAL_STATUS", nullable = false)
    private ApprovalStatus approvalStatus;

    @ManyToOne
    @JoinColumn(name = "FK_APPROVED_USER")
    private User approvedUser;

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
