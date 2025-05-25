package Modelo;

public class Cliente_Referencias_Conteo extends ModeloBD {
    String fuente_referencia;
    Integer cantidad;

    public Cliente_Referencias_Conteo(String fuente_referencia, Integer cantidad) {
        this.fuente_referencia = fuente_referencia;
        this.cantidad = cantidad;
    }
    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Cliente_Referencias_Conteo.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"Referencia", "Cantidad"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Cliente_Referencias_Conteo.class);
    }
    @Override
    public String getDisplay() {
        return fuente_referencia +": " +cantidad;
    }

    public String getFuente_referencia() {
        return fuente_referencia;
    }

    public Integer getCantidad() {
        return cantidad;
    }
}
