package Vista;

import ErrorHandlin.ErrorHandler;
import Modelo.Auto_Modelo;

import java.lang.reflect.InvocationTargetException;

public class ABCCAuto_Modelo extends ABCC {

    public ABCCAuto_Modelo() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Auto_Modelo.class.getSimpleName());

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya existe un auto con la misma ID", "Vendedor duplicado");
        });

    }
}
