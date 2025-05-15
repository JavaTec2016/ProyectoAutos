package Vista;

import ErrorHandlin.ErrorHandler;
import Modelo.Userio;

import java.lang.reflect.InvocationTargetException;

public class ABCCUserio extends ABCC {

    public ABCCUserio() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Userio.class.getSimpleName());

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya existe un usuario con el mismo nombre", "usuario duplicado");
        });
    }
}
