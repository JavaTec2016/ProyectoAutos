package Modelo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class ModeloBD implements Registrable {
    static HashMap<String, Class<? extends ModeloBD>> modelos = new HashMap<>();
    public static void registrarModelo(Class<? extends ModeloBD> c){
        modelos.put(c.getSimpleName(), c);
    }
    public static Object[] extraerPrimarias(ModeloBD modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Integer[] pris = ModeloBD.obtenerPrimariasDe(modelo.getClass().getSimpleName());
        Object[] datos = modelo.obtenerDatos();

        Object[] o = new Object[pris.length];
        for (int i = 0; i < pris.length; i++) {
            int idx = pris[i];
            Object dato = datos[idx];
            o[i] = dato;
        }
        return o;
    }
    public static Class<? extends ModeloBD> getModelo(String n){
        if(!modelos.containsKey(n)) System.err.println("MODELO: el modelo '"+n+"' no fue encontrado");
        return modelos.get(n);
    }
    @Override
    public Object[] obtenerDatos() throws IllegalAccessException {
        Field[] f = getClass().getDeclaredFields();
        Object[] o = new Object[f.length];

        for (int i = 0; i < f.length; i++) {
            Field field = f[i];
            field.setAccessible(true);
            o[i] = field.get(this);
        }
        return o;
    }
    public static String[] obtenerCampoNombresDe(Class<? extends Registrable> c){
        Field[] f = c.getDeclaredFields();
        String[] s = new String[f.length];

        int i = 0;
        for (Field field : f) {
            s[i] = field.getName();
            i++;
        }
        return s;
    }
    public String getDisplay(){return null;};
    public LinkedHashMap<String, Object> toHashMap() throws IllegalAccessException {
        String[] noms = obtenerCamposNombresDe(getClass().getSimpleName());
        Object[] vals = obtenerDatos();
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();
        int i = 0;
        for (String nom : noms) {
            out.put(nom, vals[i]);
            i++;
        }
        return out;
    }
    public  static String[] obtenerCamposNombresDe(String modelo){
        return obtenerCampoNombresDe(getModelo(modelo));
    }
    public static Class<?>[] obtenerCampoTiposDe(Class<? extends Registrable> c){
        Field[] f = c.getDeclaredFields();
        Class<?>[] s = new Class[f.length];

        int i = 0;
        for (Field field : f){
            s[i] = field.getType();
            i++;
        }
        return s;
    }
    public static Class<?>[] obtenerCampoTiposDe(String modelo){
        return obtenerCampoTiposDe(getModelo(modelo));
    }
    private static Object invocar(String nombre, String metodo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method mt = getModelo(nombre).getDeclaredMethod(metodo);
        return mt.invoke(null);
    }
    public static String[] obtenerLabelsDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (String[]) invocar(modelo, "obtenerLabels");
    }
    public static String[] obtenerCampoTiposSQLDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (String[]) invocar(modelo, "obtenerCampoTiposSQL");
    }
    public static Integer[] obtenerPrimariasDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (Integer[])invocar(modelo, "obtenerPrimarias");
    }
    public static String[] obtenerCamposComponentesDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (String[]) invocar(modelo, "obtenerCamposComponentes");
    }
    public static Integer[] obtenerLongitudesDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (Integer[]) invocar(modelo, "obtenerLongitudes");
    }
    public static Integer[] obtenerUmbralesDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (Integer[]) invocar(modelo, "obtenerUmbrales");
    }
    public static Boolean[] obtenerNoNulosDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (Boolean[]) invocar(modelo, "obtenerNoNulos");
    }
    public static String[][] obtenerEspecialesDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (String[][]) invocar(modelo, "obtenerEspeciales");
    }
    public static String[] obtenerExpresionesDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (String[]) invocar(modelo, "obtenerExpresiones");
    }
    public static String[] obtenerValidadoresDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (String[]) invocar(modelo, "obtenerValidadores");
    }
    public static Integer[] obtenerForaneasDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (Integer[]) invocar(modelo, "obtenerForaneas");
    }

    /**
     * Filtra el arreglo dado, conserva los elementos en los índices de llaves primarias del modelo
     * @param modelo modelo con el cual se filtrarán los datos
     * @param filtrables el arreglo de datos a filtrar con el modelo
     * @return arreglo filtrado
     * @param <T> tipo de dato del arreglo
     * @throws InvocationTargetException si falla la obtención de índices primarios del modelo
     * @throws NoSuchMethodException si el modelo no cuenta con método para obtener índices primarios
     * @throws IllegalAccessException si el método del modelo no es accesible
     */
    public static <T> T[] dePrimarias(String modelo, T[] filtrables) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Integer[] primarias = obtenerPrimariasDe(modelo);
        Object[] out = new Object[primarias.length];

        for (int i = 0; i < primarias.length; i++) {

            out[i] = filtrables[primarias[i]];
        }
        return (T[]) out;
    }

    /**
     * Obtiene los nombres de los campos primarios del modelo
     * @param modelo modelo a obtener los nombres de sus campos primarios
     * @return arreglo con los nombres
     * @throws InvocationTargetException si falla la obtención de índices primarios del modelo
     * @throws NoSuchMethodException si el modelo no cuenta con método para obtener índices primarios
     * @throws IllegalAccessException si el método del modelo no es accesible
     */
    public static String[] obtenerNombresPrimariasDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return dePrimarias(modelo, obtenerCamposNombresDe(modelo));
    }

    /**
     * Crea una nueva instancia de un modelo con los datos proporcionados.
     * Los tipos y el orden de los datos deben coincidir con los del primer constructor del modelo
     * @param nombre nombre del modelo a instanciar
     * @param args datos de instancia
     * @return nueva instancia del modelo
     */
    public static ModeloBD instanciar(String nombre, Object[] args){
        Class<? extends ModeloBD> modlemombo = getModelo(nombre);
        Constructor<? extends ModeloBD> c = (Constructor<? extends ModeloBD>) modlemombo.getDeclaredConstructors()[0];

        try {
            return c.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public LinkedHashMap<String, Object> getInfoImportante(){return null;}

    public static void registrarModelos(){
        registrarModelo(Cliente.class);
        registrarModelo(Vendedor.class);
        registrarModelo(Userio.class);
        registrarModelo(Auto.class);
        registrarModelo(Auto_Modelo.class);
        registrarModelo(Auto_Opcion.class);
        registrarModelo(Opciones_Activas.class);
        registrarModelo(Venta.class);
    }
    public static String formatearMensajeErrorForaneas(String modelo){
        try {
            Integer[] foraneas = obtenerForaneasDe(modelo);
            String[] labels = obtenerLabelsDe(modelo);
            StringBuilder msg = new StringBuilder();
            int i = 0;
            Integer idx = foraneas[i];

            if(foraneas.length == 1) return labels[idx];

            for (; i < foraneas.length-1; i++) {
                idx = foraneas[i];
                msg.append(labels[idx]).append(", ");
            }
            msg.setLength(msg.length()-2);
            msg.append(" o ").append(labels[idx]);
            return msg.toString();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
