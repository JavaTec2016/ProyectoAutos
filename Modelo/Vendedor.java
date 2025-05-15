package Modelo;

import java.util.LinkedHashMap;

public class Vendedor extends ModeloBD {
    private Integer id;
    private String nombre;
    private String apellido;
    private String telefono;

    public Vendedor(Integer id, String nombre, String apellido, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }
    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Cliente.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"ID", "Nombre", "Apellido", "Teléfono"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Cliente.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"INT", "VARCHAR", "VARCHAR", "VARCHAR"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{-1, 32, 32, 10};
    }
    public static Integer[] obtenerUmbrales(){
        return new Integer[]{1, 0, 0, 8};
    }
    public static Boolean[] obtenerNoNulos(){
        return new Boolean[]{true, true, true, true};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"textfield", "textfield", "textfield", "textfield"};
    }
    public static String[][] obtenerEspeciales(){
        return new String[][]{new String[]{""},new String[]{""},new String[]{""},new String[]{""}};
    }
    public static Integer[] obtenerPrimarias(){
        return new Integer[]{0};
    }
    public static Integer[] obtenerForaneas(){
        return new Integer[]{};
    }
    public static String[] obtenerExpresiones() {
        return new String[]{
                "[0-9]*",
                "([A-Za-z]|\\s)*",
                "([A-Za-z]|\\s)*",
                "[0-9]{0,10}",
        };
    }
    public static String[] obtenerValidadores() {
        return new String[]{
                "[0-9]*",
                "([A-Za-z]|\\s)*",
                "([A-Za-z]|\\s)*",
                "[0-9]{0,10}",
        };
    }

    @Override
    public LinkedHashMap<String, Object> getInfoImportante() {
        LinkedHashMap<String ,Object> o = new LinkedHashMap<>();
        o.put("id", id);
        o.put("nombre", nombre);
        o.put("apellido", apellido);
        o.put("telefono", telefono);
        return o;
    }

    @Override
    public String toString() {
        return "Vendedor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
