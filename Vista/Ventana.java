package Vista;

import FormTools.CampoHook;
import FormTools.FormHook;
import FormTools.ScrollHook;
import FormTools.PanelHook;
import Modelo.Cliente;
import Modelo.ModeloBD;
import Modelo.Usuario;
import conexion.ConexionBD;
import controlador.DAO;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Ventana extends JFrame {
    PanelHook ventanaPrincipal;
    PanelHook ventanaLogin;
    CardLayout layout = null;
    public Ventana(){
        setSize(500, 500);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        layout = new CardLayout();
        getContentPane().setLayout(layout);

        ventanaPrincipal = new PanelHook(null);


        getContentPane().setBackground(new Color(200,200,200));
        ventanaPrincipal.getComponente().setBackground(new Color(200,200,200));


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

        ventanaPrincipal.appendChild("scroll", s);
        add(ventanaPrincipal.componente, "principal");

    }
    public static ArrayList<ModeloBD> realizarConsulta(String t, String[] sn, String[] fn, Object[] fv) {

        try {
            return DAO.d.obtenerRegistros(t, sn, fn, fv);
        } catch (SQLException e) {
            System.out.println("Error de consulta: " + e.getErrorCode());
            e.printStackTrace();
        }
        return null;
    }
    public static void agregar(ModeloBD modelo){
        try {
            DAO.d.agregar(modelo);
        } catch (IllegalAccessException e) {
            System.out.println("Error al recuperar los datos del modelo");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("Error de instruccion: " + e.getErrorCode());
        }
    }
    public static void modificar(ModeloBD modelo, Object[] primariasOG){
        try {
            DAO.d.modificar(modelo, primariasOG);
        } catch (SQLException e) {
            System.out.println("Error de modificacion: " +e.getErrorCode());
            e.printStackTrace();
        }
    }

    public void cambiarALogin(){
        if(ventanaLogin == null){
            ventanaLogin = FormHook.crearLogin(getSize());
            add(ventanaLogin.componente, "login");
        }
        layout.show(getContentPane(), "login");
        revalidate();
    }
    public static void main(String[] args) {
        ModeloBD.registrarModelo(Cliente.class);
        String reg = "([A-Za-z]|\\s)*";
        Pattern p = Pattern.compile(reg);
        System.out.println(p.matcher("buenas tardes ..").matches());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Ventana v = new Ventana();
                v.setVisible(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConexionBD.getConector().abrirConexion("SANTIAGO", "santiago", "Autos");
                        try {

                            ConexionBD.getConector().ejecutarScriptInicial();
                            agregar(new Cliente(0, "Jua", "Jui", "4941002030", "Jerekistan", "Chida", "88Bis", "Jua@mail.si"));
                            modificar(new Cliente(2, "Ernest", "Jui", "4941002031", "Jerekistan", "Chida", "88Bis", "Ernest@mail.si"),
                                    new Object[]{0});
                            System.out.println(realizarConsulta("Cliente", null, null, null));

                            v.cambiarALogin();

                        } catch (IOException e) {
                            System.out.println("Error al abrir el script: ");
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
