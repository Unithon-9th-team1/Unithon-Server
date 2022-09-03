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
    // Status Code 참고: https://developer.mozilla.org/ko/docs/Web/HTTP/Status

    OK(200, HttpStatus.OK, "요청이 성공적으로 되었습니다."),
    CREATED(201, HttpStatus.CREATED, "요청이 성공적이었으며 그 결과로 새로운 리소스가 생성되었습니다."),
    ACCEPTED(202, HttpStatus.ACCEPTED, "요청을 수신하였지만 그에 응하여 행동할 수 없습니다."),
    NO_CONTENT(204, HttpStatus.NO_CONTENT, "요청에 대해서 보내줄 수 있는 콘텐츠가 없지만, 헤더는 의미있을 수 있습니다."),

    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 문법으로 인하여 서버가 요청을 이해할 수 없습니다."),
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "비인증"),
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "클라이언트는 콘텐츠에 접근할 권리를 가지고 있지 않습니다."),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "요청받은 리소스를 찾을 수 없습니다."),
    CONFLICT(409, HttpStatus.CONFLICT, "서버와 충돌이 발생했습니다."),

    INTERNAL_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버가 처리 방법을 모르는 상황이 발생했습니다."),
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
