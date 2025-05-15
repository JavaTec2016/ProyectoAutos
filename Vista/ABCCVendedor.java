package Vista;

import ErrorHandlin.ErrorHandler;
import Modelo.Vendedor;

import java.lang.reflect.InvocationTargetException;

public class ABCCVendedor extends ABCC {

    public ABCCVendedor() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Vendedor.class.getSimpleName());

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya existe un vendedor con la misma ID", "Vendedor duplicado");
        });
    }
}
