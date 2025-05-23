package Vista;

import ErrorHandlin.ErrorHandler;
import FormTools.ComboHook;
import Modelo.Auto;
import Modelo.Auto_Modelo;
import Modelo.ModeloBD;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ABCCAuto extends ABCC{
    public ABCCAuto() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Auto.class.getSimpleName());

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya existe un auto con la misma ID", "Auto duplicado");
        });

        ErrorHandler.registrarHandler(ErrorHandler.SQL_UNKNOWN_FOREIGN, data -> {
            Ventana.panelError("El modelo de auto no existe en la base de datos", "Modelo desconocido");
        });

        comboBoxOpciones("modelo", Auto_Modelo.class.getSimpleName(), null);

    }
}
