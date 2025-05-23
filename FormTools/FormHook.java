package FormTools;

import ErrorHandlin.Call;
import Modelo.ModeloBD;
import Modelo.Userio;
import Vista.Login;
import Vista.Registro;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FormHook {
    private LinkedHashMap<String, CampoHook> botones;
    private LinkedHashMap<String, CampoHook> campos;

    private LinkedHashMap<String, String> labels;
    private LinkedHashMap<String, String> tipoDatosSQL;
    private LinkedHashMap<String, Class<?>> tipoDatos;
    private LinkedHashMap<String, String> tipoComponentes;
    private LinkedHashMap<String, Integer> longitudes;
    private LinkedHashMap<String, Integer> umbrales;
    private LinkedHashMap<String, Boolean> nonulos;
    private LinkedHashMap<String, String[]> especiales;
    private LinkedHashMap<String, String> exps;
    private LinkedHashMap<String, String> vals;

    private int width = 0;
    private int height = 0;

    private LinkedHashMap<String, PanelHook> secciones;

    public FormHook(String[] camposNombres, String[] aLabels, String[] aTipoDatos, String[] aTipoComponentes, Integer[] aLongitudes, Boolean[] aNonulos, String[][] aEspeciales, String[] aExps){
        secciones = new LinkedHashMap<String, PanelHook>();
        botones = new LinkedHashMap<String, CampoHook>();
        campos = new LinkedHashMap<String, CampoHook>();
        labels = new LinkedHashMap<String, String>();
        tipoDatosSQL = new LinkedHashMap<>();
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
            tipoDatosSQL.put(nombre, aTipoDatos[i]);
            tipoComponentes.put(nombre, aTipoComponentes[i]);
            longitudes.put(nombre, aLongitudes[i]);
            nonulos.put(nombre, aNonulos[i]);
            especiales.put(nombre, aEspeciales[i]);
            exps.put(nombre, aExps[i]);
        }
    }
    public FormHook(String[] camposNombres, String[] aLabels, String[] aTipoDatos, Class<?>[] aClaseDatos, String[] aTipoComponentes, Integer[] aLongitudes, Integer[] aUmbrales, Boolean[] aNonulos, String[][] aEspeciales, String[] aExps, String[] aVals){
        secciones = new LinkedHashMap<String, PanelHook>();
        botones = new LinkedHashMap<String, CampoHook>();
        campos = new LinkedHashMap<String, CampoHook>();
        labels = new LinkedHashMap<String, String>();
        tipoDatosSQL = new LinkedHashMap<>();
        tipoDatos = new LinkedHashMap<>();
        tipoComponentes = new LinkedHashMap<>();
        longitudes = new LinkedHashMap<>();
        umbrales = new LinkedHashMap<>();
        nonulos = new LinkedHashMap<>();
        especiales = new LinkedHashMap<>();
        exps = new LinkedHashMap<>();
        vals = new LinkedHashMap<>();

        for (int i = 0; i < camposNombres.length; i++) {
            String nombre = camposNombres[i];
            CampoHook componente = FormHook.makeGridBagPanel();
            campos.put(camposNombres[i], componente);
            labels.put(nombre, aLabels[i]);
            tipoDatosSQL.put(nombre, aTipoDatos[i]);
            tipoDatos.put(nombre, aClaseDatos[i]);
            tipoComponentes.put(nombre, aTipoComponentes[i]);
            longitudes.put(nombre, aLongitudes[i]);
            umbrales.put(nombre, aUmbrales[i]);
            nonulos.put(nombre, aNonulos[i]);
            especiales.put(nombre, aEspeciales[i]);
            exps.put(nombre, aExps[i]);
            vals.put(nombre, aVals[i]);
        }
    }
    public String[] obtenerExpresiones(){
        return exps.values().toArray(new String[0]);
    }
    public String[] obtenerValidadores(){
        return vals.values().toArray(new String[0]);
    }
    public Integer[] obtenerLongitudes(){
        return longitudes.values().toArray(new Integer[0]);
    }
    public Integer[] obtenerUmbrales(){
        return umbrales.values().toArray(new Integer[0]);
    }
    public Boolean[] obtenerNoNulos(){
        return nonulos.values().toArray(new Boolean[0]);
    }
    public String[] obtenerCamposNombres(){ return campos.keySet().toArray(new String[0]); }
    public String obtenerLabel(String campo){
        return labels.get(campo);
    }
    public Integer getLongitud(String campoNombre){
        return longitudes.get(campoNombre);
    }

    public Integer getUmbral(String campoNombre){
        return umbrales.get(campoNombre);
    }
    /**
     * Establece las dimensiones de todos los campos del formulario
     * @param w tamaño horizontal, si es -1 no se efectuan cambios
     * @param h tamaño vertical, si es -1 no se efectuan cambios
     */
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
    /**
     * Establece las dimensiones de las etiquetas de todos los campos del formulario
     * @param d la dimension
     */
    public void setLabelsSize(Dimension d){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                campoHook.getChild("label").setPreferredSize(d);
            }
        });
    }
    /**
     * Obtiene un campo del formulario
     * @param nombre nombre del campo
     * @return {@link CampoHook} campo del formulario
     */
    public CampoHook getCampo(String nombre){
        return campos.get(nombre);
    }
    /**
     * Reemplaza el input actual del campo por otro
     * @param nombre nombre del campo a modificar
     * @param input nuevo input del campo
     */
    public void setInput(String nombre, CampoHook input){
        getCampo(nombre).removeChild("input");
        getCampo(nombre).appendChild("input", input);
    }

    /**
     * Obtiene el label de un campo del formulario
     * @param campo nombre del campo del cual se desea el label
     * @return {@link CampoHook} del label
     */
    public CampoHook getLabel(String campo){
        return campos.get(campo).getChild("label");
    }
    /**
     * Obtiene el input de un campo del formulario
     * @param campo nombre del campo del cual se desea el input
     * @return {@link CampoHook} del input
     */
    public CampoHook getInput(String campo){
        return campos.get(campo).getChild("input");
    }
    /**
     * Establece el color de texto de las etiquetas de todos los campos del formulario
     * @param c el color
     */
    public void setLabelsColor(Color c){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                campoHook.getChild("label").setForeground(c);
            }
        });
    }

    /**
     * Establece las dimenisones de todos los campos de entrada del formulario
     * @param d dimension
     */
    public void setInputsSize(Dimension d){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                campoHook.getChild("input").setPreferredSize(d);
            }
        });
    }

    /**
     * Establece el tamaño mínimo de los labels del formulario
     * @param d tamaño
     */
    public void setLabelsMinimumSize(Dimension d){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                campoHook.getChild("label").setMinimumSize(d);
            }
        });
    }
    /**
     * Establece el tamaño máximo de los inputs del formulario
     * @param d tamaño
     */
    public void setInputsMinimumSize(Dimension d){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                campoHook.getChild("input").setMinimumSize(d);
            }
        });
    }

    /**
     * Obtiene el boton especificado de este formularoi
     * @param nom nombre del boton
     * @return boton requerido
     */
    public CampoHook getBoton(String nom){
        return botones.get(nom);
    }
    /**
     * Crea los elementos del formulario, labels e inputs
     */
    public void generar(){
        GridBagConstraints gc = makeConstraint(0, -1, GridBagConstraints.NONE);
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                generarComponente(campoHook, s, gc);

            }
        });
    }
    /**
     * Crea los elementos del formulario, labels e inputs especificando constraints
     * @param gc constraints para agregar labels e inputs al campo con layout
     */
    public void generar(GridBagConstraints gc){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                generarComponente(campoHook, s, gc);
            }
        });
    }

    /**
     * Genera el label e input del componente basado en los datos pre-cargados del formulario
     * @param campoHook CampoHook al que se le agregan los componentes
     * @param nombre nombre del CampoHook en el formulario
     * @param gc constraints para agregar sus componentes
     */
    public void generarComponente(CampoHook campoHook, String nombre, GridBagConstraints gc){
        try {
            CampoHook input = new CampoHook(obtenerComponente(tipoComponentes.get(nombre), exps.get(nombre)));
            CampoHook label = new CampoHook(new JLabel(labels.get(nombre)));
            if(input.componente instanceof JCheckBox){
                ((JCheckBox) input.componente).setText(labels.get(nombre));
                input.setForeground(Color.white);
                input.componente.setOpaque(false);
                ((JLabel)label.componente).setText("");
            }
            campoHook.appendChild("label", label, gc);
            campoHook.appendChild("input", input, gc);
        }catch (NullPointerException e){
            System.out.println("El campo no se pudo agregar: " + tipoComponentes.get(nombre));
        }
    }
    public void setActionListenerGlobal(Call c){
        campos.forEach((nombre, campoHook) -> {
            //System.out.println("INPUT LIST "+ nombre +" AAA " +getInput(nombre).id);
            getInput(nombre).addActionListenerGlobal(c);
        });
    }
    /**
     * Extrae todos los datos de los campos de este formulario
     * @return lista de datos
     */
    public ArrayList<Object> extraer(){
        ArrayList<Object> datos = new ArrayList<>();

        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                datos.add(extraerDeCampo(campoHook, s));
            }
        });
        return datos;
    }

    /**
     * Extrae el dato de entrada de un componente del formulario
     * @param campo campo a extraer
     * @param nombre nombre del campo en el formulario
     * @return dato extraido
     */
    public Object extraerDeCampo(CampoHook campo, String nombre){
        JComponent c = campo.getChild("input").componente;
        Object crudo = Extractor.extraerDato(c);

        if(crudo instanceof String) return Extractor.convertir(crudo.toString(), tipoDatos.get(nombre));
        return crudo;
    }

    /**
     * Rellena el formulario con los datos de un modelo, el modelo debe contener los mismos campos que el formulario
     * @param modelo modelo a mostrar
     * @throws IllegalAccessException si los campos del modelo son inaccesibles
     */
    public void colocar(ModeloBD modelo) throws IllegalAccessException {
        Object[] datos = modelo.obtenerDatos();
        String[] noms = ModeloBD.obtenerCamposNombresDe(modelo.getClass().getSimpleName());

        for (int i = 0; i < noms.length; i++) {
            String nom = noms[i];
            Object dato = datos[i];
            colocarDato(campos.get(nom), dato);
        }
    }
    public void vaciar(){
        campos.forEach((nombre, campoHook) -> {
            colocarDato(campoHook, null);
        });
    }
    /**
     * Coloca un dato en el input de un campo
     * @param campo componente a llenar
     * @param dato dato a mostrar
     */
    public static void colocarDato(CampoHook campo, Object dato){
        JComponent c = campo.getChild("input").componente;
        Extractor.colocarDato(c, dato);
    }
    /**
     * Establece la opacidad de todos los campos del formulario
     * @param opaco
     */
    public void setCamposOpaque(boolean opaco){
        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                campoHook.componente.setOpaque(opaco);
            }
        });
    }

    /**
     * Permite controlar la opacidad de ciertos campos del formulario
     * @param opaco estado de opacidad
     * @param campos nombres de los campos a modificar
     */
    public void setCamposOpaque(boolean opaco, String ...campos){
        for (String campo : campos) {
            this.campos.get(campo).getComponente().setOpaque(opaco);
        }
    }

    /**
     * Agrega los campos de este formulario a un componente, especificando constraints
     * @param comp componente a poblar con los campos
     */
    public void attachCamposEn(CampoHook comp){

        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {

                comp.appendChild(s, campoHook);
            }
        });
    }
    /**
     * Agrega los campos de este formulario a un componente, especificando constraints
     * @param comp componente a poblar con los campos
     * @param constraints constraints para agregar campos con layout
     */
    public void attachCamposEn(CampoHook comp, Object constraints){

        campos.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {

                comp.appendChild(s, campoHook, constraints);
            }
        });
    }
    /**
     * Agrega los botones de este formulario a un componente
     * @param comp componente a poblar con los botones
     */
    public void attachBotonesEn(CampoHook comp){
        botones.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                comp.appendChild(s, campoHook);
            }
        });
    }

    /**
     * Agrega los botones de este formulario a un componente, especificando constraints
     * @param comp componente a poblar con los botones
     * @param constraints constraints para agregar botones con layout
     */
    public void attachBotonesEn(CampoHook comp, Object constraints){
        botones.forEach(new BiConsumer<String, CampoHook>() {
            @Override
            public void accept(String s, CampoHook campoHook) {
                comp.appendChild(s, campoHook, constraints);
            }
        });
    }
    /**
     * Agrega las secciones de este formulario a un componente
     * @param comp componente a poblar con las secciones del formulario
     */
    public void attachSeccionesEn(CampoHook comp){
        secciones.forEach(new BiConsumer<String, PanelHook>() {
            @Override
            public void accept(String s, PanelHook panelHook) {
                comp.appendChild(s, panelHook);
            }
        });
    }

    /**
     * Agrega las secciones de este formulario a un componente, especificando constraints
     * @param comp componente a poblar con las secciones del formulario
     * @param constraints constraints para agregar secciones con layout
     */
    public void attachSeccionesEn(CampoHook comp, Object constraints){
        secciones.forEach(new BiConsumer<String, PanelHook>() {
            @Override
            public void accept(String s, PanelHook panelHook) {
                comp.appendChild(s, panelHook, constraints);
            }
        });
    }

    /**
     *Agrega los campos de este formulario a una de sus secciones
     * @param nombre nombre de la seccion del formulario
     */
    public void attachEnSeccion(String nombre){
        attachCamposEn(secciones.get(nombre));
    }

    /**
     * Agrega los campos de este formulario a una de sus secciones, especificando constraints
     * @param nombre nombre de la seccion del formulario
     * @param constraints constraints para agregar los campos con un layout
     */
    public void attachEnSeccion(String nombre, Object constraints){
        attachCamposEn(secciones.get(nombre), constraints);
    }

    /**
     * Permite agregar los botones de este formulario a una de sus secciones
     * @param nombre nombre de la seccion del formulario
     */
    public void attachBotonesEnSeccion(String nombre){
        attachBotonesEn(secciones.get(nombre));
    }

    /**
     * Permite agregar los botones de este formulario a una de sus secciones, especificando constraints
     * @param nombre nombre de la seccion del formulario
     * @param constraints constraints para agregar los botones con un layout
     */
    public void attachBotonesEnSeccion(String nombre, Object constraints){
        attachBotonesEn(secciones.get(nombre), constraints);
    }

    /**
     * Agrega las secciones de este formulario a un panel
     * @param p panel a poblar con las secciones
     */
    public void attachSeccionesA(PanelHook p){
        attachSeccionesEn(p);
    }
    /**
     * Recibe un string con el tipo de componente a crear y devuelve su JComponent equivalente
     * @param tipo tipo de componente que se desea
     * @param regex restricciones para cajas de texto
     * @return componente
     */
    static JComponent obtenerComponente(String tipo, String regex){
        switch (tipo){
            case "textfield": return makeRestrictedTextField(regex);
            case "decimalfield": return new DecimalField();
            case "passfield": return makeRestrictedPassField(regex);
            case "datefield": return new JDatePicker();
            case "combobox": return  new JComboBox<>();
            case "checkbox": return new JCheckBox();
            case "listhook": return new ListHook<Object, Object>();
        }
        System.out.println("Tipo de comp desconocido: " + tipo);
        return null;
    }

    /**
     * Permite asignarle una expresion regular a un campo de texto, para evitar entradas no permitidas
     * @param regex el regex a compilar
     * @return Caja de texto con eventos configurados
     */
    public static JTextField makeRestrictedTextField(String regex){

        JTextField txt = new JTextField();

        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String testear = ""+ txt.getText()+e.getKeyChar();
                if(Extractor.probarExpresion(testear, regex)){
                    super.keyTyped(e);
                }else {

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

    /**
     * Crea un campo de contraseña y lo configura para impedir caracteres según una expresión regular
     * @param regex expresión para filtrar caracteres
     * @return campo configurado
     */
    static JComponent makeRestrictedPassField(String regex){

        JPasswordField txt = new JPasswordField();
        txt.setEchoChar('*');
        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String testear = ""+ Arrays.toString(txt.getPassword()) +e.getKeyChar();
                if(Extractor.probarExpresion(testear, regex)){
                    super.keyTyped(e);
                }else {

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

    /**
     * Crea un nuevo panel con FLowLayout
     * @return Panel creado
     */
    static PanelHook makeFlowPanel(){
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.CENTER);
        return new PanelHook(fl);
    }

    /**
     * Crea un nuevo panel con GridLayout para organizar elementos verticalmente
     * @return Panel creado
     */
    public static PanelHook makeVerticalListPanel(){
        return new PanelHook(new GridLayout(0, 1));
    }
    /**
     * Crea un nuevo panel con BoxLayout para organizar elementos verticalmente
     * @return Panel creado
     */
    public static PanelHook makeVerticalListPanel(int rows){
        PanelHook p = new PanelHook();
        p.setLayout(new BoxLayout(p.getComponente(), BoxLayout.PAGE_AXIS));
        return p;
    }
    /**
     * Crea un nuevo panel con GridLayout para organizar elementos horizontalmente
     * @return Panel creado
     */
    public static PanelHook makeHorizontalListPanel(){
        return new PanelHook(new GridLayout(1, 0));
    }
    /**
     * Crea un nuevo panel con GridLayout para organizar elementos horizontalmente con varias columnas
     * @param columns numero de columnas
     * @return Panel creado
     */
    public static PanelHook makeHorizontalListPanel(int columns){
        return new PanelHook(new GridLayout(1, columns));
    }

    /**
     * Crea un nuevo panel con GridbagLayout
     * @return Panel creado
     */
    public static PanelHook makeGridBagPanel(){
        PanelHook p = new PanelHook(new GridBagLayout());
        return p;
    }

    /**
     * Crea un registro con GridbagLayout
     * @return registro con layout
     */
    public static Registro makeGridBagRegistro(){
        return new Registro(new GridBagLayout());
    }

    /**
     * Agrega una nueva seccion al formulario
     * @param Id id de la seccion
     * @param p seccion a agregar
     */
    public void agregarSeccion(String Id, PanelHook p){
        secciones.put(Id, p);
    }
    /**
     * Crea un nuevo panel con gridbag layout con informacion dada del registro, incluye botones de eliminar y editar
     * @param Id id del registro
     * @param campoHooks información a mostrar en el registro,
     * @param nombreDatos id de cada campo que muestra uno de los datos dados
     * @param distribucion cuantas celdas se le asignan a cada registro
     * @param celdas número total de celdas en el registro
     * @param tamanio tamaño vertical del registro
     * @return panel configurado con información
     */
    public static PanelHook crearRegistroGridBag(String Id, CampoHook[] campoHooks, String[] nombreDatos, int[] distribucion, int celdas, int tamanio){
        PanelHook panel = makeGridBagPanel();
        panel.setPreferredSize(new Dimension(0, tamanio));
        panel.setMaximumSize(new Dimension(32767, tamanio));
        int celdasTotal = celdas+2;//incluir botones

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        double freeWeight = 1;
        for (int i = 0; i < campoHooks.length; i++) {
            int ocupa = distribucion[i];
            //convertir las celdas que ocupa el elemento a peso del gridbag
            double weight = obtenerDatoWeight(ocupa, celdasTotal);
            freeWeight -= weight;
            constraints.weightx = weight;
            panel.addElementConstraint(nombreDatos[i], campoHooks[i], constraints);

        }
        autoAgregarPanelAdapter(panel);
        //queda solo el weight restante pa los 2 botones
        constraints.weightx = freeWeight/2;
        constraints.fill = GridBagConstraints.NONE;
        panel.addElementConstraint("btnEditar", new JButton("Editar"), constraints);
        panel.addElementConstraint("btnEliminar", new JButton("Eliminar"), constraints);
        return panel;
    }

    /**
     * Obtiene el espacio ocupado por la distribucion del componente respecto a las celdas totales del registro
     * @param distribucion cantidad de celdas que ocupa el componente
     * @param celdasTotal celdas totales del registro
     * @return peso relativo del componente
     */
    public static double obtenerDatoWeight(int distribucion, int celdasTotal){
        return (double) distribucion /celdasTotal;

    }

    /**
     * Agrega eventos de cursor basicos al panel, como un cambio de color ligero al pasarle el cursor encima
     * @param p panel a configurar
     */
    public static void autoAgregarPanelAdapter(PanelHook p){
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
    }
    /**
     * Crea un nuevo registro con información dada del registro, incluye botones de eliminar y editar, así como
     * acción de clic configurable y un indicador visual de su selección. No incluye modelo asociado
     * @param datos información a mostrar en el registro,
     * @param nombreDatos id de cada campo que muestra uno de los datos dados
     * @param distribucion cuantas celdas se le asignan a cada registro
     * @param celdas número total de celdas en el registro
     * @param tamanio tamaño vertical del registro
     * @return Registro configurado
     */
    public static Registro crearRegistroGridBagR(CampoHook[] datos, String[] nombreDatos, int[] distribucion, int celdas, int tamanio){
        Registro registro = makeGridBagRegistro();
        registro.setPreferredSize(new Dimension(0, tamanio));
        registro.setMaximumSize(new Dimension(32767, tamanio));
        int celdasTotal = celdas+2;

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        double freeWeight = 1;
        for (int i = 0; i < datos.length; i++) {
            int ocupa = distribucion[i];
            double weight = obtenerDatoWeight(ocupa, celdasTotal);
            freeWeight -= weight;
            constraints.weightx = weight;
            registro.addElementConstraint(nombreDatos[i], datos[i], constraints);

        }
        agregarRegistroAdapter(registro);
        registro.componente.setBackground(registro.colorSinEnfoque);
        constraints.weightx = freeWeight/2;
        constraints.fill = GridBagConstraints.NONE;

        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        registro.addElementConstraint("btnEditar", btnEditar, constraints);
        registro.addElementConstraint("btnEliminar", btnEliminar, constraints);

        registro.btnEliminar = btnEliminar;
        registro.btnEditar = btnEditar;
        return registro;
    }

    /**
     * Habilita las reacciones del registro al cursor
     * @param registro registro a configurar
     */
    public static void agregarRegistroAdapter(Registro registro){
        registro.componente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(registro.clicAccion != null) registro.clicAccion.run(e);
            }
            //logica de cambio de color
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if(registro.mouseEnter != null) registro.mouseEnter.run(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(registro.mouseExit != null) registro.mouseExit.run(e);
            }
        });
    }

    /**
     * Envuelve los componentes en CampoHooks
     * @param components componentes a envolver
     * @return arreglo de CampoHooks con sus respectivos componentes
     */
    public static CampoHook[] crearHooks(JComponent[] components){
        CampoHook[] hooks = new CampoHook[components.length];
        for (int i = 0; i < components.length; i++) {
            hooks[i] = new CampoHook(components[i]);
        }
        return hooks;
    }
    /**
     * Crea un nuevo registro con los componentes dados, incluye un modelo asociado, botones de eliminar y editar, así como
     * acción de clic configurable y un indicador visual de su selección.
     * @param components componentes a mostrar en el registro,
     * @param nombreDatos id de cada campo que muestra uno de los components dados
     * @param distribucion cuantas celdas se le asignan a cada registro
     * @param celdas número total de celdas en el registro
     * @param tamanio tamaño vertical del registro
     * @param asociado modelo a asociar al registro
     * @return Registro configurado
     */
    public static Registro crearRegistroGridBag(JComponent[] components, String[] nombreDatos, int[] distribucion, int celdas, int tamanio, ModeloBD asociado){
        Registro reg = crearRegistroGridBagR(crearHooks(components), nombreDatos, distribucion, celdas, tamanio);
        reg.asociado = asociado;
        return reg;
    }
    /**
     * Crea un panel vertical con filas específicas para enlistar registros
     * @return panel vertical
     */
    public static ScrollHook crearScroll(int rows) {
        PanelHook p = makeVerticalListPanel(rows);
        return new ScrollHook(p);
    }
    /**
     * Crea un GridbagConstraint para componentes verticales
     * @return constraint
     */
    public static GridBagConstraints makeVerticalConstraint(int gridx){
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gridx;
        return gc;
    }
    /**
     * Crea un GridbagConstraint con posicion de grid y forma de llenado
     * @return constraint
     */
    public static GridBagConstraints makeConstraint(int gridx, int gridy, int fill){
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gridx;
        gc.gridy = gridy;
        gc.fill = fill;
        return gc;
    }
    /**
     * Crea un GridbagConstraint con posicion de grid, peso horizontal y vertical y forma de llenado
     * @return constraint
     */
    public static GridBagConstraints makeConstraint(int gridx, int gridy,float weightx, float weighty, int fill){
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gridx;
        gc.gridy = gridy;
        gc.weightx = weightx;
        gc.weighty = weighty;
        gc.fill = fill;
        return gc;
    }
    /**
     * Agrega los campos del formulario con un GridbagLayout, especificando si es vertical o no
     */
    public void attachCamposGridbag(int vertical){
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 1-vertical;
        attachEnSeccion("campos", gc);
    }
    /**
     * Crea un panel con la imagen especificada de la carpeta de assets
     * @return panel con imagen
     */
    public static ImageHook crearImagen(String nombre){

        ImageHook i = new ImageHook("/assets/"+nombre+".png");
        return i;
    }

    /**
     * Crea un formulario en base a los datos del modelo
     * @param modelo nombre del modelo
     * @return formulario sin generar
     * @throws InvocationTargetException si ocurre un error al obtener los datos del modelo durante la creación del formulario
     * @throws NoSuchMethodException Si durante la creación del formulario no se encuentra un método para obtener datos del modelo
     * @throws IllegalAccessException Si los datos del modelo son inaccesibles al crear el formulario
     */
    public static FormHook crearDeModelo(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new FormHook(
                ModeloBD.obtenerCampoNombresDe(ModeloBD.getModelo(modelo)),
                ModeloBD.obtenerLabelsDe(modelo),
                ModeloBD.obtenerCampoTiposSQLDe(modelo),
                ModeloBD.obtenerCampoTiposDe(modelo),
                ModeloBD.obtenerCamposComponentesDe(modelo),
                ModeloBD.obtenerLongitudesDe(modelo),
                ModeloBD.obtenerUmbralesDe(modelo),
                ModeloBD.obtenerNoNulosDe(modelo),
                ModeloBD.obtenerEspecialesDe(modelo),
                ModeloBD.obtenerExpresionesDe(modelo),
                ModeloBD.obtenerValidadoresDe(modelo)
        );
    }
    /**
     * Crea un panel lateral con información de usuario y botones de accion
     * @return panel de usuario
     */
    public static PanelHook makeUserPanel(Userio usr){
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
        String nombre = "JEUSER";
        if(usr != null) nombre = usr.getNombre();
        CampoHook nom = new CampoHook(new JLabel(nombre))
                .setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15))
                .setForeground(Color.white);

        i.componente = FormHook.crearImagen(Userio.class.getSimpleName());
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

        PanelHook btnUsuarios = FormHook.makeSidebarBoton("Administrar Usuarios", sidebar.componente.getBackground(), Color.CYAN, Color.WHITE);
        btnUsuarios.setPreferredSize(new Dimension(0, 50));
        panelBtns.appendChild("btnUsuarios", btnUsuarios, gp);

        return sidebar;
    }
    /**
     * Crea una interfaz de formulario para el modelo especificado
     * @return Interfaz de formulario
     */
    public static PanelHook crearFormulario(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        FormHook f = crearDeModelo(modelo);

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
        f.agregarSeccion("foot", foot);
        ///FORMULARIO
        GridBagConstraints gf = makeConstraint(0, -1, 1, 0, GridBagConstraints.HORIZONTAL);
        GridBagConstraints gc = makeConstraint(0, -1, 0, 0, GridBagConstraints.NONE);
        gc.insets.top = 4;
        gc.insets.bottom = 4;
        f.generar(gc);
        f.attachBotonesEnSeccion("foot");
        f.setLabelsSize(new Dimension(300, 20));
        f.setInputsSize(new Dimension(300, 20));
        f.setLabelsMinimumSize(new Dimension(300, 15));
        f.setInputsMinimumSize(new Dimension(300, 20));
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

    /**
     * Crea un boton para formulario con los datos especificados
     * @param txt texto del boton
     * @param bkg color de fondo
     * @param frg color del texto
     * @return boton personalizado
     */
    public static CampoHook makeFormBoton(String txt, Color bkg, Color frg){
        CampoHook c = new CampoHook(new JButton(txt));
        c.setBackground(bkg);
        c.setForeground(frg);
        c.componente.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        return c;
    }

    /**
     * Crea una pantalla de login con el modelo de Usuario
     * @return pantalla personalizada
     */
    public static Login crearLogin() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        FormHook form = FormHook.crearDeModelo(Userio.class.getSimpleName());

        PanelHook vertical = FormHook.makeGridBagPanel().setBackground(Color.WHITE);
        PanelHook horizontal = FormHook.makeFlowPanel().setBackground(Color.WHITE);
        PanelHook header = FormHook.makeGridBagPanel()
                .setPreferredSize(new Dimension(800, 100))
                .setForeground(Color.white)
                .setBackground(new Color(48, 48, 255));

        CampoHook labelPrincipal = new CampoHook(new JLabel("Autos Amistosos"))
                .setFont(new Font(Font.SANS_SERIF, Font.BOLD, 45))
                .setAlignment(SwingConstants.CENTER, SwingConstants.CENTER)
                .setForeground(Color.white);
        CampoHook loginIndicacion = new CampoHook(new JLabel("Inicie sesion"))
                .setForeground(Color.white);;
        header.appendChild("nombre", labelPrincipal, makeVerticalConstraint(0));
        header.appendChild("sesion", loginIndicacion, makeVerticalConstraint(0));

        form.agregarSeccion("header", header);
        form.agregarSeccion("campos", vertical);
        form.agregarSeccion("botones", horizontal);
        form.attachCamposGridbag(1);
        form.attachBotonesEnSeccion("botones");
        form.generar();
        form.setCamposSizes(
                Userio.obtenerCamposNombres(),
                new Dimension[]{
                        new Dimension(620, 150),
                        new Dimension(620, 150),
                        new Dimension(200, 75),
                        new Dimension(200, 75),
                        new Dimension(200, 75),
                }
        );
        form.whiteList("nombre", "password");
        form.setCamposOpaque(false);

        form.getLabel("nombre").setPreferredSize(new Dimension(600, 70))
                .setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 22));
        form.getLabel("password").setPreferredSize(new Dimension(600, 70))
                .setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 22));
        form.getInput("nombre").setPreferredSize(new Dimension(600, 60));
        form.getInput("password").setPreferredSize(new Dimension(600, 60));

        PanelHook panelCentral = FormHook.makeVerticalListPanel(0);
        form.attachSeccionesEn(panelCentral);
        Login panelLogin = new Login();
        panelLogin.id = "id";

        panelLogin.appendChild("izquierda", new PanelHook().setBackground(Color.LIGHT_GRAY),
                makeConstraint(0, 0, .4f, 1f, GridBagConstraints.BOTH));

        panelLogin.appendChild("central", panelCentral, makeConstraint(1, 0, .2f, .7f, GridBagConstraints.NONE));

        panelLogin.appendChild("derecha", new PanelHook().setBackground(Color.LIGHT_GRAY),
                makeConstraint(2, 0, .4f, 1f, GridBagConstraints.BOTH));

        CampoHook btnLogear = new CampoHook(new JButton("Iniciar sesion"))
                .setBackground(new Color(10, 150, 10))
                .setForeground(Color.white)
                .setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15))
                .setPreferredSize(new Dimension(300, 50));

        horizontal.setPreferredSize(new Dimension(620, 100));
        horizontal.appendChild("btnLogear", btnLogear);
        form.botones.put("btnLogear", btnLogear);

        panelLogin.form = form;
        return panelLogin;
    }

    /**
     * Crea una pantalla ABCC a partir de la tabla proporcionada, incluyendo tabla para visualizar registros
     * @param modelo modelo para crear la pantalla
     * @param modelos registros a plasmar en la tabla
     * @return pantalla personalizada
     * @throws InvocationTargetException si ocurrió un error al obtener los datos del modelo durante la creación del formulario
     * @throws NoSuchMethodException Si durante la creación del formulario no se encuentra un método para obtener datos del modelo
     * @throws IllegalAccessException Si los datos del modelo son inaccesibles al crear el formulario
     */
    public static PanelHook crearABCC(String modelo, ArrayList<ModeloBD> modelos) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

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
        gc.weighty = .075;
        topbar.setMaximumSize(new Dimension(1000, 180));
        topbar.setMinimumSize(new Dimension(0, 180));
        main.appendChild("topbar", topbar, gc);
        //gc del panel de la tabla
        gc.weighty = 1-gc.weighty;
        main.appendChild("tableHolder", tableHolder, gc);
        //gc de la tabla

        gc = FormHook.makeConstraint(-1, -1, 1.0f, 1.0f, GridBagConstraints.BOTH);
        tableHolder.appendChild("tabla", tabla, gc);

        //rellenado de tabla
        FormHook.rellenarTabla(tabla, modelos);

        return holder;
    }

    /**
     * Extrae los registros de la tabla especificada
     * @param tabla tabla a procesar
     * @return lista de los registros cargados en la tabla
     */
    public static ArrayList<Registro> obtenerRegistros(ScrollHook tabla){
        ArrayList<Registro> registros = new ArrayList<>();
        tabla.getView().children.forEach((id, registro) -> {
            registros.add((Registro) registro);
        });
        return registros;
    }

    /**
     * Crea un boton con el estilo del panel lateral de usuario
     * @param txt texto del botón
     * @param bkg color de fondo del botón
     * @param dtl color de la línea de color del botón
     * @param txc color del texto del boton
     * @param adp MouseAdapter para las acciones del botón
     * @return botón configurado
     */
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
    /**
     * Crea un boton con el estilo del panel lateral de usuario
     * @param txt texto del botón
     * @param bkg color de fondo del botón
     * @param dtl color de la línea de color del botón
     * @param txc color del texto del boton
     * @return botón configurado
     */
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

    /**
     * Elimina todos los registros de la tabla dada
     * @param tabla tabla a limpiar
     */
    public static void limpiarTabla(ScrollHook tabla){
        tabla.getView().removeChildren();
        tabla.getComponente().revalidate();
        tabla.getView().componente.revalidate();
        tabla.getView().componente.repaint();

    }

    /**
     * Rellena la tabla dada con la lista de modelos especificada
     * @param tabla tabla a rellenar
     * @param modelos lista de modelos a mostrar
     */
    public static void rellenarTabla(ScrollHook tabla, ArrayList<ModeloBD> modelos){
        if(modelos != null){
            final int[] i = {0};
            modelos.forEach(modeloBD -> {

                LinkedHashMap<String, Object> datos = modeloBD.getInfoImportante();
                if(datos == null) System.err.println("FORMHOOK los datos importantes del modelo '"+modeloBD.getClass().getSimpleName()+"' son nulos");
                JComponent[] display = new JComponent[datos.size()];
                String[] ids = new String[datos.size()];
                int[] tamaniosAuto = new int[datos.size()];
                int tamanioTotal = datos.size();
                for (int i1 = 0; i1 < display.length; i1++) {

                    Object datoCampo = datos.values().stream().toList().get(i1);
                    String dato = "sin dato";
                    if(datoCampo != null) dato = datoCampo.toString();

                    display[i1] = new JLabel(dato);
                    if(datoCampo == null) display[i1].setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
                    ids[i1] = datos.keySet().stream().toList().get(i1);
                    tamaniosAuto[i1] = 1;
                }

                PanelHook reg = FormHook.crearRegistroGridBag(display, ids, tamaniosAuto, tamanioTotal, 100, modeloBD);
                tabla.getView().appendChild(""+i[0], reg);
                i[0]++;
            });
        }
        tabla.getComponente().revalidate();
        tabla.getView().componente.revalidate();
        tabla.getView().componente.repaint();
    }
    /**
     * Elimina los campos cuyos nombres no coincidan con los nombres dados
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

    /**
     * Establece las dimensiones de los campos especificados
     * @param campoNombres nombre de cada campo a redimensionar
     * @param sizes nueva dimension del campo
     */
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
