package com.youx.tasy.controller.funcionario;

import com.youx.tasy.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FuncionarioRequest(
        @NotBlank
        @Size(min = 6)
        String nome,

        @NotBlank
        @Size(min = 11, max =15)
        String cpf,

        @NotBlank
        @Size(min = 6)
        String senha,

        String role) {
}