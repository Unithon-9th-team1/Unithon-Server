package com.example.springbootboilerplate.memo;

import com.example.springbootboilerplate.memo.domain.Memo;
import com.example.springbootboilerplate.rocket.domain.Rocket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByRocket(Rocket rocket);
}
