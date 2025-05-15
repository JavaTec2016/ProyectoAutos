package Vista;

import javax.swing.*;

public class JDecimalField extends JComponent {
    public JTextField enteros;
    private JLabel punto;
    public JTextField decimales;
    public JDecimalField(){
        enteros = new JTextField();
        punto = new JLabel(".");
        decimales = new JTextField();
    }
}
