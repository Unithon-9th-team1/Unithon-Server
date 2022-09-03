package com.example.springbootboilerplate.memo.dto;

import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.memo.domain.Memo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemoRequestDto {
    private String nickname;

    private String description;

    public Memo toMember(String photoUrl, Member member) {
        return Memo.builder()
            .photoUrl(photoUrl)
            .description(description)
            .member(member)
            .build();
    }
}
