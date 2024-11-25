package com.youx.tasy.controller.paciente;

import java.util.Date;

public record PacienteResponse(
        String nome,
        Date dtNascimento,
        float peso,
        float altura,
        String uf) {
}
