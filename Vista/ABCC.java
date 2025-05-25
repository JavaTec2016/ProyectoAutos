package Vista;

import ErrorHandlin.Call;
import ErrorHandlin.ErrorHandler;
import ErrorHandlin.ErrorMessageList;
import ErrorHandlin.Validador;
import FormTools.*;
import Modelo.ModeloBD;
import conexion.ConexionBD;
import controlador.DAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

public class ABCC extends JPanel{
    ArrayList<String> selecNombres = null;
    ArrayList<String> filtroNombres = null;
    ArrayList<Object> filtroValores = null;

    ModeloBD modeloSeleccionado = null;
    Registro registroSeleccionado = null;

    PanelHook ventana;
    PanelHook panelOpciones;
    PanelHook formulario;

    CampoHook btnAgregar;
    CampoHook btnLimpiar;
    CampoHook btnBack;
    CampoHook btnCommit;
    CampoHook btnRollback;
    CampoHook checkConsulta;

    CampoHook titulo;
    ScrollHook scroll;
    String tabla;

    public ErrorMessageList errores;

    public ABCC(String tabla) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        this.tabla = tabla;
        ArrayList<ModeloBD> modelos = realizarConsulta(tabla, null, null, null);
        ventana = FormHook.crearABCC(tabla, modelos);
        formulario = (PanelHook) ventana.getChild("sidebar");
        errores = new ErrorMessageList(formulario.form);
        scroll = (ScrollHook)ventana.childPath("main/tableHolder/tabla");
        panelOpciones = (PanelHook) ventana.childPath("main/topbar");
        titulo = formulario.childPath("header/title");
        btnAgregar = ventana.childPath("sidebar/foot/btnAgregar");

        configurarAgregar();
        registrarMensajesValidacionGenericos();
        registrarHandlersValidacion();
        registrarHandlersSQL();

        configurarPanelOpciones();
        setLimpiarAccion();
        setCommitAccion();
        setRollbackAccion();

        activarAutoFiltrado();

