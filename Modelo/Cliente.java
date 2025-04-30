package Modelo;

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
    static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Cliente.class);
    }
    static String[] obtenerLabels(){
        return new String[]{"ID", "Nombre", "Apellido", "Teléfono", "Ciudad", "Calle", "Número de domicilio", "Email"};
    }
    static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Cliente.class);
    }
    static String[] obtenerCampoTiposSQL(){
        return new String[]{"INT", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR"};
    }
    static int[] obtenerLongitudes(){
        return new int[]{-1, 32, 32, 10, 50, 50, 10, 50};
    }
    static boolean[] obtenerNoNulos(){
        return new boolean[]{true, true, false, true, true, true, false, false};
    }
    static String[] obtenerCamposComponentes(){
        return new String[]{"textfield", "textfield", "textfield", "textfield", "textfield", "textfield", "textfield", "textfield"};
    }
    static String[] obtenerExpresiones(){
        return new String[]{
                "[0-9]*",
                "([A-Za-z]|\\s)*",
                "([A-Za-z]|\\s)*" ,
                "[0-9]{10}",
                "([A-Za-z]|\\s)*",
                "([A-Za-z0-9]|\\s|.|,)*",
                "([A-Za-z0-9]|\\s)*",
                "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$" //estandares truco
        };
    }
}