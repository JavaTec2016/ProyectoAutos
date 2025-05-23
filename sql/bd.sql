CONNECT RESET;
CREATE DATABASE Autos;
CONNECT TO Autos;
GRANT DBADM, SECADM ON DATABASE TO DB2ADMIN;
CREATE SCHEMA DB2ADMIN AUTHORIZATION DB2ADMIN;
CONNECT RESET;
CONNECT TO Autos user DB2ADMIN using db2admin;

CREATE TABLE Cliente(
    id INT NOT NULL PRIMARY KEY,
    nombre VARCHAR(32) NOT NULL,
    apellido VARCHAR(32) NOT NULL,
    telefono VARCHAR(10),
    ciudad VARCHAR(50),
    calle VARCHAR(50),
    num_domicilio VARCHAR(10),
    email VARCHAR(50),
    fuente_referencia VARCHAR(32),
    es_potencial BOOLEAN NOT NULL
);

CREATE INDEX index_cliente_referencia ON Cliente(fuente_referencia);
CREATE INDEX index_cliente_nombre ON Cliente(nombre);

CREATE TABLE Vendedor(
    id INT NOT NULL PRIMARY KEY,
    nombre VARCHAR(32) NOT NULL,
    apellido VARCHAR(32) NOT NULL,
    telefono VARCHAR(10) NOT NULL
);
CREATE INDEX index_vendedor_nombre ON Vendedor(nombre);

CREATE TABLE Auto_Modelo(
    nombre VARCHAR(20) NOT NULL PRIMARY KEY
);

CREATE TABLE Auto(
    id INT NOT NULL PRIMARY KEY,
    precio DECIMAL(10, 2) NOT NULL,
    color VARCHAR(32) NOT NULL,
    modelo VARCHAR(20) NOT NULL,
    fecha_fabricacion DATE NOT NULL,
    pais_fabricacion VARCHAR(32) NOT NULL,
    estado_fabricacion VARCHAR(32) NOT NULL,
    ciudad_fabricacion VARCHAR(32),
    numero_cilindros SMALLINT NOT NULL,
    numero_puertas SMALLINT NOT NULL,
    peso_kg SMALLINT NOT NULL,
    capacidad SMALLINT NOT NULL,
    nuevo BOOLEAN NOT NULL,
    FOREIGN KEY(modelo) REFERENCES Auto_Modelo(nombre)
);

CREATE INDEX index_auto_precio ON Auto(precio);
CREATE INDEX index_modelo ON Auto(modelo);
CREATE INDEX index_auto_cilindros ON Auto(numero_cilindros);

CREATE TABLE Auto_Opcion (
    id INT NOT NULL PRIMARY KEY,
    id_auto INT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    opcion VARCHAR(32) NOT NULL,
    FOREIGN KEY(id_auto) REFERENCES Auto(id)
);

CREATE INDEX index_opcion_precio ON Auto_Opcion(precio);
CREATE INDEX index_id_auto ON Auto_Opcion(id_auto);

CREATE TABLE Cliente_Adorno (
    id INT NOT NULL PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_opcion INT NOT NULL,
    FOREIGN KEY(id_cliente) REFERENCES Cliente(id),
    FOREIGN KEY(id_opcion) REFERENCES Auto_Opcion(id)
);
CREATE INDEX index_id_adorno_cliente ON Cliente_Adorno(id_cliente);
CREATE INDEX index_id_opcion ON Cliente_Adorno(id_opcion);

CREATE OR REPLACE VIEW Opciones_Activas (
    id_adorno,
    id_cliente,
    id_opcion,
    id_auto,
    opcion,
    precio
) AS
SELECT
    a.id,
    a.id_cliente,
    o.id,
    o.id_auto,
    o.opcion,
    o.precio
FROM Cliente_Adorno AS a
    INNER JOIN Auto_Opcion AS o
        ON o.id = a.id_opcion;

