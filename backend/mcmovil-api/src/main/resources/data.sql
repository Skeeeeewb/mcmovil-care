INSERT INTO equipos (id, nombre, marca, modelo, estado) VALUES
    (1, 'Notebook personal', 'Lenovo', 'IdeaPad 3', 'En diagnóstico'),
    (2, 'Teléfono', 'Samsung', 'Galaxy A54', 'En reparación'),
    (3, 'Tablet', 'Samsung', 'Galaxy Tab A8', 'Disponible para retiro')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO atenciones (id, equipo_id, estado, diagnostico, cotizacion) VALUES
    (1001, 1, 'En diagnóstico', 'Posible falla de almacenamiento', 45000),
    (1002, 2, 'En reparación', 'Cambio de pantalla', 85000),
    (1003, 3, 'Finalizada', 'Revisión general completada', 20000),
    (1004, 1, 'En diagnóstico', 'Revisión de batería', 35000),
    (1005, 2, 'En reparación', 'Reparación de conector de carga', 25000),
    (1006, 3, 'Finalizada', 'Limpieza y mantención preventiva', 15000)
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO decisiones_cotizacion (atencion_id, decision) VALUES
    (1005, 'ACEPTADA'),
    (1006, 'RECHAZADA')
ON DUPLICATE KEY UPDATE decision = decision;
