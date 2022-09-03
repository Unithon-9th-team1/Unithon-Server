package com.example.springbootboilerplate.memo.dto;

import com.example.springbootboilerplate.memo.domain.Memo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemoResponseDto {
    private String photoUrl;

    private String nickname;

    private String description;

    @Builder
    public MemoResponseDto(String photoUrl, String nickname,String description) {
        this.photoUrl = photoUrl;
        this.nickname = nickname;
        this.description = description;
    }
}
