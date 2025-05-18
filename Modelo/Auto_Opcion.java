package Modelo;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class Auto_Opcion extends ModeloBD {
    private Integer id;
    private Integer id_auto;
    private BigDecimal precio;
    private String opcion;

    public Auto_Opcion(Integer id, Integer id_auto, BigDecimal precio, String opcion) {
        this.id = id;
        this.id_auto = id_auto;
        this.precio = precio;
        this.opcion = opcion;
    }

    @Override
    public String getDisplay() {
        return id+", " + opcion + ", $: " + precio + ", ID del auto: " + id_auto;
    }

    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Auto_Modelo.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"ID", "ID del auto", "precio", "Nombre de la opción"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Auto_Modelo.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"INT", "INT", "DECIMAL", "VARCHAR"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{-1, -1, 12, 32};
    }
    public static Integer[] obtenerUmbrales(){
        return new Integer[]{0,0,0,0};
    }
    public static Boolean[] obtenerNoNulos(){
        return new Boolean[]{true, true, true, true};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"textfield", "listhook", "decimalfield", "textfield"};
    }
    public static String[][] obtenerEspeciales(){
        return new String[][]{new String[]{""},new String[]{""},new String[]{""},new String[]{""}};
    }
    public static Integer[] obtenerPrimarias(){
        return new Integer[]{0};
    }
    public static Integer[] obtenerForaneas(){
        return new Integer[]{1};
    }
    public static String[] obtenerExpresiones(){
        return new String[]{
                "[0-9]*",
                "[0-9]*",
                "([0-9]|.)*",
                "([A-Za-z]|\\s)*",
        };
    }
    public static String[] obtenerValidadores(){
        return new String[]{
                "[0-9]*",
                "[0-9]*",
                "[0-9]{1,10}.[0-9]{2}",
                "([A-Za-z]|\\s)*",
        };
    }

    @Override
    public LinkedHashMap<String, Object> getInfoImportante() {
        LinkedHashMap<String ,Object> o = new LinkedHashMap<>();
        o.put("id", id);
        o.put("id_auto", id_auto);
        o.put("precio", precio);
        o.put("opcion", opcion);
        return o;
    }

    @Override
    public String toString() {
        return "Auto_Opcion{" +
                "id=" + id +
                ", id_auto=" + id_auto +
                ", precio=" + precio +
                ", opcion='" + opcion + '\'' +
                '}';
    }
}
