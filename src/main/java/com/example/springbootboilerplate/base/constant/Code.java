package com.example.springbootboilerplate.base.constant;

import com.example.springbootboilerplate.base.GeneralException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Code {
    /**
     1000번대: 요청 성공
     2000번대: Request 오류
     3000번대: Response 오류
     4000번대: DB, Server 오류
     5000번대: 인증(Security) 오류
     **/

    /**
     000번대: 공통 오류
     100번대: 유저 관련 오류
     200번대: 로켓 관련 오류
     300번대: 메모 관련 오류
     **/

    OK(1000, HttpStatus.OK, "Ok"),

    BAD_REQUEST(2000, HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR(2001, HttpStatus.BAD_REQUEST, "Validation error"),

    USER_ROLE_NOT_USER(2101, HttpStatus.BAD_REQUEST, "Not a user with a user role"),

    INTERNAL_ERROR(4000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(4001, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),
    S3_UPLOAD_ERROR(4002, HttpStatus.INTERNAL_SERVER_ERROR, "S3 upload error"),
    USER_NOT_FOUND(4100, HttpStatus.BAD_REQUEST, "Cannot find user from DB"),

    UNAUTHORIZED(5000, HttpStatus.UNAUTHORIZED, "User unauthorized"),
    NOT_REGISTERED(5001, HttpStatus.OK, "Need registration"),
    ALREADY_REGISTERED(5002, HttpStatus.BAD_REQUEST, "You're already registered"),
    INVALID_REFRESH_TOKEN(5003, HttpStatus.UNAUTHORIZED, "Invalid refresh token."),
    REFRESH_TOKEN_NOT_FOUND(5004, HttpStatus.UNAUTHORIZED, "Refresh token not found."),
    MALFORMED_JWT(5005, HttpStatus.UNAUTHORIZED, "Malformed jwt format"),
    EXPIRED_JWT(5006, HttpStatus.UNAUTHORIZED, "Jwt expired. Reissue it"),
    UNSUPPORTED_JWT(5007, HttpStatus.UNAUTHORIZED, "Unsupported jwt format"),
    ILLEGAL_JWT(5008, HttpStatus.UNAUTHORIZED, "Illegal jwt format"),
    FORBIDDEN(5009, HttpStatus.FORBIDDEN, "Forbidden"),

    USER_LOGOUT(5010, HttpStatus.UNAUTHORIZED, "User logged out"),
    JWT_INFO_DO_NOT_MATCH(5011, HttpStatus.UNAUTHORIZED, "User info do not match"),
    JWT_WITHOUT_AUTHORITY(5012, HttpStatus.UNAUTHORIZED, "Token do not have Authority"),
    INVALID_JWT_SIGNATURE(5013, HttpStatus.UNAUTHORIZED, "Invalid JWT signature")
    ;



    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
            .filter(Predicate.not(String::isBlank))
            .orElse(this.getMessage());
    }

    public static Code valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("HttpStatus is null.");
        }

        return Arrays.stream(values())
            .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
            .findFirst()
            .orElseGet(() -> {
                if (httpStatus.is4xxClientError()) {
                    return Code.BAD_REQUEST;
                } else if (httpStatus.is5xxServerError()) {
                    return Code.INTERNAL_ERROR;
                } else {
                    return Code.OK;
                }
            });
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}
