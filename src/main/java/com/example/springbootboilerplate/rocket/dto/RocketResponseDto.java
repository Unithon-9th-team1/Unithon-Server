package com.example.springbootboilerplate.rocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RocketResponseDto {
    private Long rocketId;
    private String rocketName;
    private List<String> passengers;
    private LocalDateTime finalArrival;
    private String boardingStatus;

    public static RocketResponseDto rocketListResponse(Long rocketId, String rocketName, LocalDateTime finalArrival, Integer boardingStatus) {
        return RocketResponseDto.builder()
            .rocketId(rocketId)
            .rocketName(rocketName)
            .finalArrival(finalArrival)
            .boardingStatus(boardingStatus==1 ? "탑승대기" : ((boardingStatus == 2 ? "탑승완료" : "항해완료")))
            .build();
    }

    public static RocketResponseDto of(List<String> passengers){
        return RocketResponseDto.builder()
                .passengers(passengers)
                .build();
    }
}
