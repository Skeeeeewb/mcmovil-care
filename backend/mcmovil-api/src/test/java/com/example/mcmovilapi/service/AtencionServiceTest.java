package com.example.mcmovilapi.service;

import com.example.mcmovilapi.domain.Atencion;
import com.example.mcmovilapi.repository.AtencionRepository;
import com.example.mcmovilapi.repository.EquipoRepository;
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
class AtencionServiceTest {

    @Mock
    private AtencionRepository repository;

    @Mock
    private EquipoRepository equipoRepository;

    @InjectMocks
    private AtencionService service;

    @Test
    void crearSinEquipoLanza400() {
        when(equipoRepository.existsById(1)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.crear(new Atencion(null, 1, "En diagnóstico", "Prueba", 1000)));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void crearConCualquierEstadoDeEquipoEsValido() {
        when(equipoRepository.existsById(1)).thenReturn(true);
        when(repository.save(any(Atencion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Atencion guardada = service.crear(new Atencion(null, 1, "En diagnóstico", "Prueba", 1000));

        assertEquals("En diagnóstico", guardada.getEstado());
    }

    @Test
    void crearValidaYGuarda() {
        when(equipoRepository.existsById(1)).thenReturn(true);
        when(repository.save(any(Atencion.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Atencion atencion = new Atencion(7, 1, "En diagnóstico", "Prueba válida", 1000);

        Atencion guardada = service.crear(atencion);

        assertEquals(null, guardada.getId());
        verify(repository).save(atencion);
    }
}
