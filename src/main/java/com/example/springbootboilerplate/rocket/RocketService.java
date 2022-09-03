package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.member.MemberRepository;
import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.passenger.PassengerRepository;
import com.example.springbootboilerplate.passenger.domain.Passenger;
import com.example.springbootboilerplate.rocket.domain.Rocket;
import com.example.springbootboilerplate.rocket.dto.RocketBookRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketBookingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RocketService {
    private final RocketRepository rocketRepository;
    private final MemberRepository memberRepository;
    private final PassengerRepository passengerRepository;

    private List<Passenger> passengers;
    @Bean
    public void init(){
        passengers = passengerRepository.findAll();
    }

    public RocketBookingResponseDto bookRocket(RocketBookRequestDto rocketRequest) {
        // 닉네임 작성해서 회원 엔티티 저장
        String nickname = rocketRequest.getNickname();
        Member member = new Member(nickname);
        Member savedMember = memberRepository.save(member);

        // 로켓
        Rocket rocket = rocketRequest.toEntity();
        Rocket savedRocket = rocketRepository.save(rocket);

        // 탑승객
        String seat = rocketRequest.getSeat();
        Passenger passenger = Passenger.builder()
                .seat(seat)
                .rocketId(savedRocket.getId())
                .userId(savedMember.getId())
                .build();
        passengerRepository.save(passenger);

        List<String> pass = passengers.stream()
                .filter(p -> p.getRocketId() == savedRocket.getId()) // 동일 로켓 번호 찾기
                .map(p -> memberRepository.findById(p.getUserId()).get().getNickname()) // 그중에서도 닉네임만 뽑기
                .collect(Collectors.toList());
        return RocketBookingResponseDto.of(nickname, pass);
    }
}
