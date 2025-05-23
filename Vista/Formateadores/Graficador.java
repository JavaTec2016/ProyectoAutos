package Vista.Formateadores;

import Modelo.*;
import controlador.DAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Graficador extends JFrame {
    public Graficador(String titulo, String categoria, String valor, DefaultCategoryDataset datasetParam) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(titulo);

        JFreeChart chart = ChartFactory.createBarChart(titulo,categoria,valor, datasetParam);

        ChartPanel panel = new ChartPanel(chart);

        panel.setPreferredSize(new Dimension(560, 300));
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public static void makeModelosVendidosChart() throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Graficador chart = new Graficador("Modelos más vendidos", "modelo", "ventas", getModelosVendidosDataset());
        chart.pack();
        chart.setVisible(true);
    }
    public static void makeModificacionesChart() throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Graficador chart = new Graficador("Modificaciones más populares", "Modificacion", "Elecciones", getModificacionesDataset());
        chart.pack();
        chart.setVisible(true);
    }
    public static DefaultCategoryDataset getModificacionesDataset() throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ArrayList<ModeloBD> modelos = DAO.d.obtenerRegistrosGroup(Opciones_Activas.class.getSimpleName(), Opciones_Activas_Conteo.class, new String[]{"opcion", "COUNT(*) AS ventas"}, null, null, "opcion");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (ModeloBD modelo : modelos) {
            Opciones_Activas_Conteo datos = (Opciones_Activas_Conteo)modelo;
            String[] labels = ModeloBD.obtenerLabelsDe(modelo.getClass().getSimpleName());

            dataset.addValue(datos.getVentas(), labels[1], datos.getOpcion());
        }
        return dataset;
    }
    public static DefaultCategoryDataset getModelosVendidosDataset() throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ArrayList<ModeloBD> modelos = DAO.d.obtenerRegistrosGroup(Datos_Venta.class.getSimpleName(), Datos_Venta_Conteo.class, new String[]{"modelo", "COUNT(*) AS ventas"}, null, null, "modelo");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (ModeloBD modelo : modelos) {
            Datos_Venta_Conteo datos = (Datos_Venta_Conteo)modelo;
            String[] labels = ModeloBD.obtenerLabelsDe(modelo.getClass().getSimpleName());

            dataset.addValue(datos.getVentas(), labels[1], datos.getModelo());
        }
        return dataset;
    }
}
