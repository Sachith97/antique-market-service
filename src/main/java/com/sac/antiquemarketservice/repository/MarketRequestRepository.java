package com.sac.antiquemarketservice.repository;

import com.sac.antiquemarketservice.enums.ApprovalStatus;
import com.sac.antiquemarketservice.model.MarketRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
@Repository
public interface MarketRequestRepository extends JpaRepository<MarketRequest, Long> {

    Optional<MarketRequest> findByActiveAndRequestHash(boolean active, String requestHash);

    List<MarketRequest> findByActiveAndApprovalStatusNotIn(Boolean active, Collection<ApprovalStatus> approvalStatus);

    List<MarketRequest> findByActiveAndApprovalStatus(boolean active, ApprovalStatus approvalStatus);
}
