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

        ErrorHandler.registrarHandler(ErrorHandler.SQL_DUPLICATE_ENTRY, data -> {
            Ventana.panelError("Ya hay una opción registrada con la misma ID", "Opción duplicado");
        });
        ErrorHandler.registrarHandler(ErrorHandler.SQL_UNKNOWN_FOREIGN, data -> {
            panelError("No existe un auto con la ID proporcionada", "El auto no existe");
        });

        ListHook<Integer, String> autoLista = (ListHook<Integer, String>)(formulario.form.getInput("id_auto").componente);
        ArrayList<ModeloBD> autos = realizarConsulta(Auto.class.getSimpleName(), null, null, null);

        autoLista.addItem(null, "Seleccione una opción..");
        for (ModeloBD modelo : autos) {
            Auto auto = (Auto)modelo;
            String display = modelo.getDisplay();
            autoLista.addItem(auto.getId(), display);
        }
    }

}
