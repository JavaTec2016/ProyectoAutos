package Instalador;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Install {
    public static ArrayList<String> salida = new ArrayList<>();
    public static int ejecutar() throws IOException, InterruptedException {
        salida.clear();
        String path = new File("").getAbsolutePath().concat("/src/Instalador/instalador.bat");
        //String oculta = "powershell.exe -WindowStyle Hidden -ExecutionPolicy Bypass -File \"" + path + "\"";
        System.out.println(path);
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", path);
        builder.redirectErrorStream(true);

        Process p = builder.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), "CP850"));
        String line;
        while ((line = r.readLine()) != null) {
            salida.add(line);
        }
        return p.waitFor();
    }
}
