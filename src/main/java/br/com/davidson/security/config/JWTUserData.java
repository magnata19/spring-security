package br.com.davidson.security.config;

import lombok.Builder;

@Builder
public record JWTUserData (
        Long userId,
        String email
){
}
