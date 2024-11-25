package com.youx.tasy.controller.funcionario;

import com.youx.tasy.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FuncionarioController {

    @Autowired
    FuncionarioService funcionarioService;

    @PostMapping("/funcionario")
    public ResponseEntity login(@RequestBody FuncionarioRequest funcionarioRequest) {
        funcionarioService.createFuncionario(funcionarioRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}