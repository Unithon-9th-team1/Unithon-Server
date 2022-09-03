package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.base.dto.ErrorResponseDto;
import com.example.springbootboilerplate.member.MemberRepository;
import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.passenger.PassengerRepository;
import com.example.springbootboilerplate.passenger.domain.Passenger;
import com.example.springbootboilerplate.rocket.domain.Rocket;
import com.example.springbootboilerplate.rocket.dto.RocketBoardRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketBookingRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketBookingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RocketService {
    private final RocketRepository rocketRepository;
    private final MemberRepository memberRepository;
    private final PassengerRepository passengerRepository;

    private List<Passenger> passengers;
    private List<Rocket> rockets;
    @Bean
    public void init(){
        passengers = passengerRepository.findAll();
        rockets = rocketRepository.findAll();
    }

    public RocketBookingResponseDto bookRocket(RocketBookingRequestDto rocketRequest) {
        // 닉네임 작성해서 회원 엔티티 저장
        String nickname = rocketRequest.getNickname();
        Member member = new Member(nickname);
        Member savedMember = memberRepository.save(member);

        // 로켓 저장
        Rocket rocket = rocketRequest.toEntity();
        Rocket savedRocket = rocketRepository.save(rocket);
        rockets.add(savedRocket);

        // 탑승객 저장
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

    // 로켓 탑승하기
    public RocketBoardRequestDto boardRocket(RocketBoardRequestDto rocketBoardRequest) {
        // 코드를 가지고 로켓을 찾아야 함
        String code = rocketBoardRequest.getCode();

        // 승객 찾기
        passengerRepository.findById(rocketBoardRequest.);


        // 탑승객 저장
        String seat = rocketBoardRequest.getSeat();
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

    public boolean confirmCode(String code){
        for(Rocket rocket: rockets){
            if(rocket.getCode() == code)
                return true;
        }
        return false;
    }

    public void saveRocketCode(Long rocketId, String code) {
        Rocket rocket = findById(rocketId);
        rocket.updateCode(code);
        rocketRepository.save(rocket);
    }

    public Rocket findById(Long rocketId){
        for(Rocket rocket: rockets){
            if(rocket.getId() == rocketId){
                return rocket;
            }
        }
        throw new GeneralException(Code.NOT_FOUND, "없는 로켓번호입니다");
    }
}
