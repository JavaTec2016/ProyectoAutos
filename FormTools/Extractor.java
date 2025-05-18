package FormTools;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;
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

            case "decimalfield":
                return ((DecimalField)campo).getDecimal();

            case "jcombobox":
                return ((JComboBox)campo).getSelectedItem();

            case "listhook":
                return ((ListHook)campo).getSelectedKey();

            case "jcheckbox":
                return ((JCheckBox)campo).isSelected();

            case "jdatepicker":
                DateModel k = ((JDatePicker)campo).getModel();
                return formatearFecha(k);

            default: return null;
        }
    }
    public static String formatearFecha(DateModel model){
        String mes = "0"+(model.getMonth()+1);
        mes = mes.substring(mes.length()-2);
        String dia = "0"+(model.getDay());
        dia = dia.substring(dia.length()-2);
        return model.getYear()+"-"+mes+"-"+dia;
    }
    /**
     * Intenta convertir un dato a una fecha SQL
     * @param dato dato a convertir
     * @return Fecha convertida, o nulo si la conversión falla
     */
    static Date convertirDate(String dato){
        return java.sql.Date.valueOf(dato);
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
            case "BigDecimal": return new BigDecimal(dato);
            case "Short": return Short.parseShort(dato);
            default:
                System.out.println("EXTRACTOR Tipo de dato desconocido: " + tipo.getSimpleName());
        }
        return null;
    }
    /**
     * Coloca un dato en un componente
     * @param comp componente a llenar
     * @param dato dato a mostrar
     */
    static void colocarDato(JComponent comp, Object dato){
        if(dato == null) return;
        if(comp instanceof JTextField){
            ((JTextField) comp).setText(dato.toString());
        }
        else if (comp instanceof ListHook<?,?>){
            ((ListHook<?, ?>) comp).setSelectedKey(dato);
        }
        else if(comp instanceof JComboBox<?>){
            ((JComboBox<?>) comp).setSelectedItem(dato);
        }
        else if(comp instanceof JCheckBox){
            ((JCheckBox) comp).setSelected((Boolean) dato);
        }
        else if(comp instanceof JDatePicker){
            java.sql.Date fecha = java.sql.Date.valueOf(dato.toString());
            Calendar c = Calendar.getInstance();
            c.setTime(fecha);

            setJDatePickerValue(c, (JDatePicker) comp);
        }
        else if(comp instanceof DecimalField){
            if(dato.toString().contains(".")) ((DecimalField) comp).setDecimal(dato.toString());
            else{
                System.out.println("EXTRACTOR Decimal invalido: " + dato.toString());
            }
        }else {
            System.out.println("EXTRACTOR COLOCAR DESCONOCIDO: " + comp);
        }
    }
    static void setJDatePickerValue(Calendar calendar, JDatePicker picker){
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        picker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.getFormattedTextField().setValue(calendar);
    }
    static boolean probarExpresion(String dato, String regex){
        Pattern patron = Pattern.compile(regex);
        return patron.matcher(dato).matches();
    }
}
