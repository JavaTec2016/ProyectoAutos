package Vista;

import ErrorHandlin.ErrorHandler;
import ErrorHandlin.Validador;
import FormTools.FormHook;
import FormTools.ScrollHook;
import FormTools.PanelHook;
import Modelo.Cliente;
import Modelo.ModeloBD;
import Modelo.Userio;
import conexion.ConexionBD;
import controlador.DAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Ventana extends JFrame {
    PanelHook ventanaPrincipal;
    Login ventanaLogin;
    ABCC control;

    CardLayout layout = null;

    String tablaActual = null;
    ArrayList<String> selecNombres = null;
    ArrayList<String> filtroNombres = null;
    ArrayList<Object> filtroValores = null;
    ModeloBD registroActual = null;
    Registro registroSeleccionado = null;
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
    public static HashMap<Integer, Integer> obtenerErrores(ArrayList<Object> datos, String[] regex, Integer[] longitudes, Boolean[] nonulos, Integer[] umbral){
        HashMap<Integer, Integer> o = new HashMap<>();
        for (int i = 0; i < datos.size(); i++) {
            String n = null;
            if(datos.get(i) != null) n = datos.get(i).toString();
            int code = Validador.probarString(n, regex[i], longitudes[i], nonulos[i], umbral[i]);
            if(code != 0) o.put(i, code);
        }
        return o;
    }
    public void handlearError(int codigo, Object... args){
        ErrorHandler.ejecutarHandler(codigo, args);
    }
    public static void panelError(String msg, String titulo){
        JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Registra funciones para manejar distintos errores
     * todo:
     * separar la funcion en varias, segun los parametros que reciban los handlers
     * @param v Ventana en la cual se muestran mensajes de error
     */

    public static void registrarHandlers(Ventana v){

        ///VALIDACION POSTERIOR

        ///data[0] sera el nombre del campo en este error
        ///data[1] sera la longitud maxima del dato
        ///data[2] sera el umbral del dato
        ErrorHandler.registrarHandler(Validador.NULL, data -> {
            JOptionPane.showMessageDialog(v, "El campo '"+data[0]+"' no debe ser nulo", "Error de datos", JOptionPane.ERROR_MESSAGE);
        });
        ErrorHandler.registrarHandler(Validador.TOO_LONG, data -> {
            JOptionPane.showMessageDialog(v, "El campo '"+data[0]+"' no debe exceder "+data[1]+" caracteres", "Error de datos", JOptionPane.ERROR_MESSAGE);
        });
        ErrorHandler.registrarHandler(Validador.TOO_SHORT, data -> {
            JOptionPane.showMessageDialog(v, "El campo '"+data[0]+"' no debe ser menor a "+data[2]+" caracteres", "Error de datos", JOptionPane.ERROR_MESSAGE);
        });
        ErrorHandler.registrarHandler(Validador.REGEX_FAIL, data -> {
            JOptionPane.showMessageDialog(v, "El campo '"+data[0]+"' no es válido", "Error de datos", JOptionPane.ERROR_MESSAGE);
        });

        ////VALIDACION DE SQL

        ///data[0] es el nombre del modelo
        ErrorHandler.registrarHandler(ErrorHandler.OBJECT_NOT_EXISTS, data -> {
            JOptionPane.showMessageDialog(v, "La tabla '"+data[0].toString()+"' no existe", "Error de consulta", JOptionPane.ERROR_MESSAGE);
        });
        ErrorHandler.registrarHandler(ErrorHandler.BAD_SQL, data -> {
            JOptionPane.showMessageDialog(v, "la instrucción dada no es válida", "Error de consulta", JOptionPane.ERROR_MESSAGE);
        });
        ErrorHandler.registrarHandler(ErrorHandler.SQL_TOO_BIG, data -> {
            JOptionPane.showMessageDialog(v, "la instrucción excede el tamaño máximo soportado", "Error de consulta", JOptionPane.ERROR_MESSAGE);
        });
        ErrorHandler.registrarHandler(ErrorHandler.SQL_ILLEGAL_SYMBOL, data -> {
            JOptionPane.showMessageDialog(v, "la instrucción contiene simbolos no permitidos", "Error de consulta", JOptionPane.ERROR_MESSAGE);
        });
        ErrorHandler.registrarHandler(ErrorHandler.SQL_MISSING_PERMISSION, data -> {
            JOptionPane.showMessageDialog(v, "No tiene permiso para realizar esta acción", "Error de consulta", JOptionPane.ERROR_MESSAGE);
        });
        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            JOptionPane.showMessageDialog(v, "Ya hay un registro con esa información", "Error de inserción", JOptionPane.ERROR_MESSAGE);
        });
        ///luego data son las llaves foraneas que pueden fallar
        ErrorHandler.registrarHandler(ErrorHandler.SQL_CONSTRAINT_FAIL, data -> {
            try {
                String d = "";
                String[] dataa = ModeloBD.obtenerCamposNombresDe(data[0].toString());
                Integer[] idxs = ModeloBD.obtenerPrimariasDe(data[0].toString());

                for (int idx : idxs) {
                    d += dataa[idx]+", ";
                }
                d=d.substring(0, data.length-2);
                JOptionPane.showMessageDialog(v, "Uno o más de los datos no se encuentra en la base de datos: \n"+d, "Error de inserción", JOptionPane.ERROR_MESSAGE);

            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                JOptionPane.showMessageDialog(v, "Algunos de los datos no se encuentran en la base de datos", "Error de inserción", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }

        });
        ErrorHandler.registrarHandler(ErrorHandler.RESULT_SET_OUT_OF_BOUNDS, data -> {
            JOptionPane.showMessageDialog(v, "Ocurrió un error al obtener datos de un registro", "Error de consulta", JOptionPane.ERROR_MESSAGE);
        });
    }

    /**
     * Realiza una conexión a una BD con los parámetros dados
     * @param usuario usuario con el cual conectarse
     * @param pass contraseña del usuario
     * @param bd nombre de la BD
     * @return código de error, o 0 si fue exitosa
     */
    public int conectar(String usuario, String pass, String bd){
        try {
            ConexionBD.getConector().abrirConexion(usuario, pass, bd);
        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR: "+e.getErrorCode());
            return e.getErrorCode();
        } catch (NullPointerException e){
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            return 1;
        }
        return 0;
    }

    /**
     * Realiza una consulta a la BD
     * @param tabla nombre de la tabla a consultar en la BD
     * @param selecNombres datos a consultar en la tabla de la BD
     * @param filtroNombres campos a filtrar en la consulta a la BD
     * @param filtroValores valores a filtrar en la consulta a la BD

     */
    public ArrayList<ModeloBD> realizarConsulta(String tabla, String[] selecNombres, String[] filtroNombres, Object[] filtroValores) {

        try {
            return DAO.d.obtenerRegistros(tabla, selecNombres, filtroNombres, filtroValores);
        } catch (SQLException e) {
            handlearError(e.getErrorCode(), tabla);
            System.out.println("Error de consulta: " + e.getErrorCode());
            e.printStackTrace();
        }
        return null;
    }
    public void agregar(ModeloBD modelo){
        try {
            DAO.d.agregar(modelo);
            JOptionPane.showMessageDialog(null, "Registro agregado", "Insercion", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalAccessException e) {
            System.out.println("Error al recuperar los datos del modelo");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("Error de instruccion: " + e.getErrorCode());
            handlearError(e.getErrorCode(), modelo.getClass().getSimpleName());
        }
    }
    public void modificar(ModeloBD modelo, Object[] primariasOG){
        try {
            DAO.d.modificar(modelo, primariasOG);
            JOptionPane.showMessageDialog(null, "Registro modificado", "Modificacion", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            System.out.println("Error de modificacion: " +e.getErrorCode());
            e.printStackTrace();
        }
    }
    public void eliminar(ModeloBD r){
        try {
            DAO.d.eliminar(r);
        } catch (SQLException e) {
            handlearError(e.getErrorCode(), r.getClass().getSimpleName());
        }
    }
    public PanelHook actualizarVentana(String id, PanelHook p){
        add(p.componente, id);
        return p;
    }
    public void configurarLogin(){
        ventanaLogin.accionLogear(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Object> l = ventanaLogin.form.extraer();
                int cod = //conectar((String) l.get(0), (String) l.get(1), "Autos");
                        conectar("SANTIAGO", "santiago", "Autos");
                if(cod!=0) return;
                try {
                    control = new ABCC("Cliente");
                    add(control.ventana.componente, "principal");
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
                layout.show(getContentPane(),"principal");
            }
        });
    }
    public void cambiarALogin() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if(ventanaLogin == null){
            ventanaLogin = FormHook.crearLogin();
            configurarLogin();
            add(ventanaLogin.componente, "login");


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

    public static void main(String[] args) throws IOException, InterruptedException {
        ConexionBD.getConector().inicializar();

        ModeloBD.registrarModelo(Cliente.class);
        ModeloBD.registrarModelo(Userio.class);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Ventana v = new Ventana();

                v.setVisible(true);
                try {
                    v.cambiarALogin();
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
