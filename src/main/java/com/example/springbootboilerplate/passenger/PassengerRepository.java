package com.example.springbootboilerplate.passenger;

import com.example.springbootboilerplate.passenger.domain.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
