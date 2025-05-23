package Modelo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

public class Autos_Disponibles extends ModeloBD {

    Integer id_auto;
    BigDecimal precio;
    String modelo;
    Date fecha_fabricacion;

    public Autos_Disponibles(Integer id_auto, BigDecimal precio, String modelo, Date fecha_fabricacion) {
        this.id_auto = id_auto;
        this.precio = precio;
        this.modelo = modelo;
        this.fecha_fabricacion = fecha_fabricacion;
    }
    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Auto_Opcion.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"ID del auto", "Precio", "Modelo", "Fecha de fabricación"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Auto_Opcion.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"INT", "DECIMAL", "VARCHAR", "DATE"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{0,0,0,0};
    }
    public static Integer[] obtenerUmbrales(){
        return new Integer[]{0,0,0,0};
    }
    public static Boolean[] obtenerNoNulos(){
        return new Boolean[]{false, false, false, false};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"listhook","decimalfield","combobox","datefield"};
    }
    public static String[][] obtenerEspeciales(){
        return new String[][]{null,null,null,null};
    }
    public static Integer[] obtenerPrimarias(){
        return new Integer[]{};
    }
    public static Integer[] obtenerForaneas(){
        return new Integer[]{};
    }
    public static String[] obtenerExpresiones(){
        return new String[]{
                "",
                "",
                "",
                ""
        };
    }
    public static String[] obtenerValidadores(){
        return new String[]{
                "",
                "",
                "",
                ""
        };
    }

    @Override
    public LinkedHashMap<String, Object> getInfoImportante() {
        LinkedHashMap<String ,Object> o = new LinkedHashMap<>();
        o.put("id_auto", id_auto);
        o.put("precio", precio);
        o.put("modelo", modelo);
        o.put("fecha_fabricacion", fecha_fabricacion);
        return o;
    }

    public Integer getId_auto() {
        return id_auto;
    }

    public String getModelo() {
        return modelo;
    }

    @Override
    public String getDisplay() {
        return "";
    }

}
