package com.example.springbootboilerplate.rocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RocketResponseDto {
    private List<String> passengers;
    private String arrivalEnd;

    public static RocketResponseDto rocketListResponse(Long rocketId, String nickname, String arrivalEnd) {
        return RocketResponseDto.builder()
            .rocketId(rocketId)
            .nickname(nickname)
            .arrivalEnd(arrivalEnd)
            .build();
    }

    public static RocketResponseDto of(List<String> passengers){
        return RocketResponseDto.builder()
                .passengers(passengers)
                .build();
    }
}
