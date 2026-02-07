package br.com.davidson.security.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CreateUserRequest(
        @NotEmpty(message = "Nome é obrigatório.")
        String name,
        @NotEmpty(message = "Email é obrigatório.")
        String email,
        @NotEmpty(message = "Senha é obrigatório.")
        String password
){
}
