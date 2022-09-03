package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.passenger.domain.Passenger;
import com.example.springbootboilerplate.passenger.PassengerRepository;
import com.example.springbootboilerplate.rocket.domain.Rocket;
import com.example.springbootboilerplate.rocket.dto.RocketBookRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RocketService {
    private final RocketRepository rocketRepository;
    private final PassengerRepository passengerRepository;
//    public Long bookRocket(RocketBookRequestDto rocketRequest) {
//        Member member = rocketRequest.
//        Rocket rocket = rocketRequest.toEntity();
//        rocketRepository.save(rocket);
//        Passenger passenger = rocketRequest.get
//        passengerRepository.save();
//         좌석 예약하기

//    }
//        String category = cardRequest.getCategory();
//        card.toCategoryEnum(category);
//        Card savedCard = cardRepository.save(card);
//        cards.add(savedCard);
//        return savedCard.getId();
//    }
}
