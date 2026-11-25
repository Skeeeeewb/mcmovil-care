INSERT INTO equipos (id, nombre, marca, modelo, estado) VALUES
    (1, 'Notebook personal', 'Lenovo', 'IdeaPad 3', 'En diagnóstico'),
    (2, 'Teléfono', 'Samsung', 'Galaxy A54', 'En reparación'),
    (3, 'Tablet', 'Samsung', 'Galaxy Tab A8', 'Disponible para retiro')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO atenciones (id, equipo_id, estado, diagnostico, cotizacion) VALUES
    (1001, 1, 'En diagnóstico', 'Posible falla de almacenamiento', 45000),
    (1002, 2, 'En reparación', 'Cambio de pantalla', 85000),
    (1003, 3, 'Finalizada', 'Revisión general completada', 20000)
ON DUPLICATE KEY UPDATE id = id;
