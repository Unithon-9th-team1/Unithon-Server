package com.example.springbootboilerplate.memo.domain;

import com.example.springbootboilerplate.config.BaseTimeEntity;
import com.example.springbootboilerplate.member.domain.Member;
import com.sun.istack.NotNull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Memo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String photoUrl;

    @NotNull
    private String description;

    @ManyToOne
    private Member member;

    @Builder
    public Memo(Long id, String photoUrl, String description, Member member) {
        this.id = id;
        this.photoUrl = photoUrl;
        this.description = description;
        this.member = member;
    }

}
