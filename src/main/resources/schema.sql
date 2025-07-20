CREATE TABLE IF NOT EXISTS cotizaciones (
    id SERIAL PRIMARY KEY,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    anio INT,
    tipo_uso VARCHAR(50),
    edad_conductor INT,
    prima_base NUMERIC,
    prima_total NUMERIC,
    fecha_cotizacion TIMESTAMP
);