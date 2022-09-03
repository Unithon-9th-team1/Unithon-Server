package com.example.springbootboilerplate.memo;

import com.example.springbootboilerplate.memo.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {

}
