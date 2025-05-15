package Vista;

import ErrorHandlin.ErrorHandler;
import Modelo.Auto;

import java.lang.reflect.InvocationTargetException;

public class ABCCAuto extends ABCC{
    public ABCCAuto() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Auto.class.getSimpleName());

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya existe un auto con la misma ID", "Vendedor duplicado");
        });

        ErrorHandler.registrarHandler(ErrorHandler.SQL_UNKNOWN_FOREIGN, data -> {
            Ventana.panelError("El modelo de auto no existe en la base de datos", "Modelo desconocido");
        });
    }
}
