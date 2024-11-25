package com.youx.tasy.controller.paciente;

import com.youx.tasy.services.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("paciente")
public class PacienteController {

    @Autowired
    PacienteService pacienteService;

    @PostMapping()
    @PreAuthorize("hasAuthority('SCOPE_MEDICO') or hasAuthority('SCOPE_ENFERMEIRO')")
    public ResponseEntity createPaciente(@RequestBody @Valid PacienteRequest pacienteRequest) {
        pacienteService.createPaciente(pacienteRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<PacienteResponse> getPaciente(@PathVariable Long id) {
        var pacienteResponse = pacienteService.getPacienteById(id);
        return new ResponseEntity<>(pacienteResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity deletePaciente(@PathVariable Long id) {
        pacienteService.deletePaciente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity editPaciente(@PathVariable Long id, @RequestBody @Valid PacienteRequest
            pacienteRequest) {
        pacienteService.updatePaciente(id, pacienteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
