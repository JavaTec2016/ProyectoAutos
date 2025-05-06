package Modelo;

import java.util.LinkedHashMap;

public interface Registrable {
    Object[] obtenerDatos() throws IllegalAccessException;

    static String[] obtenerCampoNombres() { return null; }
    static Class<?>[] obtenerCampoTipos(){ return null; }
    static String[] obtenerCampoTiposSQL(){ return null; }
    static String[] obtenerLabels(){ return  null; }
    static String[] obtenerCamposComponentes(){ return null; }
    static Integer[] obtenerLongitudes(){ return  null; }
    static Integer[] obtenerUmbrales(){ return null; }
    static Boolean[] obtenerNoNulos(){ return  null; }
    static String[] obtenerExpresiones(){ return  null; }
    static String[] obtenerValidadores(){ return  null; }
    static String[][] obtenerEspeciales(){ return null; }
    static Integer[] obtenerPrimarias(){ return null; }
    static Integer[] obtenerForaneas(){ return null; }
    LinkedHashMap<String, Object> getInfoImportante() throws IllegalAccessException;

}
