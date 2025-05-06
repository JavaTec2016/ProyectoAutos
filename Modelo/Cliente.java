package Modelo;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class Cliente extends ModeloBD {
    private Integer id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String ciudad;
    private String calle;
    private String num_domicilio;
    private String email;

    public Cliente(Integer id, String nombre, String apellido, String telefono, String ciudad, String calle, String num_domicilio, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.ciudad = ciudad;
        this.calle = calle;
        this.num_domicilio = num_domicilio;
        this.email = email;
    }
    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Cliente.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"ID", "Nombre", "Apellido", "Teléfono", "Ciudad", "Calle", "Número de domicilio", "Email"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Cliente.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"INT", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{-1, 32, 32, 10, 50, 50, 10, 50};
    }
    public static Integer[] obtenerUmbrales(){
        return new Integer[]{1, 0, 0, 8, 0, 0, 0, 0};
    }
    public static Boolean[] obtenerNoNulos(){
        return new Boolean[]{true, true, false, true, true, true, false, false};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"textfield", "textfield", "textfield", "textfield", "textfield", "textfield", "textfield", "textfield"};
    }
    public static String[][] obtenerEspeciales(){
        return new String[][]{new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""}};
    }
    public static Integer[] obtenerPrimarias(){
        return new Integer[]{0};
    }
    public static Integer[] obtenerForaneas(){
        return new Integer[]{};
    }
    public static String[] obtenerExpresiones(){
        return new String[]{
                "[0-9]*",
                "([A-Za-z]|\\s)*",
                "([A-Za-z]|\\s)*" ,
                "[0-9]{0,10}",
                "([A-Za-z]|\\s)*",
                "([A-Za-z0-9]|\\s|.|,)*",
                "([A-Za-z0-9]|\\s)*",
                "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~@-]*",
        };
    }
    public static String[] obtenerValidadores(){
        return new String[]{
                "[0-9]*",
                "([A-Za-z]|\\s)*",
                "([A-Za-z]|\\s)*" ,
                "[0-9]{0,10}",
                "([A-Za-z]|\\s)*",
                "([A-Za-z0-9]|\\s|.|,)*",
                "([A-Za-z0-9]|\\s)*",
                "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$" //estandares truco
        };
    }

    @Override
    public LinkedHashMap<String, Object> getInfoImportante() {
        LinkedHashMap<String ,Object> o = new LinkedHashMap<>();
        o.put("id", id);
        o.put("nombre", nombre);
        o.put("apellido", apellido);
        o.put("telefono", telefono);
        o.put("email", email);
        return o;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono='" + telefono + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", calle='" + calle + '\'' +
                ", num_domicilio='" + num_domicilio + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}