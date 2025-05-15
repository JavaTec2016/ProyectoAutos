package Instalador;

import Modelo.Userio;

import java.io.*;

public abstract class Install {

    /**
     * Crea la instancia de DB2 para usuarios con los valores de {@link Config}
     * @throws IOException
     * @throws InterruptedException
     */
    public static void crearInstanciaUsuario() throws IOException, InterruptedException {
        DB2Ejecutor.crearInstancia(Config.INSTANCIA_USUARIOS_NOMBRE, Config.INSTANCIA_USUARIOS_PUERTO, Config.INSTANCIA_USUARIOS_AUTORUN, Config.USER);

    }
}
