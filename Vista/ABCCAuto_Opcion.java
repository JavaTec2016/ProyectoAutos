package Vista;

import ErrorHandlin.ErrorHandler;
import FormTools.ListHook;
import Modelo.Auto;
import Modelo.Auto_Opcion;
import Modelo.ModeloBD;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ABCCAuto_Opcion extends ABCC {

    public ABCCAuto_Opcion() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Auto_Opcion.class.getSimpleName());
        titulo.setText("Lista de modificaciones");
        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya hay una opción registrada con la misma ID", "Opción duplicado");
        });
        ErrorHandler.registrarHandler(ErrorHandler.SQL_UNKNOWN_FOREIGN, data -> {
            panelError("No existe un auto con la ID proporcionada", "El auto no existe");
        });
        ErrorHandler.registrarHandler(ErrorHandler.SQL_FOREIGN_RElATION, data -> {
            panelError("Imposible eliminar: esta opción está seleccionada por uno o más clientes", "Error de eliminación");
        });

        listHookOciones("id_auto", Auto.class.getSimpleName(), "Opciones...");
    }

}
