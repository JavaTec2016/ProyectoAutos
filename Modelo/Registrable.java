package Modelo;

import java.util.HashMap;

public interface Registrable {
    Object[] obtenerDatos() throws IllegalAccessException;

    static String[] obtenerCampoNombres() { return null; }
    static Class<?>[] obtenerCampoTipos(){ return null; }
    static String[] obtenerCampoTiposSQL(){ return null; }
    static String[] obtenerLabels(){ return  null; }
    static String[] obtenerCamposComponentes(){ return null; }
    static int[] obtenerLongitudes(){ return  null; }
    static boolean[] obtenerNoNulos(){ return  null; }
    static String[] obtenerExpresiones(){ return  null; }
}
