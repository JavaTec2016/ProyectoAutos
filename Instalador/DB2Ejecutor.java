package Instalador;

import Lectura.Lector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.function.Consumer;

public interface DB2Ejecutor {
    ArrayList<String> salida = new ArrayList<>();
    static String scriptPath(String nombre){
        return Lector.getSrcPath().concat("/Instalador/").concat(nombre);
    }

    private static ProcessBuilder crearBuilder(String ...comm){
        ProcessBuilder builder = new ProcessBuilder(comm);
        builder.inheritIO();
        //builder.redirectErrorStream(true);
        return builder;
    }
    static int crearInstancia(String nombre, String puerto, String autoRun) throws IOException, InterruptedException {
        salida.clear();

        ProcessBuilder bld = crearBuilder("cmd.exe", "/c", scriptPath("instanciadorParams.bat"), nombre, puerto, autoRun);
        Process p = bld.start();

        //guardarSalida(p);
        return p.waitFor();
    }
    static int eliminarInstancia(String nombre) throws IOException, InterruptedException {
        salida.clear();
        ProcessBuilder bld = crearBuilder("cmd.exe", "/c", scriptPath("eliminarInstancia.bat"), nombre);
        Process p = bld.start();
        //guardarSalida(p);
        return p.waitFor();
    }
    static void guardarSalida(Process p) throws IOException {
        Lector.cargarStreamReader(new InputStreamReader(p.getInputStream(), "CP850"));
        Lector.porCadaLinea(salida::add);
        for (String s : salida) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        crearInstancia("DB2Inst2", "55000", "true");

    }



}
