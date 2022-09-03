package com.example.springbootboilerplate.rocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RocketResponseDto {
    private Long rocketId;
    private String rocketName;
    private List<String> passengers;
    private String arrivalEnd;
    private String boardingStatus;

    public static RocketResponseDto rocketListResponse(Long rocketId, String rocketName, String arrivalEnd, Integer boardingStatus) {
        return RocketResponseDto.builder()
            .rocketId(rocketId)
            .rocketName(rocketName)
            .arrivalEnd(arrivalEnd)
            .boardingStatus(boardingStatus==1 ? "탑승대기" : ((boardingStatus == 2 ? "탑승완료" : "항해완료")))
            .build();
    }

    public static RocketResponseDto of(List<String> passengers){
        return RocketResponseDto.builder()
                .passengers(passengers)
                .build();
    }
}
