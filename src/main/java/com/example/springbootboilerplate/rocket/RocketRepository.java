package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.rocket.domain.Rocket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RocketRepository extends JpaRepository<Rocket, Long> {
    Optional<Rocket> findByRocketName(String name);
}
