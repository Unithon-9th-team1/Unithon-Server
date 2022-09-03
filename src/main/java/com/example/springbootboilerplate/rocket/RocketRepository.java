package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.rocket.domain.Rocket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RocketRepository extends JpaRepository<Rocket, Long> {
}
