package com.youx.tasy.services;

import com.youx.tasy.controller.login.LoginRequest;
import com.youx.tasy.controller.login.LoginResponse;
import com.youx.tasy.enums.Role;
import com.youx.tasy.exceptions.BusinessException;
import com.youx.tasy.model.Funcionario;
import com.youx.tasy.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoginService {

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    JwtEncoder jwtEncoder;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncode;

    public LoginResponse getCredential(LoginRequest loginRequest){
        Optional<Funcionario> optFuncionario =  funcionarioRepository.findByCPF(loginRequest.cpf());

        if (optFuncionario.isEmpty()){
            throw new BusinessException("Usuario não encontrado", HttpStatus.NOT_FOUND);
        }

        if (!bCryptPasswordEncode.matches(loginRequest.senha(), optFuncionario.get().getSenha())){
            throw new BadCredentialsException("Senha incorreta");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = optFuncionario.get().getRoles()
                .stream()
                .map(Role::getValue)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("tasy")
                .subject(optFuncionario.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue, expiresIn);
    }
}
