package Modelo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

public class Auto extends ModeloBD {
    private Integer id;
    private BigDecimal precio;
    private String modelo;
    private Date fecha_fabricacion;
    private String pais_fabricacion;
    private String estado_fabricacion;
    private String ciudad_fabricacion;
    private Integer numero_cilindros;
    private Integer numero_puertas;
    private Integer peso_kg;
    private Integer capacidad;
    private Boolean nuevo;

    public Auto(Integer id, BigDecimal precio, String modelo, Date fecha_fabricacion, String pais_fabricacion, String estado_fabricacion, String ciudad_fabricacion, Integer numero_cilindros, Integer numero_puertas, Integer peso_kg, Integer capacidad, Boolean nuevo) {
        this.id = id;
        this.precio = precio;
        this.modelo = modelo;
        this.fecha_fabricacion = fecha_fabricacion;
        this.pais_fabricacion = pais_fabricacion;
        this.estado_fabricacion = estado_fabricacion;
        this.ciudad_fabricacion = ciudad_fabricacion;
        this.numero_cilindros = numero_cilindros;
        this.numero_puertas = numero_puertas;
        this.peso_kg = peso_kg;
        this.capacidad = capacidad;
        this.nuevo = nuevo;
    }

    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Auto.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"ID", "Precio", "Modelo", "Fecha de fabriación", "País de fabricación", "Estado de fabricación", "Ciudad de fabricación", "Número de cilindros", "Número de puertas", "Peso (Kg)", "Capacidad", "Es nuevo?"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Auto.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"INT", "DECIMAL", "VARCHAR", "DATE", "VARCHAR", "VARCHAR", "VARCHAR", "SMALLINT", "SMALLINT", "SMALLINT", "SMALLINT", "BOOLEAN"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{-1, 12, 20, -1, 32, 32, 32, -1, -1, -1, -1, -1};
    }
    public static Integer[] obtenerUmbrales(){
        return new Integer[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
    public static Boolean[] obtenerNoNulos(){
        return new Boolean[]{true, true, true, true, true, true, false, true, true, true, true, true};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"textfield", "decimalfield", "textfield", "datefield", "textfield", "textfield", "textfield", "textfield", "textfield", "textfield", "textfield", "checkbox"};
    }
    public static String[][] obtenerEspeciales(){
        return new String[][]{new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""},new String[]{""}};
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
                "([0-9]|.)*",
                "([A-Za-z]|\\s)*",
                "",
                "([A-Za-z]|\\s)*",
                "([A-Za-z]|\\s)*",
                "([A-Za-z]|\\s)*",
                "[0-9]*",
                "[0-9]*",
                "[0-9]*",
                "[0-9]*",
                "",
        };
    }
    public static String[] obtenerValidadores(){
        return new String[]{
                "[0-9]*",
                "[0-9]{1,10}.[0-9]{2}",
                "([A-Za-z]|\\s)*",
                "",
                "([A-Za-z]|\\s)*",
                "([A-Za-z]|\\s)*",
                "([A-Za-z]|\\s)*",
                "[0-9]*",
                "[0-9]*",
                "[0-9]*",
                "[0-9]*",
                "",
        };
    }

    @Override
    public LinkedHashMap<String, Object> getInfoImportante() {
        LinkedHashMap<String ,Object> o = new LinkedHashMap<>();
        o.put("id", id);
        o.put("modelo", modelo);
        o.put("fecha_fabricacion", fecha_fabricacion);
        o.put("pais_fabricacion", pais_fabricacion);
        o.put("numero_cilindros", numero_cilindros);
        return o;
    }

    @Override
    public String toString() {
        return "Auto{" +
                "id=" + id +
                ", precio=" + precio +
                ", modelo=" + modelo +
                ", fecha_fabricacion=" + fecha_fabricacion +
                ", pais_fabricacion='" + pais_fabricacion + '\'' +
                ", estado_fabricacion='" + estado_fabricacion + '\'' +
                ", ciudad_fabricacion='" + ciudad_fabricacion + '\'' +
                ", numero_cilindros=" + numero_cilindros +
                ", numero_puertas=" + numero_puertas +
                ", peso_kg=" + peso_kg +
                ", capacidad=" + capacidad +
                ", nuevo=" + nuevo +
                '}';
    }
}
