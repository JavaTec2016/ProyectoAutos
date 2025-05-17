package Vista;

import ErrorHandlin.ErrorHandler;
import Modelo.Auto_Opcion;

import java.lang.reflect.InvocationTargetException;

public class ABCCAuto_Opcion extends ABCC {

    public ABCCAuto_Opcion() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Auto_Opcion.class.getSimpleName());

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya hay una opción registrada con la misma ID", "Opción duplicado");
        });
        ErrorHandler.registrarHandler(ErrorHandler.SQL_UNKNOWN_FOREIGN, data -> {
            panelError("No existe un auto con la ID proporcionada", "El auto no existe");
        });

    }

}
