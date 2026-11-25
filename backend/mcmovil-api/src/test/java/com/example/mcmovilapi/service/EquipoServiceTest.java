package com.example.mcmovilapi.service;

import com.example.mcmovilapi.domain.Equipo;
import com.example.mcmovilapi.repository.EquipoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipoServiceTest {

    @Mock
    private EquipoRepository repository;

    @InjectMocks
    private EquipoService service;

    @Test
    void listarDevuelveEquipos() {
        when(repository.findAll()).thenReturn(List.of(new Equipo(1, "Notebook", "Lenovo", "IdeaPad", "En diagnóstico")));

        List<Equipo> equipos = service.listar();

        assertEquals(1, equipos.size());
        assertEquals("Notebook", equipos.get(0).getNombre());
    }

    @Test
    void obtenerInexistenteLanza404() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.obtener(99));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void crearAsignaIdNuloYGuarda() {
        Equipo equipo = new Equipo(5, "Tablet", "Samsung", "Tab A8", "Disponible para retiro");
        when(repository.save(any(Equipo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Equipo guardado = service.crear(equipo);

        assertEquals(null, guardado.getId());
        verify(repository).save(equipo);
    }

    @Test
    void actualizarConservaDatosExistentes() {
        Equipo existente = new Equipo(1, "Viejo", "Marca", "Modelo", "Estado");
        Equipo nuevo = new Equipo(null, "Nuevo", "Otra", "Otro", "Otro estado");
        when(repository.findById(1)).thenReturn(Optional.of(existente));
        when(repository.save(any(Equipo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Equipo actualizado = service.actualizar(1, nuevo);

        assertEquals(1, actualizado.getId());
        assertEquals("Nuevo", actualizado.getNombre());
    }
}
