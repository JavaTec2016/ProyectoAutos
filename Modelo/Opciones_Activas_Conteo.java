package Modelo;

public class Opciones_Activas_Conteo extends ModeloBD {
    String opcion;
    Integer ventas;

    public Opciones_Activas_Conteo(String opcion, Integer ventas) {
        this.opcion = opcion;
        this.ventas = ventas;
    }
    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Opciones_Activas_Conteo.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"Opción", "Ventas"};
    }
    @Override
    public String getDisplay() {
        return opcion + ": " + ventas;
    }

    public String getOpcion() {
        return opcion;
    }

    public Integer getVentas() {
        return ventas;
    }
}
