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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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


        ScrollHook s = FormHook.crearScroll(400, 100, 900, 590, 0);
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
    public static int conectar(String usuario, String pass, String bd){
        try {
            ConexionBD.getConector().abrirConexion(usuario, pass, bd);
        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR: "+e.getErrorCode());
            return e.getErrorCode();
        } catch (NullPointerException e){
            System.out.println("NULOS: " + usuario + " , " + pass + " >> " + bd);
            return 1;
        }
        return 0;
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
    public PanelHook actualizarVentana(String id, PanelHook p){
        add(p.componente, id);
        return p;
    }
    public void cambiarALogin(){
        if(ventanaLogin == null){
            ventanaLogin = FormHook.crearLogin(getSize());
            add(ventanaLogin.componente, "login");

            ventanaLogin.form.getBoton("btnLogear").componente.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ArrayList<Object> l = ventanaLogin.form.extraer();
                    int cod = //conectar((String) l.get(0), (String) l.get(1), "Autos");
                            conectar("SANTIAGO", "santiago", "Autos");
                    if(cod!=0) return;

                    try {
                        configurarABCC("Cliente");
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    } catch (NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }

                    layout.show(getContentPane(),"principal");
                    System.out.println(getContentPane().getSize());
                    System.out.println(ventanaPrincipal.componente.getSize());

                }
            });
        }
        layout.show(getContentPane(), "login");
        revalidate();
    }
    public void configurarbtnLogout(PanelHook v){
        PanelHook logo = (PanelHook) v.getChild("sidebar").getChild("logout").getChild("btn");

        logo.componente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    ConexionBD.getConector().cerrarConexion();
                    layout.show(getContentPane(), "login");
                } catch (SQLException ex) {
                    System.out.println("Error al cerrar sesion: ");
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                logo.setBackground(Color.DARK_GRAY);
                logo.getChild("detail").setBackground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                logo.setBackground(new Color(102, 102, 102));
                logo.getChild("detail").setBackground(Color.cyan);
            }
        });
    }
    public void configurarABCC(String tabla) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ArrayList<ModeloBD> ar = realizarConsulta(tabla, null, null, null);
        ventanaPrincipal = actualizarVentana("principal", FormHook.crearABCC(tabla, ar));

    }
    public void ConfigurarABCC1(String tabla){

    }
    public static void main0(String[] args) {
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
                        try {
                            ConexionBD.getConector().abrirConexion("SANTIAGO", "santiago", "Autos");
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
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

    public static void main(String[] args) {
        ModeloBD.registrarModelo(Cliente.class);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Ventana v = new Ventana();
                v.setVisible(true);
                v.cambiarALogin();
            }
        });
    }
}
