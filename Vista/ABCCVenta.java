package Vista;

import ErrorHandlin.ErrorHandler;
import FormTools.ListHook;
import Modelo.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ABCCVenta extends ABCC {

    public ABCCVenta() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Venta.class.getSimpleName());

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya hay una opción registrada con la misma ID", "Opción duplicado");
        });

        ErrorHandler.registrarHandler(ErrorHandler.SQL_UNKNOWN_FOREIGN, data -> {
            Ventana.panelError("Algunos de los campos no fueron encontrados: " + ModeloBD.formatearMensajeErrorForaneas(tabla), "Opción duplicado");
        });
        ErrorHandler.registrarHandler(ErrorHandler.SQL_FOREIGN_RElATION, data -> {
            panelError("Imposible eliminar: Esta venta está asociada a un intercambio", "Error de eliminación");
        });

        listHookOciones("id_cliente", Cliente.class.getSimpleName(), "Opciones...");
        listHookOciones("id_vendedor", Vendedor.class.getSimpleName(), "Opciones...");
        listHookOciones("id_auto", Auto.class.getSimpleName(), "Opciones...");
    }

}
