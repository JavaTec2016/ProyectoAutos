package Vista;

import Modelo.*;

import java.lang.reflect.InvocationTargetException;

public class ABCCOpciones_Activas extends ABCC {

    public ABCCOpciones_Activas() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Opciones_Activas.class.getSimpleName());
        titulo.setText("Opciones Activas");

        listHookOciones("id_adorno", Cliente_Adorno.class.getSimpleName(), "Opciones...");
        listHookOciones("id_cliente", Cliente.class.getSimpleName(), "Opciones...");
        listHookOciones("id_opcion", Auto_Opcion.class.getSimpleName(), "Opciones...");
        listHookOciones("id_auto", Auto.class.getSimpleName(), "Opciones...");

        setRegistrosEditables(false);
        setRegistrosEliminables(false);
        setVisibleAgregar(false);
    }
}
