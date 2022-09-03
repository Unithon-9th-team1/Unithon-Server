package com.example.springbootboilerplate.rocket.domain;

import com.example.springbootboilerplate.config.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rocket extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rocketName;
    private Integer arrivalEnd; // 3일 후

    private LocalDateTime finalArrival; // 도착일자
    private String code;
    private Integer boardingStatus;

    public void updateCode(String code){
        this.code = code;
    }

    public void updateArrivalEnd(Integer arrivalEnd){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, arrivalEnd);

        TimeZone tz = calendar.getTimeZone();
        ZoneId zoneId = tz.toZoneId();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(calendar.toInstant(), zoneId);
        this.finalArrival = localDateTime;
    }

    public void updateBoardingStatus(int status){
        this.boardingStatus = status;
    }
}