        actualizarTablaABCCThread(selecNombres, filtroNombres, filtroValores);
        add(ventana.componente);
    }
    /**
     * Obtiene el {@link ListHook} de un campo del formulario
     * @param campoNombre nombre del campo
     */
    public ListHook<?,?> getListHook(String campoNombre){
        return (ListHook<Object, Object>)(formulario.form.getInput(campoNombre).componente);
    }

    /**
     * Itera todos los registros de la tabla, realizando una acción para cada uno
     * @param consumer Accion a realizar para cada registro
     */
    public void porCadaRegistro(Consumer<Registro> consumer){
        FormHook.obtenerRegistros(scroll).forEach(consumer);
    }

    /**
     * Establece la visibilidad del botón "Agregar"
     * @param estado estado de visibilidad
     */
    public void setVisibleAgregar(boolean estado){
        btnAgregar.componente.setVisible(estado);
    }

    /**
     * Activa o desactiva la visibilidad del botón "Editar" de cada registro de la tabla
     * @param estado estado de visibilidad para todos los registros
     */
    public void setRegistrosEditables(boolean estado){
        porCadaRegistro(registro -> {
            registro.btnEditar.setVisible(estado);
        });
    }
    /**
     * Activa o desactiva la visibilidad del botón "Eliminar" de cada registro de la tabla
     * @param estado estado de visibilidad para todos los registros
     */
    public void setRegistrosEliminables(boolean estado){
        porCadaRegistro(registro -> {
            registro.btnEliminar.setVisible(estado);
        });
    }
    /**
     * Obtiene el {@link JComboBox} de un campo del formulario
     * @param campoNombre nombre del campo
     */
    public JComboBox<?> getComboBox(String campoNombre){
        return (JComboBox<Object>)(formulario.form.getInput(campoNombre).componente);
    }
    /**
     * Crea el botón de regreso de la pantalla ABCC
     */
    public void crearBackButton(){
        CampoHook btn = FormHook.makeFormBoton("REGRESAR", Color.BLUE, Color.white);
        btn.setPreferredSize(new Dimension(200, 50));
        btnBack = btn;
    }
    /**
     * Crea el botón de commit de la base de datos
     */
    public void crearCommitButton(){
        CampoHook btn = FormHook.makeFormBoton("GUARDAR CAMBIOS", Color.WHITE, Color.BLUE);
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnCommit = btn;
    }
    /**
     * Crea el botón de rollback de la base de datos
     */
    public void crearRollbackButton(){
        CampoHook btn = FormHook.makeFormBoton("DESCARTAR CAMBIOS", Color.RED, Color.WHITE);
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnRollback = btn;
    }
    /**
     * Crea la opción de filtrado automático de resultados
     */
    public void crearConsultaCheck(){
        CampoHook check = new CampoHook(new JCheckBox("filtrado automático"));
        check.setPreferredSize(new Dimension(250, 50));
        check.setFont(new Font("Segoe UI", Font.BOLD, 16));
        checkConsulta = check;
    }

    /**
     * Crea el botón de limpieza del formulario
     */
    public void crearLimpiarButton(){
        CampoHook btn = FormHook.makeFormBoton("limpiar formulario", Color.WHITE, Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(200, 50));
        btnLimpiar = btn;
    }

    /**
     * Configura los controles de la barra superior de la pantalla ABCC
     * Incluye botones de regreso al panel de control, limpiar formulario, filtrar registros automáticamente, commit y rollback
     */
    private void configurarPanelOpciones(){
        PanelHook panelRegresar = FormHook.makeGridBagPanel().setBackground(Color.YELLOW).setOpaque(false);
        PanelHook panelConsulta = FormHook.makeGridBagPanel().setBackground(Color.BLUE).setOpaque(false);
        PanelHook panelCambios = FormHook.makeGridBagPanel().setBackground(Color.DARK_GRAY).setOpaque(false);

        GridBagConstraints constraints = FormHook.makeConstraint(-1, 0, 0.15f, 1, GridBagConstraints.BOTH);
        panelOpciones.appendChild("panelBack", panelRegresar, constraints);

        constraints.weightx = 0.6f;
        panelOpciones.appendChild("panelConsulta", panelConsulta, constraints);

        constraints.weightx = 0.15f;
        panelOpciones.appendChild("panelCambios", panelCambios, constraints);

        crearBackButton();
        crearCommitButton();
        crearRollbackButton();
        crearConsultaCheck();
        crearLimpiarButton();

        GridBagConstraints botonesConstraint = FormHook.makeConstraint(0, -1, 0, 0, GridBagConstraints.NONE);
        botonesConstraint.insets = new Insets(20, 0, 20, 0);
        panelRegresar.appendChild("btnBack", btnBack, botonesConstraint);
        panelRegresar.appendChild("btnLimpiar", btnLimpiar, botonesConstraint);

        panelConsulta.appendChild("checkConsulta", checkConsulta, botonesConstraint);

        panelCambios.appendChild("btnCommit", btnCommit, botonesConstraint);
        panelCambios.appendChild("btnRollback", btnRollback, botonesConstraint);
    }

    /**
     * Habilita la acción de limpiado del formulario
     */
    private void setLimpiarAccion(){
        btnLimpiar.addActionListener(e->{
                formulario.form.vaciar();
        });
    }
    /**
     * Habilita la acción de commit a la base de datos
     */
    private void setCommitAccion(){
        btnCommit.addActionListener(e->{
            if(panelSiNo("Guardar cambios en la base de datos de forma permanente?", "Revertir cambios") != JOptionPane.YES_OPTION) return;
            try {
                ConexionBD.getConector().commit();
                actualizarTablaABCCThread(selecNombres, filtroNombres, filtroValores);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    /**
     * Habilita la acción de rollback de la base de datos
     */
    private void setRollbackAccion(){
        btnRollback.addActionListener(e->{
            if(panelSiNo("Deshacer cambios de la base de datos?", "Revertir cambios") != JOptionPane.YES_OPTION) return;
            try {
                ConexionBD.getConector().rollback();
                actualizarTablaABCCThread(selecNombres, filtroNombres, filtroValores);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    /**
     * Establece una acción de clic del botón de regreso
     */
    public void setBackAccion(ActionListener listener){
        btnBack.addActionListener(listener);
    }
    /**
     * Configura la acción del botón agregar
     */
    public void configurarAgregar(){
        btnAgregar.addActionListener(e -> {
            FormHook f = formulario.form;
            ArrayList<Object> datos = f.extraer();
            HashMap<Integer, Integer> errs = obtenerErrores(datos, f.obtenerValidadores(), f.obtenerLongitudes(), f.obtenerNoNulos(), f.obtenerUmbrales());
            if(!errs.isEmpty()){
                int idx = errs.keySet().toArray(new Integer[0])[0];
                int err = errs.values().toArray(new Integer[0])[0];
                autoHandlearValidacion(tabla, err, idx);
            }else{
                Object[] args = datos.toArray();
                ModeloBD modelo = ModeloBD.instanciar(tabla, args);

                agregarSeleccion(modelo);
            }
        });
    }

    /**
     * Retorna el estado de selección de la opción de auto filtrado
     * @return
     */
    public boolean getAutoFiltrado(){
        return ((JCheckBox)checkConsulta.componente).isSelected();
    }

    /**
     * Agrega listeners a todos los campos del formulario, al detectar un cambio obtiene la información del formulario
     */
    private void activarAutoFiltrado(){
        formulario.form.setActionListenerGlobal(data -> {

            if(getAutoFiltrado()){

                ArrayList<Object> datos = formulario.form.extraer();
                String[] nombres = formulario.form.obtenerCamposNombres();

                filtroNombres = new ArrayList<>(List.of(nombres));
                filtroValores = datos;

                //System.out.println(filtroValores);
                actualizarTablaABCCThreadLike(selecNombres, filtroNombres, filtroValores);

            }
        });
    }
    /**
     * Agrega un nuevo modelo si no existe una selección, o actualiza el modelo seleccionado
     * @param modelo nuevo modelo a ingresar a la BD
     */
    public void agregarSeleccion(ModeloBD modelo){
        if(registroSeleccionado == null){
            agregar(modelo);
        }else{
            try {
                modificar(modelo, ModeloBD.extraerPrimarias(modeloSeleccionado));
                cambiarSeleccion(null);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        actualizarTablaABCCThread(selecNombres, filtroNombres, filtroValores);
    }
    /**
     * Llama al handler que corresponde al código de error, le da el nombre del campo con error
     * @param modelo nombre del modelo que se está validando
     * @param codigo código de error
     * @param idx índice del campo con error
     */
    public void autoHandlearValidacion(String modelo, int codigo, int idx){
        String[] noms = ModeloBD.obtenerCamposNombresDe(modelo);
        handlearErrorCampo(codigo, noms[idx]);
    }
    public static void panelError(String msg, String titulo){
        JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.ERROR_MESSAGE);
    }
    public static int panelSiNo(String mensaje, String titulo){
        return JOptionPane.showOptionDialog(null, mensaje, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"Si", "No"}, "No");
    }
    public static int panelSiNoCancelar(String txt, String titulo){
        return JOptionPane.showOptionDialog(null, txt, titulo, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"Si", "No", "Cancelar"}, "Cancelar");
    }
    /**
     * Asocia diferentes mensajes genéricos validación fallida a cada campo del formulario para mostrarlos en pantalla después
     */
    public void registrarMensajesValidacionGenericos(){
        FormHook form = formulario.form;
        System.out.println(errores.lista);
        //registrar errores comunes para todos los campos (no SQL)
        errores.lista.forEach((campoNombre, listaMensajes) -> {

            errores.addMessage(campoNombre, Validador.NULL, "El campo '"+form.obtenerLabel(campoNombre)+"' no debe ser nulo.");

            errores.addMessage(campoNombre, Validador.TOO_SHORT,
                    "La longitud del campo '"+form.obtenerLabel(campoNombre)+"' no debe ser menor a "+form.getUmbral(campoNombre)+" caracteres");

            errores.addMessage(campoNombre, Validador.TOO_LONG,
                    "La longitud del campo '"+form.obtenerLabel(campoNombre)+"' no debe exceder "+form.getLongitud(campoNombre)+" caracteres");

            errores.addMessage(campoNombre, Validador.REGEX_FAIL,
                    "El campo '"+form.obtenerLabel(campoNombre)+"' no es válido, verifique los caracteres ingresados");
        });
    }
    public void registrarHandlersValidacion(){
        ErrorHandler.registrarHandler(Validador.NULL, e->{
            panelError(e[0].toString(), "Campo nulo");
        });
        ErrorHandler.registrarHandler(Validador.TOO_LONG, e->{
            panelError(e[0].toString(), "Longitud excedida");
        });
        ErrorHandler.registrarHandler(Validador.TOO_SHORT, e->{
            panelError(e[0].toString(), "Longitud mínima");
        });
        ErrorHandler.registrarHandler(Validador.REGEX_FAIL, e->{
            panelError(e[0].toString(), "Caracteres o formato no permitidos");
        });
    }
    public void registrarHandlersSQL(){
        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya existe un registro con el mismo identificador", "Error de datos");
        });
    }
    /**
     * Asocia un mensaje de error y el código que lo causa a un campo del formulario
     * @param campo nombre del campo del formulario
     * @param codigo código de error
     * @param mensaje mensaje de error a mostrar
     */
    public void agregarMensaje(String campo, Integer codigo, String mensaje){
        errores.addMessage(campo, codigo, mensaje);
    }

    /**
     * Asocia los campos primarios del formulario con errores de registros duplicados
     * @param modelo modelo sobre el cual se elaboran y asocian los mensajes de error
     * @throws InvocationTargetException si falla la obtención de índices primarios del modelo
     * @throws NoSuchMethodException si el modelo no cuenta con método para obtener índices primarios
     * @throws IllegalAccessException si el método del modelo no es accesible
     */
    public void agregarMensajeDuplicado(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String[] primariaLabels = ModeloBD.dePrimarias(modelo, ModeloBD.obtenerLabelsDe(modelo));
        String[] primariasNombres = ModeloBD.obtenerNombresPrimariasDe(modelo);
        String msgCampos = ErrorMessageList.formatearMensajes(primariaLabels);

        for (String nombre : primariasNombres) {
            errores.addMessage(nombre, ErrorHandler.SQL_DUPLICATE_ENTRY, "Alguno de los siguientes campos ya se encuentra registrado: "+msgCampos);
        }
    }

    /**
     * Ejecuta el handler del error especificado, le pasa el respectivo mensaje de error de la lista de mensajes
     * @param codigo codigo de error a manejar
     * @param campoError nombre del campo donde sucede el error
     */
    public void handlearErrorCampo(int codigo, String campoError){
        String error = errores.getMessage(campoError, codigo);
        ErrorHandler.ejecutarHandler(codigo, error, campoError);
    }
    public void registrarHandlerForanea(String titulo){
        ErrorHandler.registrarHandler(ErrorHandler.SQL_UNKNOWN_FOREIGN, data->{
            panelError(errores.getMessage(data[0].toString(), ErrorHandler.SQL_UNKNOWN_FOREIGN), titulo);
        });
    }
    public void handlearError(int codigo, Object... args){
        ErrorHandler.ejecutarHandler(codigo, args);
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
    public ArrayList<ModeloBD> realizarConsultaLike(String tabla, String[] selecNombres, String[] filtroNombres, Object[] filtroValores) {

        try {
            return DAO.d.obtenerRegistrosLike(tabla, selecNombres, filtroNombres, filtroValores);
        } catch (SQLException e) {
            handlearError(e.getErrorCode(), tabla);
            System.out.println("Error de consulta: " + e.getErrorCode());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Agrega un nuevo registro a la BD
     * @param modelo registro a agregar
     */
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
            e.printStackTrace();
        }
    }
    /**
     * Realiza una consulta a la tabla con el nombre especificado, agregando cada registro a un {@link JComboBox} del formulario.
     * Agrega cada registro utilizando su campo primario.
     * @param campo nombre del {@link JComboBox}
     * @param name nombre de la tabla a consultar
     * @param nulo Valor de la opción nula
     */
    protected void comboBoxOpciones(String campo, String name, Object nulo){
        JComboBox<Object> modelos = (JComboBox<Object>) getComboBox(campo);
        ArrayList<ModeloBD> registros = realizarConsulta(name, null, null, null);
        modelos.addItem(nulo);
        registros.forEach(modelo -> {
            try {
                Object[] datos = modelo.obtenerDatos();
                datos = ModeloBD.dePrimarias(name, datos);
                modelos.addItem(datos[0]);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Realiza una consulta a la tabla con el nombre especificado, agregando registro uno a un {@link ListHook} del formulario.
     * Agrega cada registro utilizando su campo primario como llave y su {@link ModeloBD#getDisplay()} como texto a mostrar.
     * @param campo nombre del {@link ListHook}
     * @param name nombre de la tabla a consultar
     * @param nulo texto de la opción con valor nulo
     */
    protected void listHookOciones( String campo, String name, String nulo){
        ListHook<Integer, String> lista = (ListHook<Integer, String>) getListHook(campo);
        ArrayList<ModeloBD> registros = realizarConsulta(name, null, null, null);
        lista.addItem(null, nulo);
        registros.forEach(modelo ->{
            try {
                Object[] datos = modelo.obtenerDatos();
                datos = ModeloBD.dePrimarias(name, datos);
                System.out.println(Arrays.toString(datos) +","+ modelo.getDisplay());
                lista.addItem((Integer) datos[0], modelo.getDisplay());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }
    /**
     * Elimina un registro de la BD
     * @param r registro a eliminar
     */
    public void eliminar(ModeloBD r){
        try {
            DAO.d.eliminar(r);
            JOptionPane.showMessageDialog(null, "Registro eliminado", "Eliminacion", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            handlearError(e.getErrorCode(), r.getClass().getSimpleName());
        }
    }
    /**
     * Modifica un registro de la BD
     * @param modelo registro con los nuevos datos
     * @param primariasOG datos para identificar al registro a modificar
     */
    public void modificar(ModeloBD modelo, Object[] primariasOG){
        try {
            DAO.d.modificar(modelo, primariasOG);
            JOptionPane.showMessageDialog(null, "Registro modificado", "Modificacion", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {

            System.out.println("Error de modificacion: " +e.getErrorCode());
            handlearError(e.getErrorCode(), modelo.getClass().getSimpleName());
            e.printStackTrace();
        }
    }

    /**
     * Realiza la validacion de datos, mapeando el código de error al índice de cada dato analizado
     * @param datos datos a validar
     * @param regex expresiónes regulares de cada campo
     * @param longitudes longitud máxima de los campos
     * @param nonulos booleanos que indican cuáles campos no deben ser nulos
     * @param umbral longitudes mínimas de los campos
     * @return mapa de índices de campos y sus errores, en orden de inserción
     */
    public LinkedHashMap<Integer, Integer> obtenerErrores(ArrayList<Object> datos, String[] regex, Integer[] longitudes, Boolean[] nonulos, Integer[] umbral){
        LinkedHashMap<Integer, Integer> o = new LinkedHashMap<>();
        for (int i = 0; i < datos.size(); i++) {
            String n = null;
            if(datos.get(i) != null) n = datos.get(i).toString();
            int code = Validador.probarString(n, regex[i], longitudes[i], nonulos[i], umbral[i]);
            if(code != 0) o.put(i, code);
        }
        return o;
    }

    /**
     * Actualiza los registros de una tabla a traves de una consulta a la BD
     * @param s tabla a actualizar
     * @param tabla nombre de la tabla a consultar en la BD
     * @param selecNombres datos a consultar en la tabla de la BD
     * @param filtroNombres campos a filtrar en la consulta a la BD
     * @param filtroValores valores a filtrar en la consulta a la BD
     */
    public void actualizarTablaABCC(ScrollHook s, String tabla, String[] selecNombres, String[] filtroNombres, Object[] filtroValores){
        ArrayList<ModeloBD> m = realizarConsulta(tabla, selecNombres, filtroNombres, filtroValores);
        if(m == null) return;
        FormHook.limpiarTabla(s);
        FormHook.rellenarTabla(s, m, tabla);
        configurarBotonesRegistros(s);
    }
    /**
     * Actualiza los registros de una tabla a traves de una consulta a la BD
     * @param selecNombres datos a consultar en la tabla de la BD
     * @param filtroNombres campos a filtrar en la consulta a la BD
     * @param filtroValores valores a filtrar en la consulta a la BD
     */
    public void actualizarTablaABCCThread(ArrayList<String> selecNombres, ArrayList<String> filtroNombres, ArrayList<Object> filtroValores){

        String[] sn = null;
        String[] fn = null;
        Object[] fv = null;
        if(selecNombres!=null)sn = selecNombres.toArray(new String[0]);
        if(filtroNombres!=null)fn = filtroNombres.toArray(new String[0]);
        if(filtroValores!=null)fv = filtroValores.toArray();

        String[] finalSn = sn;
        String[] finalFn = fn;
        Object[] finalFv = fv;
        new Thread(() -> {
            ArrayList<ModeloBD> m = realizarConsulta(tabla, finalSn, finalFn, finalFv);

            if(m == null) return;
            FormHook.limpiarTabla(scroll);
            FormHook.rellenarTabla(scroll, m, tabla);
            configurarBotonesRegistros(scroll);
        }).start();
    }
    /**
     * Actualiza los registros de una tabla a traves de una consulta a la BD con filtros aproximados
     * @param selecNombres datos a consultar en la tabla de la BD
     * @param filtroNombres campos a filtrar en la consulta a la BD
     * @param filtroValores valores a filtrar en la consulta a la BD
     */
    public void actualizarTablaABCCThreadLike(ArrayList<String> selecNombres, ArrayList<String> filtroNombres, ArrayList<Object> filtroValores){

        String[] sn = null;
        String[] fn = null;
        Object[] fv = null;
        if(selecNombres!=null)sn = selecNombres.toArray(new String[0]);
        if(filtroNombres!=null)fn = filtroNombres.toArray(new String[0]);
        if(filtroValores!=null)fv = filtroValores.toArray();

        String[] finalSn = sn;
        String[] finalFn = fn;
        Object[] finalFv = fv;
        new Thread(() -> {
            ArrayList<ModeloBD> m = realizarConsultaLike(tabla, finalSn, finalFn, finalFv);

            FormHook.limpiarTabla(scroll);
            FormHook.rellenarTabla(scroll, m, tabla);
            configurarBotonesRegistros(scroll);
        }).start();
    }
    /**
     * Restaura el registro anteriormente seleccionado (si existe) y guarda el nuevo registro, actualizando su color
     * y comportamiento de clic. Si el nuevo registro es nulo, estas configuraciones se saltan
     * @param r registro a seleccionar
     */
    public void cambiarSeleccion(Registro r){
        if(registroSeleccionado != null){
            registroSeleccionado.setColorsNormal();
            System.out.println("DESELECCIONADO");
            registroSeleccionado.setClicAccion(data -> {
                return;
            });
        }
        registroSeleccionado = r;

        if(r != null){
            r.setColorsSeleccion();
            modeloSeleccionado = r.asociado;
            r.setClicAccion(data -> cambiarSeleccion(null));

            ((JButton)btnAgregar.componente).setText("MODIFICAR");
            btnAgregar.setForeground(Color.YELLOW);
        }else {
            modeloSeleccionado = null;
            ((JButton)btnAgregar.componente).setText("AGREGAR");
            btnAgregar.setForeground(Color.CYAN);
        };

    }

    /**
     * Crea un panel de confirmacion para eliminar registros
     * @return eleccion del panel
     */
    public int optionPaneEliminar(){
        return JOptionPane.showOptionDialog(null,  "Confirme la elminacion del registro", "Confirmar eliminacion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
                new Object[]{"Continuar", "Cancelar"}, "Continuar");
    }

    /**
     * Configura los botones de eliminar y editar de todos los registros de la tabla dada
     * @param tabla tabla cuyos registros se desean configurar
     */
    public void configurarBotonesRegistros(ScrollHook tabla){
        ArrayList<Registro> regs = FormHook.obtenerRegistros(tabla);

        for (Registro reg : regs) {

            reg.configurarEliminar(e -> {
                cambiarSeleccion(reg);

                int eleccion = optionPaneEliminar();
                if(eleccion != 0) return;

                eliminar(modeloSeleccionado);
                actualizarTablaABCCThread(selecNombres, filtroNombres, filtroValores);
                cambiarSeleccion(null);
            });

            reg.configurarEditar(e -> {
                cambiarSeleccion(reg);
                //rellenar el formulario con los datos
                FormHook f = formulario.form;

                try {
                    f.colocar(modeloSeleccionado);
                } catch (IllegalAccessException ex) {
                    System.out.println("No se pudo acceder a los campos del registro");
                    cambiarSeleccion(null);
                }
            });
        }
    }
}
