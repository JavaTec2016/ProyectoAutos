package FormTools;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class DecimalField extends JPanel {
    JTextField enteros;
    JLabel punto;
    JComboBox<String> decimales;
    public DecimalField(){
        setLayout(new GridBagLayout());
        enteros = (JTextField) FormHook.makeRestrictedTextField("[0-9]*");
        punto = new JLabel(".");
        punto.setOpaque(false);
        decimales = makeDecimalList();

        GridBagConstraints constraints = FormHook.makeConstraint(-1, -1, 0.5f, 0, GridBagConstraints.BOTH);
        add(enteros, constraints);
        constraints.weightx=0.0;
        add(punto, constraints);
        add(decimales, constraints);
    }

    /**
     * Crea el campo de partes decimales de este campo
     * @return lista de opciones
     */
    private JComboBox<String> makeDecimalList(){
        JComboBox<String> decimals = new JComboBox<>();

        for(int i = 0; i < 100; i++){
            String d = ""+i;
            if(d.length() < 2) d = "0"+d;
            decimals.addItem(d);
        }
        return decimals;
    }

    /**
     * Obtiene el decimal equivalente a los datos de este campo
     * @return dato decimal
     */
    public BigDecimal getDecimal(){
        String enterosText = enteros.getText();
        if(enterosText.isEmpty()) return null;
        String decimalText = decimales.getSelectedItem().toString();
        return new BigDecimal(enterosText+"."+decimalText);

    }
    public void setDecimal(String decimal){
        if(decimal == null){
            enteros.setText("");
            decimales.setSelectedIndex(0);
            return;
        }
        String[] partes = decimal.split("\\.");
        enteros.setText(partes[0]);
        decimales.setSelectedItem(partes[1]);
    }
}
