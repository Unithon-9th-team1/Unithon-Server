package com.example.springbootboilerplate.memo.dto;

import com.example.springbootboilerplate.memo.domain.Memo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemoResponseDto {
    private String dDay;

    private String photoUrl;

    private String nickname;

    private String description;

    @Builder
    public MemoResponseDto(String photoUrl, String nickname,String description) {
        this.photoUrl = photoUrl;
        this.nickname = nickname;
        this.description = description;
    }

    @Builder
    public MemoResponseDto(String dDay, String  photoUrl, String nickname,String description) {
        this.dDay = dDay;
        this.photoUrl = photoUrl;
        this.nickname = nickname;
        this.description = description;
    }
}
