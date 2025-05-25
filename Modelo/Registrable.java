package Modelo;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

public interface Registrable {
    /**
     * Obtiene los datos de la instancia del registrable
     * @return Arreglo de datos de la instancia
     * @throws IllegalAccessException si falla la lectura de datos
     */
    Object[] obtenerDatos() throws IllegalAccessException;

    /**
     * Retorna una representación textual corta del modelo
     * @return texto referente al modelo
     */
    String getDisplay();

    /**
     * Obtiene los nombres de las propiedades de la instancia
     * @return arreglo de nombres de las propiedades
     */
    static String[] obtenerCampoNombres() { return null; }

    /**
     * Obtiene las clases de los atributos de la instancia
     * @return arreglo de clases de los atributos instancia
     */
    static Class<?>[] obtenerCampoTipos(){ return null; }

    /**
     * Retorna los tipos de dato SQL definidos para el modelo
     * @return Arreglo de tipos de dato, definidos explicitamente en el modelo
     */
    static String[] obtenerCampoTiposSQL(){ return null; }
    /**
     * Retorna los labels definidos para el modelo
     * @return Arreglo con cada label, definidos explicitamente en el modelo
     */
    static String[] obtenerLabels(){ return  null; }
    /**
     * Retorna los tipos de componente gráfico definidos para la captura de datos del modelo
     * @return Arreglo de nombres de componentes, definidos explicitamente en el modelo
     */
    static String[] obtenerCamposComponentes(){ return null; }
    /**
     * Retorna las longitudes definidas para los campos del modelo
     * @return Arreglo de longitudes, definidas explicitamente en el modelo
     */
    static Integer[] obtenerLongitudes(){ return  null; }
    /**
     * Retorna las longitudes mínimas definidas para los campos del modelo
     * @return Arreglo de longitudes mínimas, definidas explicitamente en el modelo
     */
    static Integer[] obtenerUmbrales(){ return null; }
    /**
     * Retorna los requerimientos de no nulo para los campos del modelo
     * @return Arreglo de requerimientos no nulos, definidos explicitamente en el modelo
     */
    static Boolean[] obtenerNoNulos(){ return  null; }
    /**
     * Retorna las expresiones regulares de tecleo para los campos del modelo
     * @return Arreglo de expresiones regulares, definidas explicitamente en el modelo
     */
    static String[] obtenerExpresiones(){ return  null; }
    /**
     * Retorna las expresiones regulares de validación final para los campos del modelo
     * @return Arreglo de expresiones regulares, definidas explicitamente en el modelo
     */
    static String[] obtenerValidadores(){ return  null; }
    /**
     * Retorna los caracteres especiales permitidos para los campos del modelo
     * @return Arreglo que contiene arreglos de caracteres especiales, cada uno corresponde a los caracteres de un campo, definidos explicitamente en el modelo
     */
    static String[][] obtenerEspeciales(){ return null; }
    /**
     * Retorna los índices de las llaves primarias del modelo
     * @return Arreglo índices, definidos explicitamente en el modelo
     */
    static Integer[] obtenerPrimarias(){ return null; }
    /**
     * Retorna los índices de las llaves foráneas del modelo
     * @return Arreglo índices, definidos explicitamente en el modelo
     */
    static Integer[] obtenerForaneas(){ return null; }
    /**
     * Retorna un mapa con los valores importantes de la instancia del modelo y sus respectivos nombres de atributo.
     * @return {@link LinkedHashMap} de valores importantes, definido explicitamente en el modelo
     */
    LinkedHashMap<String, Object> getInfoImportante() throws IllegalAccessException;
    /**
     * Retorna un mapa con los nombres de atributo de {@link #getInfoImportante()} y su respectivo label.
     * @return {@link LinkedHashMap} de labels importantes.
     */
    LinkedHashMap<String, String> getLabelsImportantes() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    /**
     * Organiza los datos del modelo en un mapa con sus respectivos nombres de atributo.
     * @return {@link LinkedHashMap} de atributo-valor de la instancia del modelo
     * @throws IllegalAccessException si falla la lectura de datos del modelo
     */
    LinkedHashMap<String, Object> toHashMap() throws IllegalAccessException;
    /**
     * Organiza los labels del modelo en un mapa con sus respectivos nombres de atributo.
     * @return {@link LinkedHashMap} de atributo-label de la instancia del modelo
     * @throws IllegalAccessException si falla la lectura de atributos del modelo
     * @throws InvocationTargetException si el método de obtención de labels falla
     * @throws NoSuchMethodException si el método de obtención de labels del modelo no existe
     */
    LinkedHashMap<String, String> labelsToHashMap() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

}
