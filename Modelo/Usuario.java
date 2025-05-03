package Modelo;

public class Usuario extends ModeloBD {
    String usuario;
    String password;
    Boolean lectura;
    Boolean escritura;
    Boolean admin;

    public Usuario(String usuario, String password, Boolean lectura, Boolean escritura, Boolean admin) {
        this.usuario = usuario;
        this.password = password;
        this.lectura = lectura;
        this.escritura = escritura;
        this.admin = admin;
    }
    public static String[] obtenerCamposNombres(){
        return obtenerCampoNombresDe(Usuario.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"Usuario", "Contraseña", "Lectura", "Escritura", "Administrador"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Usuario.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"VARCHAR", "VARCHAR", "BOOLEAN", "BOOLEAN", "BOOLEAN"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{50, 50, -1, -1, -1};
    }
    public static Boolean[] obtenerNoNulos(){
        return new Boolean[]{true, true, true, true, true};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"textfield", "textfield", "checkbox", "checkbox", "checkbox"};
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
                "\\.{0,50}",
                "",
                "",
                ""
        };
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuario='" + usuario + '\'' +
                ", password='" + password + '\'' +
                ", lectura=" + lectura +
                ", escritura=" + escritura +
                ", admin=" + admin +
                '}';
    }
}
