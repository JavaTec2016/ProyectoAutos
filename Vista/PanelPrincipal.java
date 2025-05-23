package Vista;

import ErrorHandlin.Call;
import FormTools.CampoHook;
import FormTools.FormHook;
import FormTools.MainButtonHook;
import FormTools.PanelHook;
import Modelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;

public class PanelPrincipal extends PanelHook {
    PanelHook mainPanel;
    PanelHook topPanel;
    PanelHook panelLabel;
    PanelHook panelTablas;
    PanelHook panelUser;

    PanelHook btnLogout;
    Call logoutAccion;
    CampoHook backButton;

    LinkedHashMap<String, MainButtonHook> botonesMain;

    public PanelPrincipal(Userio usr){
        super();
        botonesMain = new LinkedHashMap<>();
        componente = new JPanel(new GridBagLayout());
        setBackground(new Color(204, 204, 204));
        mainPanel = FormHook.makeGridBagPanel();
        topPanel = FormHook.makeGridBagPanel().setBackground(new Color(0, 153, 255));
        panelLabel = FormHook.makeGridBagPanel().setBackground(new Color(255, 255, 255));;
        panelTablas = FormHook.makeGridBagPanel();

        String nom = "JEUSER";
        if(usr != null) nom = usr.getNombre();
        panelUser = FormHook.makeUserPanel(nom);

        CampoHook label = new CampoHook(new JLabel("Categorías disponibles en el sistema"))
                .setFont(new Font("Segoe UI", Font.ITALIC, 18));
        GridBagConstraints constraints = FormHook.makeConstraint(-1, -1, 0.10f, 1, GridBagConstraints.BOTH);
        appendChild("panelUser", panelUser, constraints);
        constraints.weightx = 1;
        appendChild("mainPanel", mainPanel, constraints);

        constraints.gridx = 0;
        constraints.weighty = 0.15;
        mainPanel.appendChild("topPanel", topPanel, constraints);
        constraints.weighty = 0.15;
        mainPanel.appendChild("panelLabel", panelLabel, constraints);
        constraints.weighty = 1;
        mainPanel.appendChild("panelTablas", panelTablas, constraints);

        constraints.weightx = 1;
        constraints.weighty = 1;
        label.setAlignment(SwingConstants.CENTER, SwingConstants.CENTER);
        panelLabel.appendChild("label", label, constraints);

        btnLogout = (PanelHook) childPath("panelUser/logout/btn");

        setLogoutListener();
        iniciarOpcionesMain();
    }

    /**
     * Genera un nuevo botón principal y lo agrega al panel en la columna y fila indicado.
     * La columna y fila siguen el comportamiento de un GridbagLayout.
     * @param id id del botón en el panel
     * @param columna columna del botón dentro del layout
     * @param fila fila del botón dentro del layout
     * @param lineasTexto líneas de texto del botón, centradas vertical y horizontalmente
     * @return El {@link MainButtonHook} recién agregado al panel
     */
    public MainButtonHook addOpcion(String id, int columna, int fila, String[] lineasTexto){
        MainButtonHook btn = new MainButtonHook(lineasTexto, id, new Dimension(180, 170), Color.white, Color.black, 0.6);
        btn.setTextosFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.separator.setBackground(btn.getForeground());
        GridBagConstraints constraints = FormHook.makeConstraint(columna, fila, 0, 0, GridBagConstraints.NONE);
        constraints.insets = new Insets(30, 30, 30, 30);
        panelTablas.appendChild(id, btn, constraints);

        return btn;
    }

    /**
     * Agrega botones principales para cada pantalla ABCC de la base de datos
     */
    private void iniciarOpcionesMain(){
        botonesMain.put(Cliente.class.getSimpleName(), addOpcion(Cliente.class.getSimpleName(), -1, 0, new String[]{"Clientes"})) ;
        botonesMain.put(Vendedor.class.getSimpleName(), addOpcion(Vendedor.class.getSimpleName(), -1, 0, new String[]{"Vendedores"}));
        botonesMain.put(Auto_Modelo.class.getSimpleName(), addOpcion(Auto_Modelo.class.getSimpleName(), -1, 0, new String[]{"Modelos", "de auto"}));
        botonesMain.put(Auto.class.getSimpleName(), addOpcion(Auto.class.getSimpleName(), -1, 0, new String[]{"Autos"}));
        botonesMain.put(Auto_Opcion.class.getSimpleName(), addOpcion(Auto_Opcion.class.getSimpleName(), -1, 1, new String[]{"Catálogo de", "modificaciones"}));
        botonesMain.put(Venta.class.getSimpleName(), addOpcion(Venta.class.getSimpleName(), -1, 1, new String[]{"Ventas"}));
        botonesMain.put(Cliente_Adorno.class.getSimpleName(), addOpcion(Cliente_Adorno.class.getSimpleName(), -1, 1, new String[]{"Modificaciones", "de clientes"}));
    }

    /**
     * Establece el comportamiento de clic y cambio de colores del boton de cerrar sesión
     * al clicar, se ejecuta {@link #logoutAccion}
     */
    private void setLogoutListener(){
        btnLogout.componente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logoutAccion.run(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                btnLogout.setBackground(Color.DARK_GRAY);
                btnLogout.getChild("detail").setBackground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                btnLogout.setBackground(new Color(102, 102, 102));
                btnLogout.getChild("detail").setBackground(Color.cyan);
            }
        });
    }

    /**
     * Establece la función de clic para el botón de logout
     * @param c Función a la cual se llama cuando se le da clic al botón
     */
    public void setLogoutAccion(Call c){
        logoutAccion = c;
    }
}
