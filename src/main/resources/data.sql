-- Insertar unidades de medida solo si no existen
INSERT INTO units (name, abbreviation)
SELECT 'Kilogramo', 'kg'
    WHERE NOT EXISTS (SELECT 1 FROM units WHERE name = 'Kilogramo');

INSERT INTO units (name, abbreviation)
SELECT 'Gramo', 'g'
    WHERE NOT EXISTS (SELECT 1 FROM units WHERE name = 'Gramo');

INSERT INTO units (name, abbreviation)
SELECT 'Litro', 'L'
    WHERE NOT EXISTS (SELECT 1 FROM units WHERE name = 'Litro');

INSERT INTO units (name, abbreviation)
SELECT 'Mililitro', 'ml'
    WHERE NOT EXISTS (SELECT 1 FROM units WHERE name = 'Mililitro');

INSERT INTO units (name, abbreviation)
SELECT 'Unidad', 'u'
    WHERE NOT EXISTS (SELECT 1 FROM units WHERE name = 'Unidad');

INSERT INTO units (name, abbreviation)
SELECT 'Paquete', 'paq'
    WHERE NOT EXISTS (SELECT 1 FROM units WHERE name = 'Paquete');

INSERT INTO units (name, abbreviation)
SELECT 'Caja', 'caja'
    WHERE NOT EXISTS (SELECT 1 FROM units WHERE name = 'Caja');