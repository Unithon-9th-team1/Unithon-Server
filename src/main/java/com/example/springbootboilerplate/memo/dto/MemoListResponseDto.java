package com.example.springbootboilerplate.memo.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@AllArgsConstructor
public class MemoListResponseDto {
    private String rocketName;

    private LocalDateTime startedAt;

    private LocalDateTime endAt;

    private List<MemoResponseDto> memos;

}
