package com.ivory.ivory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ivory.ivory.domain.Authority;
import com.ivory.ivory.domain.IncomeType;
import com.ivory.ivory.domain.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @NotNull
    private String email;

    @NotNull
    @Size(max = 50)
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    private IncomeType incomeType;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(this.password))
                .incomeType(incomeType)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
