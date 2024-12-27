package com.codingshuttle.SecurityApp.SecurityApplication.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private Long id;

    private String accessToken;

    private String refreshToken;
}
