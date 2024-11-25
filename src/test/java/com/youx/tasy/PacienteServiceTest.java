package com.youx.tasy;

import com.youx.tasy.controller.paciente.PacienteRequest;
import com.youx.tasy.controller.paciente.PacienteResponse;
import com.youx.tasy.exceptions.BusinessException;
import com.youx.tasy.model.Paciente;
import com.youx.tasy.repository.PacienteRepository;
import com.youx.tasy.services.PacienteService;
import com.youx.tasy.utils.Hash;
import com.youx.tasy.utils.Validators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPacienteByIdSuccess() {
        Paciente paciente = new Paciente();
        paciente.setNome("João");
        paciente.setDtNascimento(Date.valueOf("1990-01-01"));
        paciente.setPeso(70.0F);
        paciente.setAltura(1.75F);
        paciente.setUF("SP");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        PacienteResponse response = pacienteService.getPacienteById(1L);

        assertNotNull(response);
        assertEquals("João", response.nome());
        assertEquals(Date.valueOf("1990-01-01"), response.dtNascimento());
        assertEquals(70.0, response.peso());
        assertEquals(1.75, response.altura());
        assertEquals("SP", response.uf());

        verify(pacienteRepository, times(1)).findById(1L);
    }

    @Test
    void getPacienteByIdNotFound() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> pacienteService.getPacienteById(1L));
        assertEquals("usuário não encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void createPacienteSuccess() {
        try (MockedStatic<Validators> validatorsMocked = Mockito.mockStatic(Validators.class)) {
            validatorsMocked.when(() -> Validators.validarCPF("757.006.461-53")).thenReturn(true);
            when(pacienteRepository.findByCPF(Hash.criptografarMD5("757.006.461-53")))
                    .thenReturn(Optional.empty());

            PacienteRequest request = new PacienteRequest("João", "757.006.461-53",
                    Date.valueOf("1990-01-01"), 70.0F, 1.75F, "SP");

            pacienteService.createPaciente(request);

            verify(pacienteRepository, times(1)).save(any(Paciente.class));
        }
    }

    @Test
    void createPacienteInvalidCpf() {
        PacienteRequest request = new PacienteRequest("João", "1234567891", Date.valueOf("1990-01-01"),
                70.0f, 1.75f, "SP");
        try (MockedStatic<Validators> mockedValidators = Mockito.mockStatic(Validators.class)) {
            mockedValidators.when(() -> Validators.validarCPF("1234567891")).thenReturn(false);

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> pacienteService.createPaciente(request));

            assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        }
    }

    @Test
    void updatePacienteSuccess() {
        PacienteRequest request = new PacienteRequest("João", "12345678901", Date.valueOf("1990-01-01"),
                70.0f, 1.75f, "SP");
        Paciente pacienteExistente = new Paciente();
        pacienteExistente.setCPF(Hash.criptografarMD5("12345678901"));

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExistente));

        pacienteService.updatePaciente(1L, request);
        verify(pacienteRepository, times(1)).save(pacienteExistente);
    }

    @Test
    void updatePacienteNotFound() {
        PacienteRequest request = new PacienteRequest("João", "12345678901", Date.valueOf("1990-01-01"),
                70.0f, 1.75f, "SP");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> pacienteService.updatePaciente(1L, request));
        assertEquals("Usuario não encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void deletePacienteSuccess() {
        when(pacienteRepository.existsById(1L)).thenReturn(true);

        pacienteService.deletePaciente(1L);

        verify(pacienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePacienteNotFound() {
        when(pacienteRepository.existsById(1L)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> pacienteService.deletePaciente(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}
