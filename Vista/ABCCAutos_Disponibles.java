package Vista;

import Modelo.Auto;
import Modelo.Auto_Modelo;
import Modelo.Autos_Disponibles;

import java.lang.reflect.InvocationTargetException;

public class ABCCAutos_Disponibles extends ABCC{

    public ABCCAutos_Disponibles() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Autos_Disponibles.class.getSimpleName());
        titulo.setText("Autos Disponibles");

        listHookOciones("id_auto", Auto.class.getSimpleName(), "Opciones...");
        comboBoxOpciones("modelo", Auto_Modelo.class.getSimpleName(), null);

        setRegistrosEditables(false);
        setRegistrosEliminables(false);
        setVisibleAgregar(false);
    }
}
