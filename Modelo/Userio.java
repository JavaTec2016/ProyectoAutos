package Modelo;

import java.util.LinkedHashMap;

public class Userio extends ModeloBD {
    String nombre;
    String password;
    Boolean lectura;
    Boolean escritura;
    Boolean admin;

    public Userio(String nombre, String password, Boolean lectura, Boolean escritura, Boolean admin) {
        this.nombre = nombre;
        this.password = password;
        this.lectura = lectura;
        this.escritura = escritura;
        this.admin = admin;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getLectura() {
        return lectura;
    }

    public Boolean getEscritura() {
        return escritura;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public static String[] obtenerCamposNombres(){
        return obtenerCampoNombresDe(Userio.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"Usuario", "Contraseña", "Lectura", "Escritura", "Administrador"};
    }

    @Override
    public String getDisplay() {
        return nombre;
    }

    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Userio.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"VARCHAR", "VARCHAR", "BOOLEAN", "BOOLEAN", "BOOLEAN"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{50, 50, -1, -1, -1};
    }
    public static Integer[] obtenerUmbrales(){
        return new Integer[]{0, 8, 0, 0, 0};
    }
    public static Boolean[] obtenerNoNulos(){
        return new Boolean[]{true, true, true, true, true};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"textfield", "passfield", "checkbox", "checkbox", "checkbox"};
    }
    public static Integer[] obtenerPrimarias(){
        return new Integer[]{0};
    }
    public static String[][] obtenerEspeciales(){
        return new String[][]{new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""}};
    }
    public static String[] obtenerExpresiones(){
        return new String[]{
                "^\\w{0,50}",
                "^.{0,50}",
                "",
                "",
                ""
        };
    }
    public static String[] obtenerValidadores(){
        return  obtenerExpresiones();
    }

    @Override
    public LinkedHashMap<String, Object> getInfoImportante() {
        LinkedHashMap<String, Object> o = new LinkedHashMap<>();
        o.put("nombre", nombre);
        o.put("password", password);
        o.put("lectura", lectura);
        o.put("escritura", escritura);
        o.put("admin", admin);
        return o;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuario='" + nombre + '\'' +
                ", password='" + password + '\'' +
                ", lectura=" + lectura +
                ", escritura=" + escritura +
                ", admin=" + admin +
                '}';
    }
}