--#SET TERMINATOR %
CREATE OR REPLACE FUNCTION adornos_monto (
    id_auto_objetivo INT
)
RETURNS DECIMAL(10, 2)
LANGUAGE SQL
READS SQL DATA
BEGIN
	DECLARE total DECIMAL(10, 2);
	SELECT IFNULL(SUM(precio), 0) INTO total FROM Opciones_Activas WHERE id_auto = id_auto_objetivo;
	RETURN total;
END%
--#SET TERMINATOR ;

CREATE TABLE Venta (
    id INT NOT NULL PRIMARY KEY,
    fecha DATE NOT NULL,
    id_cliente INT NOT NULL,
    id_vendedor INT NOT NULL,
    comision DECIMAL(10, 2) NOT NULL,
    id_auto INT NOT NULL,
    precio_final DECIMAL(10, 2) NOT NULL,
    financiamiento VARCHAR(32) NOT NULL,
    kilometraje INT NOT NULL,
    fecha_entrega DATE,
    garantia_tipo VARCHAR(32) NOT NULL,
    FOREIGN KEY(id_cliente) REFERENCES Cliente(id),
    FOREIGN KEY(id_auto) REFERENCES Auto(id),
    FOREIGN KEY(id_vendedor) REFERENCES Vendedor(id)
);

CREATE INDEX index_id_cliente ON Venta(id_cliente);
CREATE INDEX index_id_vendedor ON Venta(id_vendedor);
CREATE INDEX index_garantia_tipo ON Venta(garantia_tipo);

CREATE TABLE Intercambio (
    id INT NOT NULL PRIMARY KEY,
    id_venta INT NOT NULL,
    marca VARCHAR(32) NOT NULL,
    modelo VARCHAR(20) NOT NULL,
    anio SMALLINT NOT NULL,
    valor_estimado DECIMAL(10,2),
    FOREIGN KEY(id_venta) REFERENCES Venta(id)

)PARTITION BY RANGE(valor_estimado) (
    STARTING FROM (0.00) ENDING AT (10000000.00) EVERY (2500000.00)
);

CREATE INDEX index_id_venta ON Intercambio(id_venta);
CREATE INDEX index_marca ON Intercambio(marca);
CREATE INDEX index_intercambio_modelo ON Intercambio(modelo);

CREATE VIEW autos_disponibles (
	id_auto,
	precio,
	modelo,
	fecha_fabricacion
) AS
SELECT
	a.id,
	a.precio,
	m.nombre,
	a.fecha_fabricacion
FROM
	Auto AS a
	INNER JOIN
		Auto_Modelo AS m
		ON a.modelo = m.nombre
	WHERE (SELECT COUNT(*) FROM Venta WHERE a.id = id_auto) = 0;

CREATE VIEW Datos_Venta (
    id_venta,
    nombre_cliente,
    apellido_cliente,
    nombre_vendedor,
    id_auto,
    modelo,
    color,
    fecha_fabricacion,
    pais_fabricacion,
    estado_fabricacion,
    ciudad_fabricacion,
    numero_cilindros,
    numero_puertas,
    peso_kg,
    capacidad,
    nuevo,
    precio_final,
    intercambio_descuento,
    financiamiento,
    kilometraje,
    fecha_entrega,
    garantia_tipo
) AS
SELECT
    venta.id,
    cliente.nombre,
    cliente.apellido,
    vendedor.nombre,
    venta.id_auto,
    auto.modelo,
    auto.color,
    auto.fecha_fabricacion,
    auto.pais_fabricacion,
    auto.estado_fabricacion,
    auto.ciudad_fabricacion,
    auto.numero_cilindros,
    auto.numero_puertas,
    auto.peso_kg,
    auto.capacidad,
    auto.nuevo,
    venta.precio_final,
    intercambio.valor_estimado,
    venta.financiamiento,
    venta.kilometraje,
    venta.fecha_entrega,
    venta.garantia_tipo
    FROM
        Venta AS venta
        INNER JOIN
            Cliente as cliente
            ON venta.id_cliente = cliente.id
        INNER JOIN
            Vendedor AS vendedor
            ON venta.id_vendedor = vendedor.id
        INNER JOIN
            Auto as auto
            ON venta.id_auto = Auto.id
        LEFT OUTER JOIN
            Intercambio AS intercambio
            ON venta.id = intercambio.id_venta;

