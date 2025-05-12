package Modelo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
}
