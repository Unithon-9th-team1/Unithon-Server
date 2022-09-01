package com.example.springbootboilerplate.jwt;

import com.example.springbootboilerplate.jwt.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByKey(String key);
}
