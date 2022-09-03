package com.example.springbootboilerplate.memo.dto;

import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.memo.domain.Memo;
import com.example.springbootboilerplate.rocket.domain.Rocket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemoRequestDto {
    private String nickname;
    private Long rocketId;
    private String description;

    public Memo toMemo(String photoUrl, Member member, Rocket rocket) {
        return Memo.builder()
            .photoUrl(photoUrl)
            .description(description)
            .member(member)
            .rocket(rocket)
            .build();
    }
}
