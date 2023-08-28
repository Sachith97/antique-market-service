package com.sac.antiquemarketservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
@Getter
@AllArgsConstructor
public enum MarketMethod {

    ANTIQUE_ARTIFACT("ANTIQUE ARTIFACT"),
    HIGH_VALUE_ARTIFACT("HIGH VALUE ARTIFACT");

    private final String name;
}
