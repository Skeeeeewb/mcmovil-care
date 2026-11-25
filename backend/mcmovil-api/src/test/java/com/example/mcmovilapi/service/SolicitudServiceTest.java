package com.example.mcmovilapi.service;

import com.example.mcmovilapi.domain.Solicitud;
import com.example.mcmovilapi.repository.EquipoRepository;
import com.example.mcmovilapi.repository.SolicitudRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private SolicitudRepository repository;

    @Mock
    private EquipoRepository equipoRepository;

    @InjectMocks
    private SolicitudService service;

    @Test
    void crearConDescripcionCortaLanza400() {
        when(equipoRepository.existsById(1)).thenReturn(true);
        Solicitud solicitud = new Solicitud(null, 1, "Muy corta", "Pendiente");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.crear(solicitud));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("La descripción debe tener al menos 10 caracteres", exception.getReason());
    }

    @Test
    void crearConEstadoInvalidoLanza400() {
        when(equipoRepository.existsById(1)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.crear(new Solicitud(null, 1, "Descripción válida", "Inválido")));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void crearValidaYGuarda() {
        when(equipoRepository.existsById(1)).thenReturn(true);
        when(repository.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Solicitud solicitud = new Solicitud(9, 1, "Descripción válida", "Pendiente");

        Solicitud guardada = service.crear(solicitud);

        assertEquals(null, guardada.getId());
        verify(repository).save(solicitud);
    }
}
