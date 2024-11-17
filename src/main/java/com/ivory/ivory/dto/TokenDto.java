package com.ivory.ivory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenDto {

    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;

    @Builder
    public TokenDto(String grantType, String accessToken, Long accessTokenExpiresIn, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
    }
}
