package com.example.springbootboilerplate.passenger.dto;

import com.example.springbootboilerplate.passenger.domain.Passenger;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PassengerDto {
    private String seat;

    public Passenger toEntity(){
        return Passenger.builder()
                .seat(seat)
                .build();
    }
}
