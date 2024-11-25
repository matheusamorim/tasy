package com.youx.tasy.services;

import com.youx.tasy.controller.funcionario.FuncionarioRequest;
import com.youx.tasy.enums.Role;
import com.youx.tasy.exceptions.BusinessException;
import com.youx.tasy.model.Funcionario;
import com.youx.tasy.repository.FuncionarioRepository;
import com.youx.tasy.utils.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FuncionarioService {
    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void createFuncionario(FuncionarioRequest request) {
        var cpfIsValid = Validators.validarCPF(request.cpf());
        if(!cpfIsValid){
            throw new BusinessException("CPF teve ser válido", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = funcionarioRepository.existsByCPF(request.cpf());

        if(isExist) {
            throw new BusinessException("CPF já é cadastrado", HttpStatus.CONFLICT);
        }

        var funcionario = new Funcionario(request.nome(),
                                request.cpf(),
                                passwordEncoder.encode(request.senha()),
                                Set.of(Role.valueOf(request.role())));

        funcionarioRepository.save(funcionario);
    }
}
