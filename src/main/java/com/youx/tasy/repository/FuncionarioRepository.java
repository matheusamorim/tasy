package com.youx.tasy.repository;

import com.youx.tasy.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByCPF(String CPF);

    Boolean existsByCPF(String cpf);
}
