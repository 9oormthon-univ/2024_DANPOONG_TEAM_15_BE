package com.ivory.ivory.dto;

import com.ivory.ivory.domain.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenDto {

    private Long id;
    private String authority;
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;

    @Builder
    public TokenDto(Long id, String authority, String grantType, String accessToken, Long accessTokenExpiresIn, String refreshToken) {
        this.id = id;
        this.authority = authority;
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
    }
}
