package Instalador;

import Lectura.Lector;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public interface DB2Ejecutor {
    ArrayList<String> salida = new ArrayList<>();
    String ruta = "/Instalador/";
    /**
     * Obtiene la ruta de scripts para la instalación de la base de datos
     * @param nombre
     * @return
     */
    static String scriptPath(String nombre){
        return Lector.getSrcPath().concat(ruta).concat(nombre);
    }
    static InputStream scriptPathStream(String nombre){
        return DB2Ejecutor.class.getResourceAsStream("/Instalador/".concat(nombre));
    }
    /**
     * Crea un nuevo {@link ProcessBuilder} para la ejecución de scripts por lotes
     * @param comm parámetros del proceso
     * @return {@link ProcessBuilder} configurado
     */
    private static ProcessBuilder crearBuilder(String ...comm){
        ProcessBuilder builder = new ProcessBuilder(comm);
        builder.inheritIO();
        return builder;
    }
    /**
     * Crea un nuevo {@link ProcessBuilder} con parámetros para ejecutar scripts en el entorno de DB2
     * @param args argumentos del script, debe iniciar con la ruta del script a ejecutar
     * @return {@link ProcessBuilder} configurado
     */
    private static ProcessBuilder crearDB2Builder(String ...args){
        System.out.println("\""+scriptPath("cargadorDB2.bat")+"\"");
        ArrayList<String> fullArgs = new ArrayList<>(List.of(new String[]{"cmd.exe", "/c", "\""+scriptPath("cargadorDB2.bat")+"\""}));
        fullArgs.addAll(Arrays.asList(args));
        ProcessBuilder b = new ProcessBuilder(fullArgs);
        b.inheritIO();
        return b;
    }
    /**
     * Crea un usuario de windows, si no existe, para el manejo de la BD
     * @param nombre nombre del usuario
     * @param password contraseña del usuario
     * @return codigo de salida del proceso
     * @throws IOException si ocurre un error al leer el script
     * @throws InterruptedException si la ejecución del script es interrumpida
     */
    static int crearUsuarioDB2(String nombre, String password) throws IOException, InterruptedException {
        salida.clear();
        ProcessBuilder bld = crearDB2Builder(scriptPath("crearUsuario.bat"), nombre, password);
        Process p = bld.start();
        return p.waitFor();
    }
    /**
     * Crea una nueva instancia de DB2 con el nombre y puerto especificados, puede establecer el arranque automático de la instancia.
     * Si ya hay una instancia con ese nombre, el script modifica sus configuraciones.
     * @param nombre nombre de la nueva instancia
     * @param puerto puerto de la nueva instancia
     * @param autoRun si la instancia arranca automáticamente al encender el equipo
     * @return código de salida del script
     * @throws IOException si ocurre un error al leer el script
     * @throws InterruptedException si la ejecución del script es interrumpida
     */
    static int crearInstancia(String nombre, String puerto, String autoRun, String usr) throws IOException, InterruptedException {
        salida.clear();

        ProcessBuilder bld = crearBuilder("cmd.exe", "/c", scriptPath("instanciadorParams.bat"), nombre, puerto, autoRun, usr);
        Process p = bld.start();
        return p.waitFor();
    }
    /**
     * Elimina una instancia de DB2 con el nombre especificado si existe.
     * @param nombre nombre de la nueva instancia
     * @return código de salida del script
     * @throws IOException si ocurre un error al leer el script
     * @throws InterruptedException si la ejecución del script es interrumpida
     */
    static int eliminarInstancia(String nombre) throws IOException, InterruptedException {
        salida.clear();
        ProcessBuilder bld = crearBuilder("cmd.exe", "/c", scriptPath("eliminarInstancia.bat"), nombre);
        Process p = bld.start();
        return p.waitFor();
    }
    /**
     * Guarda la salida de mensajes del script
     * @param p proceso del script
     * @throws IOException si ocurre un problema durante la lectura
     */
    static void guardarSalida(Process p) throws IOException {
        Lector.cargarStreamReader(new InputStreamReader(p.getInputStream(), "CP850"));
        Lector.porCadaLinea(salida::add);
        for (String s : salida) {
            System.out.println(s);
        }
    }
    /**
     * Ejecuta un script SQL de DB2 en una instancia especifica
     * @param instancia nombre de la instancia
     * @param ruta ruta del script SQL a ejecutar
     * @return código de salida del script
     * @throws IOException si ocurre un error al leer el script
     * @throws InterruptedException si la ejecución del script es interrumpida
     */
    private static int cargarScriptEn(String instancia, String ruta) throws IOException, InterruptedException {
        return crearDB2Builder(scriptPath("DB2vtfInstancia.bat"), instancia, ruta).start().waitFor();
    }
    private static int cargarScriptEn(String instancia, String ruta, String nombre, String pass) throws IOException, InterruptedException {
        return crearDB2Builder(scriptPath("DB2vtfInstancia.bat"), instancia, ruta, nombre, pass).start().waitFor();
    }
    /**
     * Crea las bases de datos de usuario y Autos en sus respectivas instancias
     * @throws IOException si ocurre un error al leer el script
     * @throws InterruptedException si la ejecución del script es interrumpida
     */
    static void instalarBases() throws IOException, InterruptedException {
        cargarScriptEn(Config.INSTANCIA_USUARIOS_NOMBRE, Lector.getScriptPath("usr"), Config.USER, Config.PASS);
        cargarScriptEn("DB2", Lector.getScriptPath("bd"), Config.USER, Config.PASS);
    }
    static void instalarBasesSencillo() throws IOException, InterruptedException {
        crearUsuarioDB2(Config.USER, Config.PASS);
        cargarScriptEn("DB2", Lector.getScriptPath("usr"));
        cargarScriptEn("DB2", Lector.getScriptPath("bd"));
    }

}
