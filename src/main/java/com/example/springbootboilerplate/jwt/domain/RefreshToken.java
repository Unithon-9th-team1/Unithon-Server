package com.example.springbootboilerplate.jwt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {
    @Id
    @Column(name = "rt_key")
    private String key; // Member ID가 들어감

    @Column(name = "rt_value")
    private String value; // Refresh Token String이 들어감

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }

    // TODO: 만료된 토큰 삭제 작업 필요
}
