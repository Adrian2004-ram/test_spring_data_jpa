TRUNCATE TABLE empleado, departamento RESTART IDENTITY CASCADE;

INSERT INTO departamento (id, nombre, presupuesto, gasto) VALUES
                                                              (1, 'Desarrollo', 120000.00, 6000.00),
                                                              (2, 'Sistemas', 150000.00, 21000.00),
                                                              (3, 'Recursos Humanos', 280000.00, 25000.00),
                                                              (4, 'Contabilidad', 110000.00, 3000.00),
                                                              (5, 'I+D', 375000.00, 380000.00),
                                                              (6, 'Proyectos', 0.00, 0.00),
                                                              (7, 'Publicidad', 0.00, 1000.00);

INSERT INTO empleado (id, nif, nombre, apellido1, apellido2, departamento_id) VALUES
                                                                                  (1,  '32481596F', 'Aarón',   'Rivero',    'Gómez', 1),
                                                                                  (2,  'Y5575632D', 'Adela',   'Salas',     'Díaz',  2),
                                                                                  (3,  'R6970642B', 'Adolfo',  'Rubio',     'Flores',3),
                                                                                  (4,  '38382980M', 'Alberto', 'Suárez',    'Pérez', 1),
                                                                                  (5,  '98956322E', 'Carlos',  'Sánchez',   'Martín',NULL),
                                                                                  (6,  '77653982P', 'Ana',     'Gómez',     NULL,    1),
                                                                                  (7,  '80576669X', 'Pilar',   'Ruiz',      NULL,    2),
                                                                                  (8,  '12345987P', 'Marta',   'Ortega',    'Ramos', NULL),
                                                                                  (9,  '56399183D', 'Juan',    'Gómez',     'López', 2),
                                                                                  (10, '11111111H', 'Pepe',    'Ruiz',      'Santana',3),
                                                                                  (11, '22222222J', 'Irene',   'Hernández', 'Moreno',4),
                                                                                  (12, '33333333K', 'Sergio',  'Navarro',   'Torres',5),
                                                                                  (13, '44444444L', 'Lucía',   'Molina',    'Santos',5);