
CREATE TABLE Cliente(
    id INT NOT NULL PRIMARY KEY,
    nombre VARCHAR(32) NOT NULL,
    apellido VARCHAR(32) NOT NULL,
    telefono VARCHAR(10) NOT NULL,
    ciudad VARCHAR(50),
    calle VARCHAR(50),
    num_domicilio VARCHAR(10),
    email VARCHAR(50),
    referencia INT,
    fuente_referencia INT,
    es_potencial BOOLEAN NOT NULL,

    FOREIGN KEY(referencia) REFERENCES Cliente(id)
);

CREATE TABLE Vendedor(
    id INT NOT NULL PRIMARY KEY;
    nombre VARCHAR(32) NOT NULL,
    apellido VARCHAR(32) NOT NULL,
    telefono VARCHAR(10) NOT NULL,
)
CREATE TABLE Auto_Modelo(
    id INT NOT NULL PRIMARY KEY,
    Nombre VARCHAR(20) NOT NULL,
)

CREATE TABLE Auto(
    id INT NOT NULL PRIMARY KEY,
    precio DECIMAL(10, 2) NOT NULL,
    id_modelo INT NOT NULL,
    fecha_fabricacion DATE NOT NULL,
    pais_fabricacion VARCHAR(32) NOT NULL,
    estado_fabricacion VARCHAR(32) NOT NULL,
    ciudad_fabricacion VARCHAR(32),
    numero_cilindros SMALLINT NOT NULL,
    numero_puertas SMALLINT NOT NULL,
    peso_kg SMALLINT NOT NULL,
    capacidad SMALLINT NOT NULL,
    nuevo BOOLEAN NOT NULL
)

CREATE TABLE Venta (
    id INT NOT NULL,
    fecha DATE NOT NULL,
    id_cliente INT NOT NULL,
    comision FLOAT NOT NULL,
    id_auto INT NOT NULL,
    precio_final DECIMAL(10, 2) NOT NULL,
    financiamiento VARCHAR(32) NOT NULL,
    kilometraje INT NOT NULL,
    fecha_entrega DATE,
    garantia_tipo SMALLINT NOT NULL,

    FOREIGN KEY(id_cliente) REFERENCES Cliente(id),
    FOREIGN KEY(id_auto) REFERENCES Auto(id),
    FOREIGN KEY(id_vendedor) REFERENCES Vendedor(id),
)

CREATE TABLE Intercambio (
    id INT NOT NULL PRIMARY KEY,
    id_auto INT NOT NULL,
    id_venta INT NOT NULL,
    marca VARCHAR(32) NOT NULL,
    modelo VARCHAR(32) NOT NULL,
    anio SMALLINT NOT NULL,
    valor_estimado DECIMAL(10,2),

    FOREIGN KEY(id_auto) REFERENCES Auto(id),
    FOREIGN KEY(id_venta) REFERENCES Venta(id),
)PARTITION BY RANGE(valor_estimado) (
    STARTING FROM (1.00) ENDING AT (99999999.00) EVERY (10000.00)
)

CREATE TABLE Cargo_Licencia(
    id INT NOT NULL PRIMARY KEY,
    id_venta INT NOT NULL,
    costo DECIMAL(10,2) NOT NULL,
    impuesto DECIMAL(10, 2) NOT NULL,
    seguro_cubierto BOOLEAN NOT NULL

    FOREIGN KEY(id_venta) REFERENCES Venta(id),
)

CREATE TABLE Auto_Opcion (
    id_auto INT NOT NULL,
    incremento_precio FLOAT NOT NULL,
    opcion VARCHAR(32) NOT NULL,

    FOREIGN KEY(id_auto) REFERENCES Auto(id)
)

CREATE OR REPLACE FUNCTION obtener_opciones_precio (
    id_objetivo INT
)
RETURNS DECIMAL(10, 2)
LANGUAGE SQL
READS SQL DATA
BEGIN
    DECLARE porcentaje_total FLOAT;
    SELECT SUM(incremento_precio)
    INTO porcentaje_total
    FROM Auto_Opcion
    WHERE id_auto = id_objetivo;

    RETURN porcentaje_total*(SELECT precio FROM Auto WHERE id = id_objetivo FETCH 1 ROW ONLY);
END;

CREATE VIEW Factura_Inicial (
    id_venta INT NOT NULL,
    nombre VARCHAR(32) NOT NULL,
    apellido VARCHAR(32) NOT NULL,
    telefono VARCHAR(10) NOT NULL,
    nombre_vendedor VARCHAR(32) NOT NULL,

)

CREATE TABLE Cliente_Eliminado(
    id INT NOT NULL PRIMARY KEY,
    nombre VARCHAR(32) NOT NULL,
    apellido VARCHAR(32) NOT NULL,
    telefono VARCHAR(10) NOT NULL,
    ciudad VARCHAR(50) NOT NULL,
    calle VARCHAR(50) NOT NULL,
    num_domicilio VARCHAR(10) NOT NULL,
    email VARCHAR(50) NOT NULL,

    usuario_eliminador VARCHAR(32) NOT NULL,
    fecha_eliminacion DATE NOT NULL
) PARTITION BY RANGE(fecha_eliminacion)(
    STARTING '1/1/2000' ENDING '1/1/2030' EVERY 2 MONTHS
);
