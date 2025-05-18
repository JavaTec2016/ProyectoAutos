package Modelo;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class Opciones_Activas extends ModeloBD {
    private Integer id_adorno;
    private Integer id_cliente;
    private Integer id_opcion;
    private Integer id_auto;
    private String opcion;
    private BigDecimal precio;

    public Opciones_Activas(Integer id_adorno, Integer id_cliente, Integer id_opcion, Integer id_auto, String opcion, BigDecimal precio) {
        this.id_adorno = id_adorno;
        this.id_cliente = id_cliente;
        this.id_opcion = id_opcion;
        this.id_auto = id_auto;
        this.opcion = opcion;
        this.precio = precio;
    }

    @Override
    public String getDisplay() {
        return "ID: " + id_adorno + ", ID Cliente: " + id_cliente+", Opcion: " +opcion;
    }

    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Opciones_Activas.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"ID del adorno", "ID del cliente", "ID de la opcion", "ID del auto", "Opción", "Precio"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Opciones_Activas.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"INT", "INT", "INT", "INT", "VARCHAR", "DECIMAL"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{-1, -1, -1, -1, 32, 12};
    }
    public static Integer[] obtenerUmbrales(){
        return new Integer[]{0, 0, 0, 0, 0, 1};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"textfield", "textfield", "textfield", "textfield", "textfield", "decimalfield"};
    }
    public static String[][] obtenerEspeciales(){
        return new String[][]{new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""}};
    }
    public static Integer[] obtenerPrimarias(){
        return new Integer[]{0};
    }
    public static Integer[] obtenerForaneas(){
        return new Integer[]{0, 1, 2, 3, 4, 5, 6};
    }
    public static String[] obtenerExpresiones(){
        return new String[]{
                "[0-9]*",
                "[0-9]*",
                "[0-9]*",
                "[0-9]*",
                "([A-Za-z]|\\s)*",
                "([0-9]|.)*",
        };
    }
    public static String[] obtenerValidadores(){
        return new String[]{
                "[0-9]*",
                "[0-9]*",
                "[0-9]*",
                "[0-9]*",
                "([A-Za-z]|\\s)*",
                "[0-9]{1,10}.[0-9]{2}",
        };
    }

    @Override
    public LinkedHashMap<String, Object> getInfoImportante() {
        LinkedHashMap<String ,Object> o = new LinkedHashMap<>();
        o.put("id_adorno", id_adorno);
        o.put("id_cliente", id_cliente);
        o.put("id_opcion", id_opcion);
        o.put("id_auto", id_auto);
        o.put("opcion", opcion);
        o.put("precio", precio);
        return o;
    }
}
