package Modelo;

import java.util.LinkedHashMap;

public class Auto_Modelo extends ModeloBD {
    private String nombre;

    public Auto_Modelo(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getDisplay() {
        return nombre;
    }

    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Auto_Modelo.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"Nombre del modelo"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Auto_Modelo.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"VARCHAR"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{20};
    }
    public static Integer[] obtenerUmbrales(){
        return new Integer[]{0};
    }
    public static Boolean[] obtenerNoNulos(){
        return new Boolean[]{true};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"textfield"};
    }
    public static String[][] obtenerEspeciales(){
        return new String[][]{new String[]{""}};
    }
    public static Integer[] obtenerPrimarias(){
        return new Integer[]{0};
    }
    public static Integer[] obtenerForaneas(){
        return new Integer[]{};
    }
    public static String[] obtenerExpresiones(){
        return new String[]{
                "([A-Za-z]|\\s)*",
        };
    }
    public static String[] obtenerValidadores(){
        return new String[]{
                "([A-Za-z]|\\s)*",
        };
    }
    @Override
    public LinkedHashMap<String, Object> getInfoImportante() {
        LinkedHashMap<String ,Object> o = new LinkedHashMap<>();
        o.put("nombre", nombre);
        return o;
    }
    @Override
    public String toString() {
        return "Auto_Modelo{" +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    public String getNombre() {
        return nombre;
    }
}
