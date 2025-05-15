package Lectura;

import java.io.*;
import java.util.function.Consumer;

public class Lector {
    private static BufferedReader reader;

    /**
     * Obtiene la ruta actual del directorio "src" en disco
     * @return ruta en el disco
     */
    public static String getSrcPath(){
        return new File("").getAbsolutePath().concat("/src");
    }
    public static String getScriptPath(String nombre){
        return getSrcPath().concat("/sql/").concat(nombre).concat(".sql");
    }
    /**
     * Abre un script sql del directorio "sql" en el lector de archivos
     * @param nombre nombre del archivo
     * @throws FileNotFoundException si no se encontró el script
     */
    public static void abrirScript(String nombre) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(getScriptPath(nombre)));
    }

    /**
     * Ejecuta una función por cada línea de texto del archivo abierto
     * @param consumer funcion a ejecutar
     * @throws IOException si hay errores de lectura del archivo
     */
    public static void porCadaLinea(Consumer<String> consumer) throws IOException {
        String line = "";
        while ((line = reader.readLine())!=null){
            consumer.accept(line);
        }
    }
    public static void cargarStreamReader(InputStreamReader r){
        reader = new BufferedReader(r);
    }

}
