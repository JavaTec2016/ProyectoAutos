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

        clienteOpciones();
        vendedorOpciones();
        autoOpciones();
    }
    private void clienteOpciones(){
        ListHook<Integer, String> lista = (ListHook<Integer, String>) getListHook("id_cliente");
        ArrayList<ModeloBD> registros = realizarConsulta(Cliente.class.getSimpleName(), null, null, null);
        lista.addItem(null, "Opciones...");
        registros.forEach(modeloBD -> {
            Cliente cliente = (Cliente)modeloBD;
            lista.addItem(cliente.getId(), cliente.getDisplay());
        });
    }
    private void vendedorOpciones(){
        ListHook<Integer, String> lista = (ListHook<Integer, String>) getListHook("id_vendedor");
        ArrayList<ModeloBD> registros = realizarConsulta(Vendedor.class.getSimpleName(), null, null, null);
        lista.addItem(null, "Opciones...");
        registros.forEach(modeloBD -> {
            Vendedor vendedor = (Vendedor)modeloBD;
            lista.addItem(vendedor.getId(), vendedor.getDisplay());
        });
    }
    private void autoOpciones(){
        ListHook<Integer, String> lista = (ListHook<Integer, String>) getListHook("id_auto");
        ArrayList<ModeloBD> registros = realizarConsulta(Auto.class.getSimpleName(), null, null, null);
        lista.addItem(null, "Opciones...");
        registros.forEach(modeloBD -> {
            Auto auto = (Auto)modeloBD;
            lista.addItem(auto.getId(), auto.getDisplay());
        });
    }
}
