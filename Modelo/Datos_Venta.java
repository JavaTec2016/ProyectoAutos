package Modelo;

import java.math.BigDecimal;
import java.util.Date;

public class Datos_Venta extends ModeloBD {
    Integer id_venta;
    String nombre_cliente;
    String apellido_cliente;
    String nombre_vendedor;
    Integer id_auto;
    String modelo;
    String color;
    Date fecha_fabricacion;
    String pais_fabricacion;
    String estado_fabricacion;
    String ciudad_fabricacion;
    Integer numero_cilindros;
    Integer numero_puertas;
    Integer peso_kg;
    Integer capacidad;
    Boolean nuevo;
    BigDecimal precio_final;
    BigDecimal intercambio_descuento;
    String financiamiento;
    Integer kilometraje;
    Date fecha_entrega;
    String garantia_tipo;

    public Datos_Venta(Integer id_venta, String nombre_cliente, String apellido_cliente, String nombre_vendedor, Integer id_auto, String modelo, String color, Date fecha_fabricacion, String pais_fabricacion, String estado_fabricacion, String ciudad_fabricacion, Integer numero_cilindros, Integer numero_puertas, Integer peso_kg, Integer capacidad, Boolean nuevo, BigDecimal precio_final, BigDecimal intercambio_descuento, String financiamiento, Integer kilometraje, Date fecha_entrega, String garantia_tipo) {
        this.id_venta = id_venta;
        this.nombre_cliente = nombre_cliente;
        this.apellido_cliente = apellido_cliente;
        this.nombre_vendedor = nombre_vendedor;
        this.id_auto = id_auto;
        this.modelo = modelo;
        this.color = color;
        this.fecha_fabricacion = fecha_fabricacion;
        this.pais_fabricacion = pais_fabricacion;
        this.estado_fabricacion = estado_fabricacion;
        this.ciudad_fabricacion = ciudad_fabricacion;
        this.numero_cilindros = numero_cilindros;
        this.numero_puertas = numero_puertas;
        this.peso_kg = peso_kg;
        this.capacidad = capacidad;
        this.nuevo = nuevo;
        this.precio_final = precio_final;
        this.intercambio_descuento = intercambio_descuento;
        this.financiamiento = financiamiento;
        this.kilometraje = kilometraje;
        this.fecha_entrega = fecha_entrega;
        this.garantia_tipo = garantia_tipo;
    }

    @Override
    public String getDisplay() {
        return "";
    }

    public static String[] obtenerCampoNombres(){
        return ModeloBD.obtenerCampoNombresDe(Datos_Venta.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"ID", "Fecha de venta", "Nombre del Cliente", "Apellido del Cliente", "Nombre del Vendedor", "ID del auto", "Color", "Modelo", "Fecha de fabriación", "País de fabricación", "Estado de fabricación", "Ciudad de fabricación", "Número de cilindros", "Número de puertas", "Peso (Kg)", "Capacidad", "Es nuevo?", "Precio final", "Descuento por intercambio", "Financiamiento", "Kilometraje", "Fecha de entrega", "Tipo de garantía"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return ModeloBD.obtenerCampoTiposDe(Datos_Venta.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"INT", "DATE", "VARCHAR", "VARCHAR", "VARCHAR", "INT", "DECIMAL", "VARCHAR", "VARCHAR", "DATE", "VARCHAR", "VARCHAR", "VARCHAR", "INT", "INT", "INT", "INT", "BOOLEAN", "DECIMAL", "DECIMAL", "VARCHAR", "INT", "DATE", "VARCHAR"};
    }

    public String getModelo() {
        return modelo;
    }
}
