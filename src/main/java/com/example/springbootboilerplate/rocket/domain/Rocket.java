package com.example.springbootboilerplate.rocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rocket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rocketName;
    private String arrivalEnd;
    private String code;
    private Integer boardingStatus;

    public void updateCode(String code){
        this.code = code;
    }

    public void updateBoardingStatus(int status){
        this.boardingStatus = status;
    }
}
