package Modelo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

public class Venta extends ModeloBD {
    private Integer id;
    private Date fecha;
    private Integer id_cliente;
    private Integer id_vendedor;
    private BigDecimal comision;
    private Integer id_auto;
    private BigDecimal precio_final;
    private String financiamiento;
    private Integer kilometraje;
    private Date fecha_entrega;
    private String garantia_tipo;

    public Venta(Integer id, Date fecha, Integer id_cliente, Integer id_vendedor, BigDecimal comision, Integer id_auto, BigDecimal precio_final, String financiamiento, Integer kilometraje, Date fecha_entrega, String garantia_tipo) {
        this.id = id;
        this.fecha = fecha;
        this.id_cliente = id_cliente;
        this.id_vendedor = id_vendedor;
        this.comision = comision;
        this.id_auto = id_auto;
        this.precio_final = precio_final;
        this.financiamiento = financiamiento;
        this.kilometraje = kilometraje;
        this.fecha_entrega = fecha_entrega;
        this.garantia_tipo = garantia_tipo;
    }
    public static String[] obtenerCampoNombres(){
        return obtenerCampoNombresDe(Venta.class);
    }
    public static String[] obtenerLabels(){
        return new String[]{"ID", "Fecha de venta", "Cliente", "Vendedor", "Comisión del vendedor", "Auto", "Precio final del auto", "Tipo de financiamiento", "Kilometraje", "Fecha de entrega", "Tipo de garantía"};
    }
    public static Class<?>[] obtenerCampoTipos(){
        return obtenerCampoTiposDe(Venta.class);
    }
    public static String[] obtenerCampoTiposSQL(){
        return new String[]{"INT", "DATE", "INT", "INT", "DECIMAL", "INT", "DECIMAL", "VARCHAR", "INT", "DATE", "VARCHAR"};
    }
    public static Integer[] obtenerLongitudes(){
        return new Integer[]{-1, -1, -1, -1, -1, -1, 12, -1, 12, 32, -1, -1, 32};
    }
    public static Integer[] obtenerUmbrales(){
        return new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
    public static Boolean[] obtenerNoNulos(){
        return new Boolean[]{true, true, true, true, true, true, true, true, true, false, true};
    }
    public static String[] obtenerCamposComponentes(){
        return new String[]{"textfield", "datefield", "listhook", "listhook", "decimalfield", "listhook", "decimalfield", "textfield", "textfield", "datefield", "textfield"};
    }
    public static String[][] obtenerEspeciales(){
        return new String[][]{new String[]{""}, new String[]{""}, new String[]{""}, new String[]{""}, new String[]{""}, new String[]{""}, new String[]{""}, new String[]{""}, new String[]{""}, new String[]{""}, new String[]{""}};
    }
    public static Integer[] obtenerPrimarias(){
        return new Integer[]{0};
    }
    public static Integer[] obtenerForaneas(){
        return new Integer[]{2,3,5};
    }
    public static String[] obtenerExpresiones(){
        return new String[]{
                "[0-9]*",
                "",
                "[0-9]*",
                "[0-9]*",
                "([0-9]|.)*",
                "[0-9]*",
                "([0-9]|.)*",
                "([A-Za-z]|\\s)*",
                "[0-9]*",
                "",
                "([A-Za-z]|\\s)*",
        };
    }
    public static String[] obtenerValidadores(){
        return new String[]{
                "[0-9]*",
                "",
                "[0-9]*",
                "[0-9]*",
                "[0-9]{1,10}.[0-9]{2}",
                "[0-9]*",
                "[0-9]{1,10}.[0-9]{2}",
                "([A-Za-z]|\\s)*",
                "[0-9]*",
                "",
                "([A-Za-z]|\\s)*",
        };
    }

    @Override
    public LinkedHashMap<String, Object> getInfoImportante() {
        LinkedHashMap<String ,Object> o = new LinkedHashMap<>();
        o.put("id", id);
        o.put("id_cliente", id_cliente);
        o.put("id_vendedor", id_vendedor);
        o.put("id_auto", id_auto);
        o.put("precio_final", precio_final);
        o.put("financiamiento", financiamiento);
        return o;
    };

    public Integer getId() {
        return id;
    }
}
