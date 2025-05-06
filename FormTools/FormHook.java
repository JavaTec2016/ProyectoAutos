package FormTools;

import Modelo.ModeloBD;
import Modelo.Usuario;
import conexion.ConexionBD;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class FormHook {
    private LinkedHashMap<String, CampoHook> botones;
    private LinkedHashMap<String, CampoHook> campos;

    private LinkedHashMap<String, String> labels;
    private LinkedHashMap<String, String> tipoDatos;
    private LinkedHashMap<String, String> tipoComponentes;
    private LinkedHashMap<String, Integer> longitudes;
    private LinkedHashMap<String, Boolean> nonulos;
    private LinkedHashMap<String, String[]> especiales;
    private LinkedHashMap<String, String> exps;

    private int width = 0;
    private int height = 0;

    private LinkedHashMap<String, PanelHook> secciones;

    public FormHook(String[] camposNombres, String[] aLabels, String[] aTipoDatos, String[] aTipoComponentes, Integer[] aLongitudes, Boolean[] aNonulos, String[][] aEspeciales, String[] aExps){
        secciones = new LinkedHashMap<String, PanelHook>();
        botones = new LinkedHashMap<String, CampoHook>();
        campos = new LinkedHashMap<String, CampoHook>();
        labels = new LinkedHashMap<String, String>();
        tipoDatos = new LinkedHashMap<>();
        tipoComponentes = new LinkedHashMap<>();
        longitudes = new LinkedHashMap<>();
        nonulos = new LinkedHashMap<>();
        especiales = new LinkedHashMap<>();
        exps = new LinkedHashMap<>();

        for (int i = 0; i < camposNombres.length; i++) {
            String nombre = camposNombres[i];
            CampoHook componente = FormHook.makeGridBagPanel();
            campos.put(camposNombres[i], componente);
            labels.put(nombre, aLabels[i]);
            tipoDatos.put(nombre, aTipoDatos[i]);
            tipoComponentes.put(nombre, aTipoComponentes[i]);
            longitudes.put(nombre, aLongitudes[i]);
            nonulos.put(nombre, aNonulos[i]);
            especiales.put(nombre, aEspeciales[i]);
            exps.put(nombre, aExps[i]);
        }
    }
    public void setCamposSize(Integer w, Integer h){

        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                int wd;
                int hg;

                if(w == null) wd = campoHook.componente.getWidth();
                else wd = w;
                if(h == null) hg = campoHook.componente.getHeight();
                else hg = h;

                campoHook.setPreferredSize(new Dimension(wd, hg));
            }
        });
    }
    public void setLabelsSize(Dimension d){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                campoHook.getChild("label").setPreferredSize(d);
            }
        });
    }
    public void setLabelsColor(Color c){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                campoHook.getChild("label").setForeground(c);
            }
        });
    }
    public void setInputsSize(Dimension d){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                campoHook.getChild("input").setPreferredSize(d);
            }
        });
    }
    public CampoHook getBoton(String nom){
        return botones.get(nom);
    }
    /**
     * Crea los elementos del formulario, labels e inputs
     */
    public void generar(){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                //System.out.println(s + " APENDACION " + campoHook.componente);
                //asignar un panel con flow o box layout al campo, luego ponele label e input
                try{
                    CampoHook input = new CampoHook(obtenerComponente(tipoComponentes.get(s), exps.get(s)));
                    CampoHook label = new CampoHook(new JLabel(labels.get(s)));

                    GridBagConstraints gc = makeConstraint(0, -1, GridBagConstraints.NONE);
                    campoHook.appendChild("label", label, gc);
                    campoHook.appendChild("input", input, gc);

                }catch (NullPointerException e){
                    System.out.println("El campo no se pudo agregar: " + tipoComponentes.get(s));
                }

            }
        });
    }
    public void generar(GridBagConstraints gc){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                //System.out.println(s + " APENDACION " + campoHook.componente);
                //asignar un panel con flow o box layout al campo, luego ponele label e input
                try{
                    CampoHook input = new CampoHook(obtenerComponente(tipoComponentes.get(s), exps.get(s)));
                    CampoHook label = new CampoHook(new JLabel(labels.get(s)));

                    campoHook.appendChild("label", label, gc);
                    campoHook.appendChild("input", input, gc);

                }catch (NullPointerException e){
                    System.out.println("El campo no se pudo agregar: " + tipoComponentes.get(s));
                }

            }
        });
    }
    public ArrayList<Object> extraer(){
        ArrayList<Object> datos = new ArrayList<>();
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                 JComponent c = campoHook.getChild("input").componente;
                 datos.add(extraerDato(c));
            }
        });
        return datos;
    }
    public Object extraerDato(JComponent campo){

        Class<? extends JComponent> clase = campo.getClass();
        switch (clase.getSimpleName().toLowerCase()){
            case "jtextfield":
                String t = ((JTextField)campo).getText();
                if(t.isEmpty()) return null;
                else return t;

            case "jcombobox":
                return ((JComboBox)campo).getSelectedItem();

            case "checkbox":
                return ((JCheckBox)campo).isSelected();

            default: return null;
        }
    }
    public void setCamposOpaque(boolean b){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                campoHook.componente.setOpaque(b);
            }
        });
    }
    public void setCamposOpaque(boolean b, String ...campos){
        for (String campo : campos) {
            this.campos.get(campo).getComponente().setOpaque(b);
        }
    }
    public void attachCamposEn(CampoHook comp){

        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                //System.out.println(s + " Attachin " + campoHook);
                comp.appendChild(s, campoHook);
            }
        });
    }
    public void attachCamposEn(CampoHook comp, Object constraints){

        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                //System.out.println(s + " Attachin " + campoHook);
                comp.appendChild(s, campoHook, constraints);
            }
        });
    }
    public void attachBotonesEn(CampoHook comp){
        botones.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                comp.appendChild(s, campoHook);
            }
        });
    }
    public void attachBotonesEn(CampoHook comp, Object constraints){
        botones.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                comp.appendChild(s, campoHook, constraints);
            }
        });
    }
    public void attachSeccionesEn(CampoHook comp){
        secciones.forEach(new BiConsumer<String, PanelHook>() {
            @Override
            public void accept(String s, PanelHook panelHook) {
                comp.appendChild(s, panelHook);
            }
        });
    }
    public void attachSeccionesEn(CampoHook comp, Object constraints){
        secciones.forEach(new BiConsumer<String, PanelHook>() {
            @Override
            public void accept(String s, PanelHook panelHook) {
                comp.appendChild(s, panelHook, constraints);
            }
        });
    }
    public void attachEnSeccion(String nombre){
        attachCamposEn(secciones.get(nombre));
    }
    public void attachEnSeccion(String nombre, Object constraints){
        attachCamposEn(secciones.get(nombre), constraints);
    }
    public void attachBotonesEnSeccion(String nombre){
        attachBotonesEn(secciones.get(nombre));
    }
    public void attachBotonesEnSeccion(String nombre, Object constraints){
        attachBotonesEn(secciones.get(nombre), constraints);
    }
    public void attachSeccionesA(PanelHook p){
        attachSeccionesEn(p);
    }
    /**
     * le das un tipo de componente valido y avienta el jcomponent equivalente
     * @param tipo tipo de componente que se desea
     * @param regex restricciones para cajas de texto
     * @return componente swin
     */
    static JComponent obtenerComponente(String tipo, String regex){
        switch (tipo){
            case "textfield": return makeRestrictedTextField(regex);
            case "datefield": return new JDatePicker();
            case "combobox": return  new JComboBox<>();
        }
        System.out.println("Tipo de comp desconocido: " + tipo);
        return null;
    }

    /**
     * Permite asignarle un regex a un campo de texto, para evitar que el usuario le ponga cosas que no son
     * @param regex el regex a compilar
     * @return la caja de texto bien despierta
     */
    static JComponent makeRestrictedTextField(String regex){

        JTextField txt = new JTextField();
        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                Pattern patron = Pattern.compile(regex);
                String testear = ""+ txt.getText()+e.getKeyChar();
                if(patron.matcher(testear).matches()){
                    super.keyTyped(e);
                }else {
                    //System.out.println("incorrec: "+ testear + "  " + regex);
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) { //el caracter esta permitido en el field
                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
        return txt;
    }
    static PanelHook makeFlowPanel(){
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.CENTER);
        return new PanelHook(fl);
    }
    public static PanelHook makeVerticalListPanel(){
        return new PanelHook(new GridLayout(0, 1));
    }
    public static PanelHook makeVerticalListPanel(int rows){
        PanelHook p = new PanelHook();
        p.setLayout(new BoxLayout(p.getComponente(), BoxLayout.PAGE_AXIS));
        p.getComponente().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                //p.getComponente().setBackground(Color.lightGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                //p.getComponente().setBackground(Color.white);
            }
        });
        return p;
    }
    public static PanelHook makeHorizontalListPanel(){
        return new PanelHook(new GridLayout(1, 0));
    }
    public static PanelHook makeHorizontalListPanel(int columns){
        return new PanelHook(new GridLayout(1, columns));
    }
    public static PanelHook makeGridBagPanel(){
        PanelHook p = new PanelHook(new GridBagLayout());
        p.getComponente().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                //p.getComponente().setBackground(Color.lightGray);
                //System.out.println(p.id);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                //p.getComponente().setBackground(Color.white);
            }
        });
        return p;
    }
    public void agregarSeccion(String Id, PanelHook p){
        //System.out.println("Nueva seccion: " + Id + p.getComponente());
        secciones.put(Id, p);
    }
    public static PanelHook crearRegistro(String[] info){
        PanelHook p = makeHorizontalListPanel();
        p.addElement("Prueba", new JLabel("Buenas"));
        p.addElement("btnEditar", new JButton("Editar"));
        p.addElement("btnEliminar", new JButton("Eliminar"));
        return p;
    }
    public static PanelHook crearRegistroGridBag(String Id, CampoHook[] datos, String[] nombreDatos, int[] distribucion, int celdas, int tamanio){
        PanelHook p = makeGridBagPanel();
        p.setPreferredSize(new Dimension(0, tamanio));
        p.setMaximumSize(new Dimension(32767, tamanio));
        int celdasTotal = celdas+2;//incluir botones

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        double freeWeight = 1;
        for (int i = 0; i < datos.length; i++) {
            //datos[i].getComponente().setPreferredSize(new Dimension(0, tamanio));
            int ocupa = distribucion[i];
            //convertir las celdas que ocupa el elemento a peso del gridbag
            double weight = (double) ocupa /celdasTotal;
            freeWeight -= weight;
            c.weightx = weight;
            p.addElementConstraint(Id+"_"+nombreDatos[i], datos[i], c);

        }
        p.componente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                p.setBackground(Color.lightGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                p.setBackground(Color.white);
            }
        });
        //queda solo el weight restante pa los 2 botones
        c.weightx = freeWeight/2;
        c.fill = GridBagConstraints.NONE;
        //p.addElement("Prueba", new JLabel("Buenas"));
        p.addElementConstraint("btnEditar", new JButton("Editar"), c);
        p.addElementConstraint("btnEliminar", new JButton("Eliminar"), c);
        return p;
    }
    public static PanelHook crearRegistroGridBag(String Id, JComponent[] datos, String[] nombreDatos, int[] distribucion, int celdas, int tamanio){
        CampoHook[] cs = new CampoHook[datos.length];
        for (int i = 0; i < datos.length; i++) {
            cs[i] = new CampoHook(datos[i]);
        }
        return crearRegistroGridBag(Id, cs, nombreDatos, distribucion, celdas, tamanio);
    }
    public static ScrollHook crearScroll() {
        PanelHook p = makeVerticalListPanel();
        return new ScrollHook(p);
    }
    public static ScrollHook crearScroll(int rows) {
        PanelHook p = makeVerticalListPanel(rows);
        return new ScrollHook(p);
    }
    public static ScrollHook crearScroll(int x, int y, int w, int h){
        ScrollHook s = crearScroll();
        s.setBounds(x,y,w,h);
        s.getView().setBounds(x,y,w,h);
        return s;
    }
    public static ScrollHook crearScroll(int x, int y, int w, int h, int rowsVisible){
        ScrollHook s = crearScroll(rowsVisible);
        s.setBounds(x,y,w,h);
        //s.getView().setBounds(x,y,w,h);
        return s;
    }
    public static ScrollHook crearScroll(int w, int h){
        ScrollHook s = crearScroll();
        s.getComponente().setSize(w, h);
        s.getView().getComponente().setSize(w, h);
        return s;
    }
    public static GridBagConstraints makeVerticalConstraint(int gridx){
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gridx;
        return gc;
    }
    public static GridBagConstraints makeConstraint(int gridx, int gridy, int fill){
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gridx;
        gc.gridy = gridy;
        gc.fill = fill;
        return gc;
    }
    public static GridBagConstraints makeConstraint(int gridx, int gridy,float weightx, float weighty, int fill){
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gridx;
        gc.gridy = gridy;
        gc.weightx = weightx;
        gc.weighty = weighty;
        gc.fill = fill;
        return gc;
    }
    public void attachCamposGridbag(int vertical){
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 1-vertical;
        attachEnSeccion("campos", gc);
    }
    public JComponent getCampoComp(String nombre){
        return campos.get(nombre).componente;
    }
    public static ImageHook crearImagen(String nombre){

        ImageHook i = new ImageHook("/assets/usr.png");
        return i;
    }
    public static void crearImagenLabel(String nombre){

    }
    public static PanelHook makeUserPanel(String usr){
        PanelHook sidebar = FormHook.makeGridBagPanel().setBackground(new Color(102, 102, 102))
                .setPreferredSize(new Dimension(200, 0));
        sidebar.componente.setMinimumSize(new Dimension(200, 10));

        PanelHook panelUser = FormHook.makeGridBagPanel();
        PanelHook panelBtns = FormHook.makeGridBagPanel();
        PanelHook panelLogout = FormHook.makeGridBagPanel();

        panelUser.componente.setOpaque(false);
        panelBtns.componente.setOpaque(false);
        panelLogout.componente.setOpaque(false);

        PanelHook i = new PanelHook();
        CampoHook nom = new CampoHook(new JLabel(usr))
                .setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15))
                .setForeground(Color.white);

        i.componente = FormHook.crearImagen("usr");
        i.componente.setOpaque(false);

        GridBagConstraints gp = FormHook.makeConstraint(0, -1, GridBagConstraints.NONE);
        gp.insets.top = 5;
        panelUser.appendChild("imagen", i, gp);
        gp.insets.bottom = 5;
        panelUser.appendChild("nombre", nom, gp);

        gp = FormHook.makeConstraint(0, -1, 1, 0.15f, GridBagConstraints.BOTH);
        sidebar.appendChild("user", panelUser, gp);
        gp.weighty = .8;
        sidebar.appendChild("btns", panelBtns, gp);
        gp.weighty = 1-gp.weighty;
        sidebar.appendChild("logout", panelLogout, gp);

        ///BOTONES EN PANEL LATERAL
        gp.fill = GridBagConstraints.HORIZONTAL;
        PanelHook btnLogout = FormHook.makeSidebarBoton("Cerrar sesión", sidebar.componente.getBackground(), Color.CYAN, Color.white);
        btnLogout.setPreferredSize(new Dimension(0, 50));
        panelLogout.appendChild("btn", btnLogout, gp);

        return sidebar;
    }
    public static PanelHook crearFormulario(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        FormHook f = new FormHook(
                ModeloBD.obtenerCampoNombresDe(ModeloBD.getModelo(modelo)),
                ModeloBD.obtenerLabelsDe(modelo),
                ModeloBD.obtenerCampoTiposSQLDe(modelo),
                ModeloBD.obtenerCamposComponentesDe(modelo),
                ModeloBD.obtenerLongitudesDe(modelo),
                ModeloBD.obtenerNoNulosDe(modelo),
                ModeloBD.obtenerEspecialesDe(modelo),
                ModeloBD.obtenerExpresionesDe(modelo)
        );

        ///AREAS DE LA VENTANA

        PanelHook holder = makeGridBagPanel().setBackground(new Color(102, 102, 102));
        PanelHook header = makeGridBagPanel().setBackground(Color.white);
        PanelHook form = makeGridBagPanel();
        PanelHook foot = makeGridBagPanel();
        form.componente.setOpaque(false);
        foot.componente.setOpaque(false);

        GridBagConstraints gh = makeConstraint(0, -1, 1, 0.15f, GridBagConstraints.BOTH);
        holder.appendChild("header", header, gh);
        gh.weighty = 0.8;
        holder.appendChild("campos", form, gh);
        gh.weighty = 1 - gh.weighty-0.15;
        holder.appendChild("foot", foot, gh);

        ///TITULO

        CampoHook title = new CampoHook(new JLabel(modelo+"s"))
                .setForeground(Color.blue)
                .setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));

        header.appendChild("title", title, makeConstraint(0,0,1,1,GridBagConstraints.BOTH));
        f.agregarSeccion("header", header);
        f.agregarSeccion("campos", form);

        ///FORMULARIO
        GridBagConstraints gf = makeConstraint(0, -1, 1, 0, GridBagConstraints.HORIZONTAL);
        GridBagConstraints gc = makeConstraint(0, -1, 0, 0, GridBagConstraints.NONE);
        gc.insets.top = 4;
        gc.insets.bottom = 4;
        f.generar(gc);
        f.attachBotonesEnSeccion("foot");
        f.setLabelsSize(new Dimension(300, 20));
        f.setInputsSize(new Dimension(300, 30));
        f.setLabelsColor(Color.white);
        f.setCamposOpaque(false);
        f.attachCamposEn(form, gf);

        ///FOOT (nomas botones

        CampoHook btnAgregar = FormHook.makeFormBoton("AGREGAR", Color.GRAY, Color.CYAN);
        GridBagConstraints gb = makeConstraint(0, 0, 1, 1, GridBagConstraints.BOTH);
        gb.insets = new Insets(10,10,10,10);
        foot.appendChild("btnAgregar", btnAgregar, gb);

        holder.form = f;
        return holder;
    }
    public static CampoHook makeFormBoton(String txt, Color bkg, Color frg){
        CampoHook c = new CampoHook(new JButton(txt));
        c.setBackground(bkg);
        c.setForeground(frg);
        c.componente.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        return c;
    }
    public static PanelHook crearLogin(Dimension d){
        FormHook f = new FormHook(
                Usuario.obtenerCamposNombres(),
                Usuario.obtenerLabels(),
                Usuario.obtenerCampoTiposSQL(),
                Usuario.obtenerCamposComponentes(),
                Usuario.obtenerLongitudes(),
                Usuario.obtenerNoNulos(),
                Usuario.obtenerEspeciales(),
                Usuario.obtenerExpresiones()
        );
        PanelHook vertical = FormHook.makeGridBagPanel().setBackground(Color.WHITE);
        PanelHook horizontal = FormHook.makeFlowPanel().setBackground(Color.WHITE);

        //logineo anuncio

        PanelHook header = FormHook.makeGridBagPanel()
                .setPreferredSize(new Dimension(800, 100))
                .setForeground(Color.white)
                .setBackground(new Color(48, 48, 255));

        CampoHook c = new CampoHook(new JLabel("Autos Amistosos"))
                .setFont(new Font(Font.SANS_SERIF, Font.BOLD, 45))
                .setAlignment(SwingConstants.CENTER, SwingConstants.CENTER)
                .setForeground(Color.white);

        CampoHook i = new CampoHook(new JLabel("Inicie sesion"))
                .setForeground(Color.white);;
        header.appendChild("nombre", c, makeVerticalConstraint(0));
        header.appendChild("sesion", i, makeVerticalConstraint(0));

        //formatear campos de interaccion
        f.agregarSeccion("header", header);
        f.agregarSeccion("campos", vertical);
        f.agregarSeccion("botones", horizontal);

        f.attachCamposGridbag(1);
        //f.attachEnSeccion("campos");
        f.attachBotonesEnSeccion("botones");

        f.generar();
        //f.setCamposSize(null, 75);
        f.setCamposSizes(
                Usuario.obtenerCamposNombres(),
                new Dimension[]{
                        new Dimension(620, 150),
                        new Dimension(620, 150),
                        new Dimension(200, 75),
                        new Dimension(200, 75),
                        new Dimension(200, 75),
                }
        );
        f.whiteList("usuario", "password");
        f.setCamposOpaque(false);
        //((FlowLayout)f.getCampoComp("usuario").getLayout()).setAlignment(FlowLayout.LEFT);
        //((FlowLayout)f.getCampoComp("password").getLayout()).setAlignment(FlowLayout.LEFT);

        //formatear campos de logoe

        f.campos.get("usuario").getChild("label").setPreferredSize(new Dimension(600, 70));
        ((JLabel)f.campos.get("usuario").getChild("label").componente).setFont(
                new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 22)
        );
        f.campos.get("password").getChild("label").setPreferredSize(new Dimension(600, 70));
        ((JLabel)f.campos.get("password").getChild("label").componente).setFont(
                new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 22)
        );
        ((JTextField)f.campos.get("usuario").getChild("input").componente).setPreferredSize(
                new Dimension(600, 60)
        );
        ((JTextField)f.campos.get("password").getChild("input").componente).setPreferredSize(
                new Dimension(600, 60)
        );

        //organizar paneles mas bonito

        PanelHook panelCentral = FormHook.makeVerticalListPanel(0);

        System.out.println(panelCentral.componente.getSize());
        f.attachSeccionesEn(panelCentral);

        PanelHook panelLogin = FormHook.makeGridBagPanel();
        panelLogin.id = "id";

        panelLogin.appendChild("izquierda", new PanelHook()
                .setBackground(Color.LIGHT_GRAY),
                makeConstraint(0, 0, .4f, 1f, GridBagConstraints.BOTH));

        panelLogin.appendChild("central", panelCentral,
                makeConstraint(1, 0, .2f, .7f, GridBagConstraints.NONE));

        panelLogin.appendChild("derecha", new PanelHook()
                        .setBackground(Color.LIGHT_GRAY),
                makeConstraint(2, 0, .4f, 1f, GridBagConstraints.BOTH));
        //panelLogin.setPreferredSize(new Dimension(2000, 1300));

        CampoHook btnLogear = new CampoHook(new JButton("Iniciar sesion"))
                .setBackground(new Color(10, 150, 10))
                .setForeground(Color.white)
                .setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15))
                .setPreferredSize(new Dimension(300, 50));
        horizontal.setPreferredSize(new Dimension(620, 100));
        horizontal.appendChild("btnLogear", btnLogear);
        f.botones.put("btnLogear", btnLogear);
        PanelHook wrap = new PanelHook();

        //panelLogin.componente.setMaximumSize(new Dimension((int) (d.width*0.75), (int) (d.height*0.75)));
        wrap.appendChild("log", panelLogin);

        panelLogin.form = f;
        return panelLogin;
    }
    public static PanelHook crearABCC(String modelo, ArrayList<ModeloBD> mds) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        PanelHook holder = FormHook.makeGridBagPanel().setBackground(Color.white);
        PanelHook topbar = FormHook.makeGridBagPanel().setBackground(new Color(0, 102, 204));
        PanelHook tableHolder = FormHook.makeGridBagPanel();
        PanelHook main = FormHook.makeGridBagPanel();
        ScrollHook tabla = FormHook.crearScroll(0);

        PanelHook sidebar = FormHook.makeGridBagPanel().setBackground(new Color(102, 102, 102));

        //gc del panel lateral
        GridBagConstraints gc = FormHook.makeConstraint(-1, -1, 0, 1, GridBagConstraints.VERTICAL);


        ////INFO DE USUARIO EN PANEL LATERAL o de formulario
        sidebar = FormHook.crearFormulario(modelo);//FormHook.makeUserPanel("JEUSER");
        sidebar.setPreferredSize(new Dimension(400, 0));
        sidebar.componente.setMinimumSize(new Dimension(400, 0));
        holder.appendChild("sidebar", sidebar, gc);

        //gc de la info main

        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = .75;
        gc.weighty = 1;
        holder.appendChild("main", main, gc);
        //gc de la barra main
        gc.gridx = 0;
        gc.weighty = .25;
        main.appendChild("topbar", topbar, gc);
        //gc del panel de la tabla
        gc.weighty = 1-gc.weighty;
        main.appendChild("tableHolder", tableHolder, gc);
        //gc de la tabla

        gc = FormHook.makeConstraint(-1, -1, 1.0f, 1.0f, GridBagConstraints.BOTH);
        tableHolder.appendChild("tabla", tabla, gc);

        //rellenado de tabla
        FormHook.rellenarTabla(tabla, mds);


        ///LECTURA DE REGISTROS Y LLENADO DE TABLA
        tabla.getView().appendChild("reg1", FormHook.crearRegistroGridBag("reg1",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                164
        ));
        tabla.getView().appendChild("reg2", FormHook.crearRegistroGridBag("reg2",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                64
        ));
        tabla.getView().appendChild("reg3", FormHook.crearRegistroGridBag("reg3",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                164
        ));
        tabla.getView().appendChild("reg4", FormHook.crearRegistroGridBag("reg4",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                64
        ));
        tabla.getView().appendChild("reg5", FormHook.crearRegistroGridBag("reg5",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                164
        ));
        tabla.getView().appendChild("reg6", FormHook.crearRegistroGridBag("reg6",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                64
        ));
        tabla.getView().appendChild("reg7", FormHook.crearRegistroGridBag("reg7",
                new JComponent[]{new JLabel("Malas"), new JLabel("sincomil")},
                new String[]{"ID", "cantidad"},
                new int[]{10, 10},
                20,
                164
        ));
        ///CONFIGURACION DE BOTONES

        return holder;
    }

    public static PanelHook makeSidebarBoton(String txt, Color bkg, Color dtl, Color txc, MouseAdapter adp){
        PanelHook panelBtn = FormHook.makeGridBagPanel().setBackground(bkg);
        PanelHook panelDtl = new PanelHook().setBackground(dtl);
        CampoHook lbl = new CampoHook(new JLabel(txt)).setForeground(txc);

        GridBagConstraints gc = makeConstraint(-1, -1, 0.1f, 1, GridBagConstraints.BOTH);
        panelBtn.appendChild("detail", panelDtl, gc);
        gc.weightx = 1 - gc.weightx;
        panelBtn.appendChild("label", lbl, gc);

        panelBtn.componente.addMouseListener(adp);
        return panelBtn;
    }
    public static PanelHook makeSidebarBoton(String txt, Color bkg, Color dtl, Color txc){
        PanelHook panelBtn = FormHook.makeGridBagPanel().setBackground(bkg);
        PanelHook panelDtl = new PanelHook().setBackground(dtl);
        CampoHook lbl = new CampoHook(new JLabel(txt)).setForeground(txc);

        lbl.setAlignment(SwingConstants.CENTER, SwingConstants.CENTER);
        GridBagConstraints gc = makeConstraint(-1, -1, 0.075f, 1, GridBagConstraints.BOTH);
        panelBtn.appendChild("detail", panelDtl, gc);
        gc.weightx = 1 - gc.weightx;
        panelBtn.appendChild("label", lbl, gc);

        return panelBtn;
    }
    public static void rellenarTabla(ScrollHook tabla, ArrayList<ModeloBD> mds){
        if(mds != null){
            final int[] i = {0};
            mds.forEach(new Consumer<ModeloBD>() {
                @Override
                public void accept(ModeloBD modeloBD) {

                    LinkedHashMap<String, Object> datos = modeloBD.getInfoImportante();
                    JComponent[] display = new JComponent[datos.size()];
                    String[] ids = new String[datos.size()];
                    int[] tamaniosAuto = new int[datos.size()];
                    int tamanioTotal = datos.size();
                    for (int i = 0; i < display.length; i++) {
                        display[i] = new JLabel(datos.values().stream().toList().get(i).toString());
                        ids[i] = datos.keySet().stream().toList().get(i);
                        tamaniosAuto[i] = 1;
                    }

                    PanelHook reg = FormHook.crearRegistroGridBag(""+(i[0]), display, ids, tamaniosAuto, tamanioTotal, 100);
                    tabla.getView().appendChild(""+i[0], reg);
                    i[0]++;
                }
            });
        }
    }
    /**
     * elimina los campos cuyos nombres no coincidan con los nombres dados
     * @param nom lista de nombres que permanecen
     */
    public void whiteList(String ...nom){
        ArrayList<String> eliminar = new ArrayList<>();
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String nombre, CampoHook campoHook) {
                boolean whitelist = false;
                for (String s : nom) {
                    whitelist = whitelist || (Objects.equals(s, nombre));
                }
                if(!whitelist) { eliminar.add(nombre); }
            }
        });

        for (String s : eliminar) {
            CampoHook c = campos.get(s);

            if(c.parent != null){
                c.parent.removeChild(s);
            }
            campos.remove(s);
        }

    }
    public void setCamposSizes(String[] campoNombres, Dimension[] sizes){
        for (int i = 0; i < campoNombres.length; i++) {
            Dimension d = sizes[i];
            String nombre = campoNombres[i];
            if(!campos.containsKey(nombre)) continue;

            JComponent compo = campos.get(nombre).componente;

            if(d.width < 0) d.width = compo.getWidth();
            if(d.height < 0) d.height = compo.getHeight();

            campos.get(nombre).componente.setPreferredSize(d);
        }

    }
}
