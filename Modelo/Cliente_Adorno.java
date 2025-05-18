package Modelo;

import java.util.LinkedHashMap;

public class Cliente_Adorno extends ModeloBD {
    private Integer id;
    private Integer id_cliente;
    private Integer id_opcion;

    public Cliente_Adorno(Integer id, Integer id_cliente, Integer id_opcion) {
        this.id = id;
        this.id_cliente = id_cliente;
        this.id_opcion = id_opcion;
    }

    @Override
    public String getDisplay() {
        return "ID:" + id + ", ID cliente: " + id_cliente + " ID opcion: " + id_opcion;
    }

    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Cliente_Adorno.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"ID", "ID del cliente", "id de la opcion"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Cliente_Adorno.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"INT", "INT", "INT"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{-1, -1, -1};
    }
    public static Integer[] obtenerUmbrales(){
        return new Integer[]{0,0,0};
    }
    public static Boolean[] obtenerNoNulos(){
        return new Boolean[]{true, true, true};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"textfield", "listhook", "listhook"};
    }
    public static String[][] obtenerEspeciales(){
        return new String[][]{new String[]{""},new String[]{""},new String[]{""}};
    }
    public static Integer[] obtenerPrimarias(){
        return new Integer[]{0};
    }
    public static Integer[] obtenerForaneas(){
        return new Integer[]{1, 2};
    }
    public static String[] obtenerExpresiones(){
        return new String[]{
                "[0-9]*",
                "[0-9]*",
                "[0-9]*",
        };
    }
    public static String[] obtenerValidadores(){
        return new String[]{
                "[0-9]*",
                "[0-9]*",
                "[0-9]*",
        };
    }
    @Override
    public LinkedHashMap<String, Object> getInfoImportante() {
        LinkedHashMap<String ,Object> o = new LinkedHashMap<>();
        o.put("id", id);
        o.put("id_cliente", id_cliente);
        o.put("id_opcion", id_opcion);
        return o;
    }
    @Override
    public String toString() {
        return "Cliente_Adorno{" +
                "id=" + id +
                ", id_cliente=" + id_cliente +
                ", id_opcion=" + id_opcion +
                '}';
    }
}
