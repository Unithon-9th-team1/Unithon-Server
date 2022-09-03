package com.example.springbootboilerplate.rocket.dto;

import com.example.springbootboilerplate.rocket.domain.Rocket;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RocketBookingRequestDto {
    private String rocketName;
    private String nickname;
    private String arrivalEnd;
    private String arrivalTime;
    private String seat;

    public Rocket toEntity(){
        return Rocket.builder()
                .rocketName(rocketName)
                .arrivalEnd(arrivalEnd)
                .arrivalTime(arrivalTime)
                .code("")
                .build();
    }
}
