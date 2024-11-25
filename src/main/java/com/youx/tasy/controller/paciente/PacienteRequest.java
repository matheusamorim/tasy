package com.youx.tasy.controller.paciente;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record PacienteRequest(
        @NotEmpty
        String nome,

        @NotEmpty
        String cpf,

        @NotNull
        @Past
        Date dtNascimento,

        @Positive
        float peso,

        @Positive
        float altura,

        @NotEmpty
        @Size(min = 2, max = 2)
        String uf) {
}
