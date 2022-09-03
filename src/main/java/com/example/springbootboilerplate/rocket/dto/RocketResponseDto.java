package com.example.springbootboilerplate.rocket.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RocketResponseDto {
    private List<String> passengers;

    public static RocketResponseDto of(List<String> passengers){
        return RocketResponseDto.builder()
                .passengers(passengers)
                .build();
    }
}
