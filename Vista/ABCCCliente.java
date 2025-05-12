package Vista;

import ErrorHandlin.Call;
import ErrorHandlin.ErrorHandler;
import ErrorHandlin.ErrorMessageList;
import ErrorHandlin.Validador;
import FormTools.FormHook;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class ABCCCliente extends ABCC {

    public ABCCCliente(String tabla) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(tabla);

        FormHook form = formulario.form;
        registrarMensajesValidacionGenericos();

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya existe un cliente con la misma ID", "Error de datos");
        });
    }
}
