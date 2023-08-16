package com.sac.antiquemarketservice.enums;

import lombok.Getter;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@Getter
public enum Response {

    SUCCESS(Boolean.TRUE, 200, "Success"),
    UNAUTHORIZED(Boolean.FALSE, 401, "Unauthorized request"),
    FORBIDDEN(Boolean.FALSE, 403, "Forbidden request"),
    NOT_FOUND(Boolean.FALSE, 404, "Can not find request details"),
    FAILED(Boolean.FALSE, 500, "Failed with unhandled error");

    private final boolean isOk;
    private final int responseCode;
    private final String responseMessage;

    Response(boolean isOk, int responseCode, String responseMessage) {
        this.isOk = isOk;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}
