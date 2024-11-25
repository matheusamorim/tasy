package com.youx.tasy.services;

import com.youx.tasy.controller.paciente.PacienteRequest;
import com.youx.tasy.controller.paciente.PacienteResponse;
import com.youx.tasy.exceptions.BusinessException;
import com.youx.tasy.model.Paciente;
import com.youx.tasy.repository.PacienteRepository;
import com.youx.tasy.utils.Hash;
import com.youx.tasy.utils.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    PacienteRepository pacienteRepository;

    public PacienteResponse getPacienteById(Long id) {
        Optional<Paciente> opt = pacienteRepository.findById(id);
        if(opt.isEmpty()){
            throw new BusinessException("usuário não encontrado", HttpStatus.NOT_FOUND);
        }

        var paciente = opt.get();
        return new PacienteResponse(paciente.getNome(), paciente.getDtNascimento(), paciente.getPeso(),
                paciente.getAltura(), paciente.getUF());
    }

    public void createPaciente(PacienteRequest request) {
        boolean cpfIsvalid = Validators.validarCPF(request.cpf());
        if(!cpfIsvalid){
            throw new BusinessException("O CPF dever ser válido", HttpStatus.BAD_REQUEST);
        }

        String md5Cpf = Hash.criptografarMD5(request.cpf());
        checkIfCpfIsExist(md5Cpf);

        var paciente = new Paciente();

        paciente.setNome(request.nome());
        paciente.setCPF(md5Cpf);
        paciente.setDtNascimento(request.dtNascimento());
        paciente.setAltura(request.altura());
        paciente.setPeso(request.peso());
        paciente.setUF(request.uf());

        pacienteRepository.save(paciente);
    }

    public void updatePaciente(Long id, PacienteRequest request) {
        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        if (optPaciente.isEmpty()) {
            throw new BusinessException("Usuario não encontrado", HttpStatus.NOT_FOUND);
        }

        String md5Cpf = Hash.criptografarMD5(request.cpf());
        checkIfCpfIsExist(md5Cpf);

        Paciente paciente = optPaciente.get();
        paciente.setNome(request.nome());
        paciente.setCPF(request.cpf());
        paciente.setDtNascimento(request.dtNascimento());
        paciente.setAltura(request.altura());
        paciente.setPeso(request.peso());
        paciente.setUF(request.uf());
        pacienteRepository.save(optPaciente.get());
    }

    public void deletePaciente(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new BusinessException("Usuario não encontrado", HttpStatus.NOT_FOUND);
        }
        pacienteRepository.deleteById(id);
    }

    private void checkIfCpfIsExist(String cpf){
        Optional<Paciente> optPaciente = pacienteRepository.findByCPF(cpf);
        var cpfIsExist = optPaciente.isEmpty();

        if(!cpfIsExist){
            throw new BusinessException("O CPF já é cadastrado", HttpStatus.CONFLICT);
        }
    }
}
