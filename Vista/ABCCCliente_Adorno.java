package Vista;

import ErrorHandlin.ErrorHandler;
import Modelo.Auto_Opcion;
import Modelo.Cliente;
import Modelo.Cliente_Adorno;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class ABCCCliente_Adorno extends ABCC {

    public ABCCCliente_Adorno() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Cliente_Adorno.class.getSimpleName());
        titulo.setText("Modificaciones seleccionadas");
        titulo.setAlignment(SwingConstants.CENTER, SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya hay una opción registrada con la misma ID", "Opción duplicado");
        });

        listHookOciones("id_cliente", Cliente.class.getSimpleName(), "Opciones...");
        listHookOciones("id_opcion", Auto_Opcion.class.getSimpleName(), "Opciones...");
    }
}
