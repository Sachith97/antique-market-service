package com.sac.antiquemarketservice.repository;

import com.sac.antiquemarketservice.model.MarketRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
@Repository
public interface MarketRequestRepository extends JpaRepository<MarketRequest, Long> {
}
