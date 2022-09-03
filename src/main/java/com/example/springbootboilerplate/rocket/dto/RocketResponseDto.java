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

    public static RocketResponseDto of(Long rocketId, String nickname, List<String> passengers){
        return RocketResponseDto.builder()
                .rocketId(rocketId)
                .nickname(nickname)
                .passengers(passengers)
                .build();
    }
}
