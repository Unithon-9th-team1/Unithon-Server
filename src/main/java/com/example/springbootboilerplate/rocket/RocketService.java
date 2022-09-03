package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.member.MemberRepository;
import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.passenger.PassengerRepository;
import com.example.springbootboilerplate.passenger.domain.Passenger;
import com.example.springbootboilerplate.rocket.domain.Rocket;
import com.example.springbootboilerplate.rocket.dto.RocketBoardRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketBookingRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketResponseDto;
import java.util.ArrayList;
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

    private List<Member> members;
    private List<Passenger> passengers;
    private List<Rocket> rockets;
    @Bean
    public void init(){
        members = memberRepository.findAll();
        passengers = passengerRepository.findAll();
        rockets = rocketRepository.findAll();
    }

    public RocketResponseDto bookRocket(RocketBookingRequestDto rocketRequest) {
        // 닉네임 작성해서 회원 엔티티 저장
        String nickname = rocketRequest.getNickname();
        Member member = new Member(nickname);
        Member savedMember = memberRepository.save(member);
        members.add(savedMember);

        // 로켓 저장
        Rocket rocket = rocketRequest.toEntity();
        Rocket savedRocket = rocketRepository.save(rocket);
        rockets.add(savedRocket);

        // 좌석 저장

        // 탑승객 저장
//        String seat = rocketRequest.getSeat();
//        Passenger passenger = Passenger.builder()
//                .seat(seat)
//                .rocketId(savedRocket.getId())
//                .userId(savedMember.getId())
//                .build();
//        passengerRepository.save(passenger);

        List<String> pass = passengers.stream()
                .filter(p -> p.getRocketId() == savedRocket.getId()) // 동일 로켓 번호 찾기
                .map(p -> memberRepository.findById(p.getUserId()).get().getNickname()) // 그중에서도 닉네임만 뽑기
                .collect(Collectors.toList());
        return RocketResponseDto.of(nickname, pass);
    }

    // 로켓 탑승하기
    public RocketResponseDto boardRocket(RocketBoardRequestDto rocketBoardRequest) {
        // 코드를 가지고 로켓을 찾아야 함
        String code = rocketBoardRequest.getCode();
        Rocket rocket = findByCode(code);


        // 닉네임 갖고 승객 찾기
        String nickname = rocketBoardRequest.getNickname();
        Member member = memberRepository.findByNickname(nickname).get();

        // 탑승객 저장
//        Integer seatId = rocketBoard.
//        String seat = rocketBoardRequest.getSeat();
//        Passenger passenger = Passenger.builder()
//                .seat(seat)
//                .rocketId(rocket.getId())
//                .userId(member.getId())
//                .build();
//        passengerRepository.save(passenger);

        List<String> pass = passengers.stream()
                .filter(p -> p.getRocketId() == rocket.getId()) // 동일 로켓 번호 찾기
                .map(p -> memberRepository.findById(p.getUserId()).get().getNickname()) // 그중에서도 닉네임만 뽑기
                .collect(Collectors.toList());

        return RocketResponseDto.of(nickname, pass);
    }

    public boolean confirmCode(String code){
        for(Rocket rocket: rockets){
            if(rocket.getCode() == code)
                return true;
        }
        return false;
    }

    public void saveRocketCode(Long rocketId, String code) {
        Rocket rocket = this.findById(rocketId);
        rocket.updateCode(code);
        rocketRepository.save(rocket);
    }

    // 로켓 번호로 찾기
    public Rocket findById(Long rocketId){
        for(Rocket rocket: rockets) {
            if (rocket.getId() == rocketId)
                return rocket;
        }
        throw new GeneralException(Code.NOT_FOUND, "없는 로켓번호입니다");
    }

    // 로켓 코드로 찾기
    public Rocket findByCode(String code){
        for(Rocket rocket: rockets) {
            if (rocket.getCode() == code)
                return rocket;
        }
        throw new GeneralException(Code.NOT_FOUND, "잘못된 코드번호입니다");
    }

    // 유저가 참가한 로켓 목록 조회
    public List<RocketResponseDto> getMyRockets(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new GeneralException(Code.NOT_FOUND, "유저를 DB에서 찾을 수 없습니다."));
        List<Passenger> passengers = passengerRepository.findAllByUserId(member.getId());

        List<RocketResponseDto> rocketResponseDtos = new ArrayList<>();
        for (Passenger passenger : passengers) {
            Rocket rocket = rocketRepository.findById(passenger.getRocketId())
                    .orElseThrow(() -> new GeneralException(Code.NOT_FOUND, "로켓을 DB에서 찾을 수 없습니다."));
            rocketResponseDtos.add(RocketResponseDto.rocketListResponse(
                rocket.getId(),
                rocket.getRocketName(),
                rocket.getArrivalEnd()
                )
            );
        }

        return rocketResponseDtos;
    }
}
