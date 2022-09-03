package com.example.springbootboilerplate.rocket.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RocketBookingResponseDto {
    private Long rocketId;
    private String nickname;
    private List<String> passengers;

    public static RocketBookingResponseDto of(String nickname, List<String> passengers){
        return RocketBookingResponseDto.builder()
                .nickname(nickname)
                .passengers(passengers)
                .build();
    }
}
