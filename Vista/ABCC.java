package Vista;

import ErrorHandlin.Call;
import ErrorHandlin.ErrorHandler;
import ErrorHandlin.ErrorMessageList;
import ErrorHandlin.Validador;
import FormTools.CampoHook;
import FormTools.FormHook;
import FormTools.PanelHook;
import FormTools.ScrollHook;
import Modelo.ModeloBD;
import controlador.DAO;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ABCC extends JPanel{
    ArrayList<String> selecNombres = null;
    ArrayList<String> filtroNombres = null;
    ArrayList<Object> filtroValores = null;

    ModeloBD modeloSeleccionado = null;
    Registro registroSeleccionado = null;

    PanelHook ventana;
    PanelHook formulario;
    CampoHook btnAgregar;
    ScrollHook scroll;
    String tabla;

    public ErrorMessageList errores;

    public ABCC(String tabla) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        this.tabla = tabla;
        ArrayList<ModeloBD> ar = realizarConsulta(tabla, null, null, null);
        ventana = FormHook.crearABCC(tabla, ar);
        formulario = (PanelHook) ventana.getChild("sidebar");
        errores = new ErrorMessageList(formulario.form);
        scroll = (ScrollHook)ventana.childPath("main/tableHolder/tabla");
        btnAgregar = ventana.childPath("sidebar/foot/btnAgregar");

        configurarAgregar();
        registrarMensajesValidacionGenericos();
        registrarHandlersValidacion();
        actualizarTablaABCCThread(selecNombres, filtroNombres, filtroValores);
        add(ventana.componente);
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
     * Agrega un nuevo modelo si no existe una selección, o actualiza el modelo seleccionado
     * @param modelo nuevo modelo a ingresar a la BD
     */
    public void agregarSeleccion(ModeloBD modelo){
        if(registroSeleccionado == null){
            agregar(modelo);
        }else{
            try {
                modificar(modelo, ModeloBD.extraerPrimarias(modeloSeleccionado));
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
    public void panelError(String msg, String titulo){
        JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.ERROR_MESSAGE);
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
        ErrorHandler.ejecutarHandler(codigo, error);
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
        }
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
        FormHook.rellenarTabla(s, m);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ModeloBD> m = realizarConsulta(tabla, finalSn, finalFn, finalFv);

                if(m == null) return;
                FormHook.limpiarTabla(scroll);
                FormHook.rellenarTabla(scroll, m);
                configurarBotonesRegistros(scroll);
            }
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
            registroSeleccionado.setClicAccion(new Call() {
                @Override
                public void run(Object... data) {
                    return;
                }
            });
        }
        registroSeleccionado = r;

        if(r != null){
            r.setColorsSeleccion();
            modeloSeleccionado = r.asociado;

            r.setClicAccion(new Call() {
                @Override
                public void run(Object... data) {
                    cambiarSeleccion(null);
                }
            });
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
