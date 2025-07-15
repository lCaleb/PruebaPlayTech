package com.playtech.userapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponseDTO {
    private String access_token;
    private String token_type;
    private UserDTO user;
}
