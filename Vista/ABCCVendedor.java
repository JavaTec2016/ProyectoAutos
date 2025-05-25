package Vista;

import ErrorHandlin.ErrorHandler;
import Modelo.Vendedor;

import java.lang.reflect.InvocationTargetException;

public class ABCCVendedor extends ABCC {

    public ABCCVendedor() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Vendedor.class.getSimpleName());
        titulo.setText("Vendedores");
        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya existe un vendedor con la misma ID", "Vendedor duplicado");
        });
        ErrorHandler.registrarHandler(ErrorHandler.SQL_FOREIGN_RElATION, data -> {
            panelError("Imposible eliminar: Este vendedor está asignado en una o más ventas", "Error de eliminación");
        });
    }
}
