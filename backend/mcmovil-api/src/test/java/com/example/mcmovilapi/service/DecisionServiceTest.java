package com.example.mcmovilapi.service;

import com.example.mcmovilapi.domain.DecisionCotizacionEntity;
import com.example.mcmovilapi.repository.AtencionRepository;
import com.example.mcmovilapi.repository.DecisionCotizacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecisionServiceTest {

    @Mock
    private AtencionRepository atencionRepository;

    @Mock
    private DecisionCotizacionRepository repository;

    @InjectMocks
    private DecisionService service;

    @Test
    void obtenerSinDecisionDevuelvePendiente() {
        when(atencionRepository.existsById(1001)).thenReturn(true);
        when(repository.findById(1001)).thenReturn(Optional.empty());

        DecisionCotizacionEntity decision = service.obtener(1001);

        assertEquals("PENDIENTE", decision.getDecision());
    }

    @Test
    void actualizarConDecisionInvalidaLanza400() {
        when(atencionRepository.existsById(1001)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.actualizar(1001, "INVALIDA"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void actualizarGuardaDecision() {
        when(atencionRepository.existsById(1001)).thenReturn(true);
        when(repository.save(any(DecisionCotizacionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DecisionCotizacionEntity decision = service.actualizar(1001, "ACEPTADA");

        assertEquals("ACEPTADA", decision.getDecision());
        verify(repository).save(decision);
    }

    @Test
    void obtenerAtencionInexistenteLanza404() {
        when(atencionRepository.existsById(99)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.obtener(99));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
