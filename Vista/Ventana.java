package Vista;

import ErrorHandlin.Call;
import ErrorHandlin.ErrorHandler;
import FormTools.FormHook;
import Instalador.Config;
import Instalador.DB2Ejecutor;
import Modelo.*;
import Vista.Formateadores.Graficador;
import conexion.ConexionBD;
import controlador.DAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Ventana extends JFrame {
    Login ventanaLogin;
    PanelPrincipal principal;
    ABCC control;
    CardLayout layout = null;

    JMenuBar menu;
    JMenu menuReporte;
    JMenu menuVistas;
    JMenu menuEstadisticas;

    JMenuItem reporteFactura;

    public Ventana(){
        crearMenu();
        menu.setVisible(false);
        menuReporte = menuOpcion("Reportes");
        menuVistas = menuOpcion("Vistas");
        menuEstadisticas = menuOpcion("Estadísticas");
        reporteFactura = opcionItem("Facturar..", e->{
            new PopupFactura().setVisible(true);
        });

        menuVistas.add(opcionItem("Autos disponibles", e -> {
            try {
                cambiarAABCC(Autos_Disponibles.class.getSimpleName());
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }));
        menuVistas.add(opcionItem("Modificaciones seleccionadas", e -> {
            try {
                cambiarAABCC(Opciones_Activas.class.getSimpleName());
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }));

        menuEstadisticas.add(opcionItem("Modelos más vendidos", e -> {
            try {
                Graficador.makeModelosVendidosChart();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }));

        menuEstadisticas.add(opcionItem("Modificaciones más vendidas", e -> {
            try {
                Graficador.makeModificacionesChart();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }));
        menuEstadisticas.add(opcionItem("Referencias más populares", e -> {

            try {
                Graficador.makeReferenciasDataset();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }));

        menuReporte.add(reporteFactura);
        menu.add(menuReporte);
        menu.add(menuVistas);
        menu.add(menuEstadisticas);

        setSize(500, 500);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        layout = new CardLayout();
        getContentPane().setLayout(layout);

        setVisible(true);

        try {
            cambiarALogin();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void crearMenu(){
        menu = new JMenuBar();
        menu.setBackground(new Color(0, 153, 255));
        menu.setBorder(null);
        menu.setForeground(Color.white);
        menu.setOpaque(true);
    }
    /**
     * Agrega un nuevo menu de opciones
     */
    public void agregarMenu(){

        setJMenuBar(menu);
    }
    public JMenu menuOpcion(String texto){
        JMenu opcion = new JMenu();
        opcion.setBackground(new Color(0, 153, 255));
        opcion.setText(texto);
        return opcion;
    }
    public JMenuItem opcionItem(String texto, ActionListener clic){
        JMenuItem item = new JMenuItem();
        item.setBackground(new Color(0, 153, 255));
        item.setForeground(Color.WHITE);
        item.setText(texto);
        item.setOpaque(true);

        item.addActionListener(clic);
        return item;
    }
    /**
     * Muestra un panel de error con el mensaje y título dados
     * @param msg Mensaje de la ventana
     * @param titulo Título de la ventana
     */
    public static void panelError(String msg, String titulo){
        JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Configura la acción de inicio de sesión de la ventana de login y abre una conexión con los datos de autenticación.
     * Si la autenticación es exitosa, entonces muestra el panel de control configurado para el usuario.
     * Si la autenticación falla, se relanza el error.
     */
    public void configurarLogin(){
        ventanaLogin.accionLogear(e -> {
            ArrayList<Object> l = ventanaLogin.form.extraer();
            int cod =  ConexionBD.getConector().abrirConexion2((String) l.get(0), (String) l.get(1), "Autos"); //conectar((String) l.get(0), (String) l.get(1), "Autos");
            if(cod!=0) {
                panelError("Usuario o contraseña incorrectos", "Autenticación fallida");
                return;
            };
            if(ConexionBD.getConector().getUsrLectura()) agregarMenu();
            cambiarAPrincipal(ConexionBD.getConector().getUsr());
        });
    }

    /**
     * Cambia la vista actual al panel de control
     * @param usr Usuario activo
     */
    public void cambiarAPrincipal(Userio usr){
        if(principal != null) remove(principal.componente);
        principal = new PanelPrincipal(usr);
        configurarbtnLogout();
        configurarbtnUsuarios();
        configurarBotonesPrincipal();
        menu.setVisible(true);
        add(principal.componente, "principal");
        layout.show(getContentPane(), "principal");
        revalidate();
    }

    /**
     * Cambia la pantalla actual al login
     * @throws InvocationTargetException Si ocurre un error al generar la pantalla de login con el modelo de usuario
     * @throws NoSuchMethodException Si faltan métodos del usuario necesarios para crear la pantalla
     * @throws IllegalAccessException Si la información del modelo de Usuario no es accesible
     */
    public void cambiarALogin() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if(ventanaLogin == null){
            ventanaLogin = FormHook.crearLogin();
            configurarLogin();
            add(ventanaLogin.componente, "login");
        }
        menu.setVisible(false);
        layout.show(getContentPane(), "login");
        revalidate();
    }

    /**
     * Establece una pantalla ABCC como la actual y la muestra
     * @param nuevo pantalla ABCC a mostrar
     */
    public void refrescarControl(ABCC nuevo){
        if(control != null) remove(control.ventana.componente);
        control = nuevo;
        control.setBackAccion(e -> {
            cambiarAPrincipal(ConexionBD.getConector().getUsr());
        });

        add(control.ventana.componente, "ABCC");
        layout.show(getContentPane(), "ABCC");
        revalidate();

        if(!ConexionBD.getConector().getUsrEscritura()){
            control.setRegistrosEditables(false);
            control.setRegistrosEliminables(false);
            control.setVisibleAgregar(false);
        }
    }

    /**
     * Busca la pantalla ABCC según el nombre modelo dado y, si existe, la muestra
     * @param tabla nombre del modelo
     * @throws InvocationTargetException Si ocurre un error al generar la pantalla ABCC
     * @throws NoSuchMethodException Si no existen metodos necesarios en el modelo generar la pantalla ABCC
     * @throws IllegalAccessException Si la información del modelo no es accesible al generar la pantalla ABCC
     */
    public void cambiarAABCC(String tabla) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if(tabla.equals(Userio.class.getSimpleName())){
            refrescarControl(new ABCCUserio());
        }else
        if(tabla.equals(Cliente.class.getSimpleName())){
            refrescarControl(new ABCCCliente());
        }else
        if(tabla.equals(Vendedor.class.getSimpleName())){
            refrescarControl(new ABCCVendedor());
        }else
        if(tabla.equals(Auto_Modelo.class.getSimpleName())){
            refrescarControl(new ABCCAuto_Modelo());
        }else
        if(tabla.equals(Auto.class.getSimpleName())){
            refrescarControl(new ABCCAuto());
        }else
        if(tabla.equals(Auto_Opcion.class.getSimpleName())){
            refrescarControl(new ABCCAuto_Opcion());
        }else
        if(tabla.equals(Opciones_Activas.class.getSimpleName())){
            refrescarControl(new ABCCOpciones_Activas());
        }else
        if(tabla.equals(Venta.class.getSimpleName())){
            refrescarControl(new ABCCVenta());
        }else
        if(tabla.equals(Cliente_Adorno.class.getSimpleName())){
            refrescarControl(new ABCCCliente_Adorno());
        }else
        if(tabla.equals(Autos_Disponibles.class.getSimpleName())){
            refrescarControl(new ABCCAutos_Disponibles());
        }
    }

    /**
     * Itera por el mapa de los botones principales del panel de control.
     * Establece la acción de clicado de cada uno para mostrar su respectivo ABCC, según su ID en el mapa
     */
    public void configurarBotonesPrincipal(){
        principal.botonesMain.forEach((id, btn) -> btn.setMouseClick(e -> {
            try {
                if(!ConexionBD.getConector().getUsrLectura()){
                    panelError("No tiene permiso para consultar datos", "Permisos insuficientes");
                    return;
                }
                cambiarAABCC(id);
                System.out.println(id);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }));
    }
    /**
     * Configura la acción del boton de cierre de sesión para cerrar la conexión y cambiar la pantalla al login
     */
    public void configurarbtnLogout(){
        principal.setLogoutAccion(data -> {
            try {
                ConexionBD.getConector().cerrarConexion();
                menu.setVisible(false);
                layout.show(getContentPane(), "login");
            } catch (SQLException ex) {
                System.out.println("Error al cerrar sesion: ");
                if(ex.getErrorCode() == ErrorHandler.ERR_TRANSACTION_PENDING){
                    int accion = panelCambios("Hay cambios sin guardar, desea descartarlos?", "Cambios sin guardar");

                    try {
                        if(accion == 0) ConexionBD.getConector().commit();
                        if(accion == 1) ConexionBD.getConector().rollback();
                        ConexionBD.getConector().cerrarConexion();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }
                ex.printStackTrace();
            }
        });
    }
    public void configurarbtnUsuarios(){
        principal.setUsuariosAccion(data -> {
            System.out.println("Cambiando a usuarios...");
            try {
                ConexionBD.getConector().cambiarBD(Userio.class.getSimpleName());
                cambiarAABCC(Userio.class.getSimpleName());

                control.setBackAccion(e -> {
                    try {
                        ConexionBD.getConector().cambiarBD("Autos");
                    } catch (SQLException ex) {
                        System.out.println("Error al salir de Usuarios:");
                        ex.printStackTrace();
                        ErrorHandler.ejecutarHandler(ex.getErrorCode(), (Object) null);
                    }

                });
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                System.out.println("Error al cambiar a Usuarios:");
                e.printStackTrace();
                ErrorHandler.ejecutarHandler(e.getErrorCode(), (Object) null);

            }
        });
    }
    public static int panelCambios(String txt, String titulo){
        return JOptionPane.showOptionDialog(null, txt, titulo, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"Guardar", "Descartar", "Cancelar"}, "Cancelar");
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        boolean cambios = ConexionBD.getConector().autoInstalar();
        if(cambios){
            try {
                ConexionBD.getConector().abrirConexion(Config.USER, Config.PASS, Userio.class.getSimpleName());
                DAO.d.agregar(new Userio("admin", "admin", true, true, true));
                ConexionBD.getConector().cerrarConexion();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                if(e.getErrorCode() == ErrorHandler.SQL_DUPLICATE_ENTRY) {
                    System.out.println("Ya hay un usuario administrador");
                }else{

                    panelError("Ocurrió un error al generar al usuario administrador", "Error de instalación");
                    e.printStackTrace();
                }
            }
        }
        //DB2Ejecutor.instalarBasesSencillo();
        ModeloBD.registrarModelos();
        SwingUtilities.invokeLater(() -> {
            Ventana v = new Ventana();

        });
    }
}