CREATE TABLE Cargo_Licencia(
    id INT NOT NULL PRIMARY KEY,
    id_venta INT NOT NULL,
    costo DECIMAL(10,2) NOT NULL,
    impuesto DECIMAL(10, 2) NOT NULL,
    seguro_cubierto BOOLEAN NOT NULL,
    FOREIGN KEY(id_venta) REFERENCES Venta(id)
);

CREATE TABLE Auto_Eliminado(
    id INT NOT NULL PRIMARY KEY,
    precio DECIMAL(10, 2) NOT NULL,
    color VARCHAR(32) NOT NULL,
    modelo VARCHAR(20) NOT NULL,
    fecha_fabricacion DATE NOT NULL,
    pais_fabricacion VARCHAR(32) NOT NULL,
    estado_fabricacion VARCHAR(32) NOT NULL,
    ciudad_fabricacion VARCHAR(32),
    numero_cilindros SMALLINT NOT NULL,
    numero_puertas SMALLINT NOT NULL,
    peso_kg SMALLINT NOT NULL,
    capacidad SMALLINT NOT NULL,
    nuevo BOOLEAN NOT NULL,
    usuario_eliminador VARCHAR(32),
    fecha_eliminacion DATE NOT NULL
);

CREATE TABLE Auto_Opcion_Eliminado (
    id INT NOT NULL PRIMARY KEY,
    id_auto INT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    opcion VARCHAR(32) NOT NULL,
    usuario_eliminador VARCHAR(32),
    fecha_eliminacion DATE NOT NULL
);

CREATE TABLE Cliente_Eliminado(
    id INT NOT NULL PRIMARY KEY,
    nombre VARCHAR(32) NOT NULL,
    apellido VARCHAR(32) NOT NULL,
    telefono VARCHAR(10),
    ciudad VARCHAR(50),
    calle VARCHAR(50),
    num_domicilio VARCHAR(10),
    email VARCHAR(50),
    fuente_referencia VARCHAR(32),
    es_potencial BOOLEAN NOT NULL,
    usuario_eliminador VARCHAR(32),
    fecha_eliminacion DATE NOT NULL
) PARTITION BY RANGE(fecha_eliminacion)(
    STARTING '1/1/2020' ENDING '1/1/2050' EVERY 12 MONTHS
);

CREATE TABLE Venta_Eliminada (
    id INT NOT NULL PRIMARY KEY,
    fecha DATE NOT NULL,
    id_cliente INT NOT NULL,
    id_vendedor INT NOT NULL,
    comision DECIMAL(10, 2) NOT NULL,
    id_auto INT NOT NULL,
    precio_final DECIMAL(10, 2) NOT NULL,
    financiamiento VARCHAR(32) NOT NULL,
    kilometraje INT NOT NULL,
    fecha_entrega DATE,
    garantia_tipo VARCHAR(32) NOT NULL,
    usuario_eliminador VARCHAR(32),
    fecha_eliminacion DATE NOT NULL
);


--#SET TERMINATOR %
CREATE OR REPLACE TRIGGER Opciones_Adicion BEFORE INSERT ON Venta REFERENCING NEW AS NEWLER
FOR EACH ROW MODE DB2SQL
BEGIN ATOMIC
    SET NEWLER.precio_final = NEWLER.precio_final + adornos_monto(NEWLER.id_auto);
END%

CREATE OR REPLACE TRIGGER auto_opcion_delete AFTER DELETE ON Auto_Opcion
REFERENCING OLD AS OLDLER
FOR EACH ROW MODE DB2SQL
BEGIN ATOMIC
	INSERT INTO Auto_Opcion_eliminado VALUES (OLDLER.id, OLDLER.id_auto, OLDLER.precio, OLDLER.opcion, NULL, CURRENT DATE);
END%

--#SET TERMINATOR ;
