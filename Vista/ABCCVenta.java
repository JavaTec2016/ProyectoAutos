package Vista;

import ErrorHandlin.ErrorHandler;
import Modelo.Venta;

import java.lang.reflect.InvocationTargetException;

public class ABCCVenta extends ABCC {

    public ABCCVenta() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Venta.class.getSimpleName());

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya hay una opción registrada con la misma ID", "Opción duplicado");
        });
    }
}
