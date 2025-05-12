package FormTools;

import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public interface Extractor {
    /**
     * Extrae los datos de entrada de un componente
     * @param campo componente a procesar
     * @return La información del campo
     */
    static Object extraerDato(JComponent campo){

        Class<? extends JComponent> clase = campo.getClass();
        switch (clase.getSimpleName().toLowerCase()){
            case "jtextfield":
                String t = ((JTextField)campo).getText();
                if(t.isEmpty()) return null;
                else return t;

            case "jcombobox":
                return ((JComboBox)campo).getSelectedItem();

            case "checkbox":
                return ((JCheckBox)campo).isSelected();

            case "datepicker":
                var k = ((JDatePicker)campo).getModel().getValue();
                System.out.println(k);
                return k;

            default: return null;
        }
    }
    /**
     * Intenta convertir un dato a una fecha SQL
     * @param dato dato a convertir
     * @return Fecha convertida, o nulo si la conversión falla
     */
    static Date convertirDate(String dato){
        try {
            return DateFormat.getInstance().parse(dato);
        } catch (ParseException e) {
            System.out.println("ERROR AL DATEAR: ");
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Convierte un dato a su respectivo tipo
     * @param dato dato en formato textual
     * @param tipo tipo de dato para convertir
     * @return dato convertido
     */
    static Object convertir(String dato, Class<?> tipo){
        if(dato == null) return null;
        switch (tipo.getSimpleName()){
            case "String": return dato;
            case "Integer": return Integer.parseInt(dato);
            case "Float": return Float.parseFloat(dato);
            case "Double": return Double.parseDouble(dato);
            case "Boolean": return Boolean.parseBoolean(dato);
            case "Date": return Extractor.convertirDate(dato);
            default:
                System.out.println("Tipo de dato desconocido: " + tipo.getSimpleName());
        }
        return null;
    }
    /**
     * Coloca un dato en un componente
     * @param comp componente a llenar
     * @param dato dato a mostrar
     */
    static void colocarDato(JComponent comp, Object dato){
        if(comp instanceof JTextField){
            ((JTextField) comp).setText(dato.toString());
        }
        else if(comp instanceof JComboBox<?>){
            ((JComboBox<?>) comp).setSelectedIndex((Integer) dato);
        }
        else if(comp instanceof JCheckBox){
            ((JCheckBox) comp).setSelected((Boolean) dato);
        }
        else if(comp instanceof JDatePicker){
            Calendar.getInstance().setTime((java.sql.Date) dato);
            Calendar c = Calendar.getInstance();
            ((JDatePicker) comp).getModel().setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
    }
    static boolean probarExpresion(String dato, String regex){
        Pattern patron = Pattern.compile(regex);
        return patron.matcher(dato).matches();
    }
}
