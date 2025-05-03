package FormTools;

import Modelo.Usuario;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
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
            CampoHook componente = FormHook.makeVerticalListPanel(1);
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

    /**
     * Crea los elementos del formulario, labels e inputs
     */
    public void generar(){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                System.out.println(s + " generandin " + campoHook.componente);
                //asignar un panel con flow o box layout al campo, luego ponele label e input
                try{
                    CampoHook input = new CampoHook(obtenerComponente(tipoComponentes.get(s), exps.get(s)));
                    CampoHook label = new CampoHook(new JLabel(labels.get(s)));

                    campoHook.appendChild("label_"+s, label);
                    campoHook.appendChild("input_"+s, input);

                }catch (NullPointerException e){
                    System.out.println("El campo no se pudo agregar: " + tipoComponentes.get(s));
                }

            }
        });
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
    public void attachBotonesEn(CampoHook comp){
        botones.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                comp.appendChild(s, campoHook);
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
    public void attachEnSeccion(String nombre){
        attachCamposEn(secciones.get(nombre));
    }
    public void attachBotonesEnSeccion(String nombre){
        attachBotonesEn(secciones.get(nombre));
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
        return new PanelHook(new GridLayout(3, 1));
    }
    public static PanelHook makeVerticalListPanel(int rows){
        PanelHook p = new PanelHook();
        p.setLayout(new BoxLayout(p.getComponente(), BoxLayout.PAGE_AXIS));
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
                p.getComponente().setBackground(Color.lightGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                p.getComponente().setBackground(Color.white);
            }
        });
        return p;
    }
    public void agregarSeccion(String Id, PanelHook p){
        System.out.println("Nueva seccion: " + Id + p.getComponente());
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
        int celdasTotal = celdas+2;//incluir botones

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        double freeWeight = 1;
        for (int i = 0; i < datos.length; i++) {
            datos[i].getComponente().setPreferredSize(new Dimension(0, tamanio));
            int ocupa = distribucion[i];
            //convertir las celdas que ocupa el elemento a peso del gridbag
            double weight = (double) ocupa /celdasTotal;
            freeWeight -= weight;
            c.weightx = weight;
            p.addElementConstraint(Id+"_"+nombreDatos[i], datos[i], c);
        }
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

    public static FormHook crearLogin(){
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
        PanelHook vertical = FormHook.makeVerticalListPanel(0);
        PanelHook horizontal = FormHook.makeHorizontalListPanel(2);

        f.agregarSeccion("campos", vertical);
        f.agregarSeccion("botones", horizontal);

        f.generar();
        f.attachEnSeccion("campos");
        f.attachBotonesEnSeccion("botones");
        return f;
    }
}
