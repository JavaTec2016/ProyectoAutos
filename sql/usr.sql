CONNECT RESET;
CREATE DATABASE Userio;
CONNECT TO Userio;
CREATE TABLE Usuario (
    nombre VARCHAR(32) NOT NULL PRIMARY KEY,
    password VARCHAR(32) NOT NULL,
    lectura BOOLEAN NOT NULL,
    escritura BOOLEAN NOT NULL,
    admin BOOLEAN NOT NULL
);

CREATE TABLE Usuario_Eliminado (
    nombre VARCHAR(32) NOT NULL PRIMARY KEY,
    password VARCHAR(32) NOT NULL,
    lectura BOOLEAN NOT NULL,
    escritura BOOLEAN NOT NULL,
    admin BOOLEAN NOT NULL,

    usuario_eliminador VARCHAR(32),
    fecha_eliminacion DATE NOT NULL
) PARTITION BY RANGE(fecha_eliminacion)(
    STARTING '1/1/2000' ENDING '1/1/2030' EVERY 2 MONTHS
);