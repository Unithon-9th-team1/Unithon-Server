package com.example.springbootboilerplate.rocket.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RocketResponseDto {
    private Long rocketId;
    private String nickname;
    private List<String> passengers;

    public static RocketResponseDto of(String nickname, List<String> passengers){
        return RocketResponseDto.builder()
                .nickname(nickname)
                .passengers(passengers)
                .build();
    }
}
