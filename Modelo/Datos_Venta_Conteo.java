package Modelo;

public class Datos_Venta_Conteo extends ModeloBD {
    String modelo;
    Integer ventas;

    public Datos_Venta_Conteo(String modelo, Integer ventas) {
        this.modelo = modelo;
        this.ventas = ventas;
    }
    public static String[] obtenerCampoNombres(){
        return ModeloBD.obtenerCampoNombresDe(Datos_Venta_Conteo.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"Modelo", "Ventas"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return ModeloBD.obtenerCampoTiposDe(Datos_Venta_Conteo.class);
    }

    @Override
    public String getDisplay() {
        return modelo + ": " + ventas;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getVentas() {
        return ventas;
    }
}
