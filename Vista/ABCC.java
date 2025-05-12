package Vista;

import ErrorHandlin.Call;
import ErrorHandlin.ErrorHandler;
import ErrorHandlin.Validador;
import FormTools.FormHook;
import FormTools.PanelHook;
import FormTools.ScrollHook;
import Modelo.ModeloBD;
import controlador.DAO;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ABCC extends JPanel{
    ArrayList<String> selecNombres = null;
    ArrayList<String> filtroNombres = null;
    ArrayList<Object> filtroValores = null;
    ModeloBD registroActual = null;
    Registro registroSeleccionado = null;

    PanelHook ventana;
    PanelHook formulario;
    ScrollHook scroll;
    String tabla;

    public ABCC(String tabla) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        this.tabla = tabla;
        ArrayList<ModeloBD> ar = realizarConsulta(tabla, null, null, null);
        ventana = FormHook.crearABCC(tabla, ar);
        formulario = (PanelHook) ventana.getChild("sidebar");
        scroll = (ScrollHook)ventana.childPath("main/tableHolder/tabla");
        add(ventana.componente);
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
            e.printStackTrace();
        }
    }


    public HashMap<Integer, Integer> obtenerErrores(ArrayList<Object> datos, String[] regex, Integer[] longitudes, Boolean[] nonulos, Integer[] umbral){
        HashMap<Integer, Integer> o = new HashMap<>();
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
            registroActual = r.asociado;

            r.setClicAccion(new Call() {
                @Override
                public void run(Object... data) {
                    cambiarSeleccion(null);
                }
            });

        }else registroActual = null;

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

                eliminar(registroActual);
                actualizarTablaABCCThread(selecNombres, filtroNombres, filtroValores);
                cambiarSeleccion(null);
            });

            reg.configurarEditar(e -> {
                cambiarSeleccion(reg);
                //rellenar el formulario con los datos
                FormHook f = formulario.form;

                try {
                    f.colocar(registroActual);
                } catch (IllegalAccessException ex) {
                    System.out.println("No se pudo acceder a los campos del registro");
                    cambiarSeleccion(null);
                }
            });
        }
    }
}
