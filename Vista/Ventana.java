package Vista;

import FormTools.CampoHook;
import FormTools.FormHook;
import FormTools.ScrollHook;
import FormTools.PanelHook;
import Modelo.Cliente;
import conexion.ConexionBD;
import controlador.DAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class Ventana extends JFrame {
    PanelHook ventanaPrincipal;
    public Ventana(){
        setSize(500, 500);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        getContentPane().setBackground(new Color(200,200,200));



        ScrollHook s = FormHook.crearScroll(400, 100, 1100, 590, 0);
        s.getView().appendChild("Registro1", FormHook.crearRegistroGridBag("Registro1",
                new JComponent[]{new JLabel("Buenas")},
                new String[]{"ID"},
                new int[]{20},
                20,
                64
        ));
        s.getView().appendChild("Registro2", FormHook.crearRegistroGridBag("Registro2",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                164
        ));
        s.getView().appendChild("Registro3", FormHook.crearRegistroGridBag("Registro3",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                64
        ));
        s.getView().appendChild("Registro4", FormHook.crearRegistroGridBag("Registro4",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                164
        ));
        s.getView().appendChild("Registro5", FormHook.crearRegistroGridBag("Registro5",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                164
        ));
        s.getView().appendChild("Registro6", FormHook.crearRegistroGridBag("Registro6",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil"),  new JLabel("1"),  new JLabel("2"),  new JLabel("3"),  new JLabel("5")},
                new String[]{"ID", "cantidad", "buenas", "GG", "A", "B"},
                new int[]{6, 6, 6, 6, 6, 6},
                36,
                64
        ));

        //s.getView().appendChild("Registro2", FormHook.crearRegistro(null));
        //s.getView().getComponente().setPreferredSize(new Dimension(s.getView().getComponente().getWidth(),s.getView().getComponente().getHeight()/3));
        add(s.getComponente());

    }

    public static void main(String[] args) {
        String reg = "([A-Za-z]|\\s)*";
        Pattern p = Pattern.compile(reg);
        System.out.println(p.matcher("buenas tardes ..").matches());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Ventana().setVisible(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConexionBD.getConector().abrirConexion("THEGR", "chinga tu madre");
                        try {
                            DAO.d.agregar(
                                    new Cliente(0, "Jua", "Jui", "4941002030", "Jerekistan", "Chida", "88Bis", "Jua@mail.si")
                            );
                        } catch (IllegalAccessException e) {
                            System.out.println("Error al recuperar los datos del modelo:");
                            e.printStackTrace();
                        } catch (SQLException e) {
                            System.out.println("Error en la consulta: " + e.getErrorCode());
                            e.printStackTrace();
                        }
                        try {
                            ConexionBD.getConector().cerrarConexion();
                        } catch (SQLException e) {
                            System.out.println("trono");
                            throw new RuntimeException(e);
                        }
                    }
                }).start();

            }
        });
    }
}
