package com.sac.antiquemarketservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
@Getter
@AllArgsConstructor
public enum ApprovalStatus {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    COMPLETED("COMPLETED");

    private final String name;
}
