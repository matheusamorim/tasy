package com.youx.tasy.controller.login;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String cpf,

        @NotBlank
        String senha) {
}
