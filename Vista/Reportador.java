package Vista;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.util.HashMap;

public class Reportador {

    JasperDesign design;
    JasperReport report;
    JasperPrint print;
    String path;

    public void cargarReporte(String ruta, HashMap<String,Object> mapa, Connection conexion) throws JRException {
        path = ruta;
        System.out.println("Cargando...");
        design = JRXmlLoader.load(getClass().getResourceAsStream(path));
        System.out.println(design);
        report = JasperCompileManager.compileReport(design);
        print = JasperFillManager.fillReport(report, mapa, conexion);
    }
    public HashMap<String, Object> crearPropiedades(String[] keys, Object[] valores){
        int i = 0;
        HashMap<String, Object> output = new HashMap<>();
        for (String key : keys) {
            output.put(key, valores[i]);
            i++;
        }
        return output;
    }
    public static String reportePath(String nombre, boolean compilado){
        String extension = ".jrxml";
        if(compilado) extension = ".jasper";
        return "/assets/Reportes/"+nombre+extension;
    }
    public void verFactura(Integer id_venta, Connection connection) throws JRException {
        HashMap<String , Object> m = crearPropiedades(new String[]{"id_venta"}, new Object[]{id_venta});
        cargarReporte(reportePath("factura", false), m, connection);
        JasperViewer.viewReport(print, false);
    }
    public void verFacturaThread(Integer id_venta, Connection connection) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String , Object> m = crearPropiedades(new String[]{"id_venta"}, new Object[]{id_venta});
                try {
                    cargarReporte(reportePath("factura", false), m, connection);
                } catch (JRException e) {
                    throw new RuntimeException(e);
                }
                JasperViewer.viewReport(print, false);
            }
        }).start();

    }
}
