package Modelo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ModeloBD implements Registrable {
    static HashMap<String, Class<ModeloBD>> modelos = new HashMap<>();
    public static void registrarModelo(Class<ModeloBD> c){
        modelos.put(c.getSimpleName(), c);
    }
    public static Class<ModeloBD> getModelo(String n){
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
    static Class<?>[] obtenerCampoTiposDe(Class<? extends Registrable> c){
        Field[] f = c.getDeclaredFields();
        Class<?>[] s = new Class[f.length];

        int i = 0;
        for (Field field : f){
            s[i] = field.getType();
            i++;
        }
        return s;
    }
    private static Object invocar(String nombre, String metodo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method mt = getModelo(nombre).getDeclaredMethod(metodo, (Class<?>) null);
        return mt.invoke(null, (Object) null);
    }
    public static String[] obtenerLabelsDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (String[]) invocar(modelo, "obtenerLabels");
    }
    public static String[] obtenerCampoTiposSQLDe(String modelo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (String[]) invocar(modelo, "obtenerCampoTiposSQL");
    }
}
