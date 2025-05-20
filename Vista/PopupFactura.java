package Vista;

import FormTools.FormHook;
import conexion.ConexionBD;

import javax.swing.*;
import java.awt.*;

public class PopupFactura extends JFrame {
    JLabel label;
    JTextField input;

    public PopupFactura(){
        setLayout(new GridBagLayout());

        label = new JLabel("Ingrse el ID de venta a facturar");
        input = FormHook.makeRestrictedTextField("[0-9]*");
        setSize(new Dimension(200, 100));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        GridBagConstraints constraints = FormHook.makeConstraint(0, -1, GridBagConstraints.BOTH);
        add(label, constraints);
        add(input, constraints);

        input.addActionListener(e -> {
            if(input.getText().isEmpty()) return;
            Integer id = Integer.parseInt(input.getText());
            new Reportador().verFacturaThread(id, ConexionBD.getConexion());
            setVisible(false);
        });

    }
}
