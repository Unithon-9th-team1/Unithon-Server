package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.member.MemberRepository;
import com.example.springbootboilerplate.member.MemberService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RocketService {
    private final RocketRepository rocketRepository;
    private final MemberRepository memberRepository;
    private final PassengerRepository passengerRepository;

    private final MemberService memberService;

    private List<Member> members;
    private List<Passenger> passengers;
    private List<Rocket> rockets;
    @Bean
    public void init(){
        members = memberRepository.findAll();
        passengers = passengerRepository.findAll();
        rockets = rocketRepository.findAll();
    }

    // 로켓 생성
    public RocketResponseDto createRocket(RocketBookingRequestDto rocketRequest) {
        // 로켓 저장
        Rocket rocket = rocketRequest.toEntity();
        Rocket savedRocket = rocketRepository.save(rocket);
        rockets.add(savedRocket);

        // 닉네임으로 멤버 저장
        String nickname = rocketRequest.getNickname();
        Member member = Member.builder()
                .nickname(nickname)
                .build();
        memberRepository.save(member);

        /***
         * 로켓만들기 완료 버튼을 누른 순간 서버한테 데이터 보내라
         * uuid 를 언제받아서 저장할것인가.?
         * **/

        // 탑승객 저장
        Passenger passenger = Passenger.builder()
                .rocketId(savedRocket.getId())
                .userId(member.getId())
                .build();
        Passenger savedPassenger = passengerRepository.save(passenger);
        passengers.add(savedPassenger);

        String code = rocketRequest.getCode();
        rocket.updateCode(code);
        rocketRepository.save(rocket);
        return RocketResponseDto.of(this.getPassengerList(savedRocket));
    }

    // 출발하기 버튼 누른 경우
    public void departRocket(Long rocketId) {
        Rocket rocket = this.findById(rocketId);
        rocket.updateBoardingStatus(2); // 탑승완료로 변경
        rocketRepository.save(rocket);
    }

    // 로켓 탑승하기 버튼 누른 경우
    public RocketResponseDto boardRocket(RocketBoardRequestDto rocketBoardRequest) {
        // 코드를 가지고 로켓을 찾아야 함
        String code = rocketBoardRequest.getCode();
        if(!this.confirmCode(code))
            throw new GeneralException(Code.CONFLICT, "없는 코드번호입니다");

        // 닉네임 갖고 승객 찾기
        String nickname = rocketBoardRequest.getNickname();
        if(!memberService.confirmNickname(nickname))
            throw new GeneralException(Code.CONFLICT, "없는 닉네임입니다");

        Member member = memberRepository.findByNickname(nickname).get();
        Rocket rocket = findByCode(code);

        // 탑승객 저장
        Passenger newPassenger = Passenger.builder()
                .rocketId(rocket.getId())
                .userId(member.getId())
                .build();
        passengerRepository.save(newPassenger);

        return RocketResponseDto.of(this.getPassengerList(rocket));
    }

    // 로켓의 승객 찾아오는 코드
    public List<String> getPassengerList(Rocket rocket){
        List<String> passengerList = new ArrayList<>();
        for(Passenger p: passengers){
            if(p.getRocketId() == rocket.getId()){
                String foundNickname = memberRepository.findById(p.getUserId()).get().getNickname();
                passengerList.add(foundNickname);
            }
        }
        return passengerList;
    }

    // 코드 검증하기
    public boolean confirmCode(String code){
        for(Rocket rocket: rockets){
            if(rocket.getCode() == code)
                return true;
        }
        return false;
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
