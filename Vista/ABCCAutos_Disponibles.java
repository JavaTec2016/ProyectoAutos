package Vista;

import Modelo.Autos_Disponibles;

import java.lang.reflect.InvocationTargetException;

public class ABCCAutos_Disponibles extends ABCC{

    public ABCCAutos_Disponibles() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(Autos_Disponibles.class.getSimpleName());

        
    }
}
