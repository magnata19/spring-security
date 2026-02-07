package br.com.davidson.security.dto.response;

public record CreateUserResponse (
        Long id,
        String name,
        String email
) {
}
