package Vista;

import ErrorHandlin.ErrorHandler;
import Modelo.Auto_Modelo;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class ABCCAuto_Modelo extends ABCC {

    public ABCCAuto_Modelo() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Auto_Modelo.class.getSimpleName());
        titulo.setText("Modelos de auto");
        titulo.setAlignment(SwingConstants.CENTER, SwingConstants.CENTER);
        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Este modelo ya existe", "Modelo duplicado");
        });

        ErrorHandler.registrarHandler(ErrorHandler.SQL_FOREIGN_RElATION, data -> {
            panelError("Imposible eliminar: hay autos asociados a este modelo", "Error de eliminación");
        });

    }
}
