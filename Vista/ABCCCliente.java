package Vista;

import ErrorHandlin.ErrorHandler;
import Modelo.Cliente;

import java.lang.reflect.InvocationTargetException;

public class ABCCCliente extends ABCC {

    public ABCCCliente() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Cliente.class.getSimpleName());

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya existe un cliente con la misma ID", "Cliente duplicado");
        });
    }
}
