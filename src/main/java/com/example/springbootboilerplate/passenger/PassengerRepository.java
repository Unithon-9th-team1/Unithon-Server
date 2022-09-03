package com.example.springbootboilerplate.passenger;

import com.example.springbootboilerplate.passenger.domain.Passenger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findAllByUserId(Long id);
}
