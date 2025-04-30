package controlador;

import Modelo.ModeloBD;
import Modelo.Registrable;
import conexion.ConexionBD;

import java.sql.SQLException;

public class DAO {
    ConexionBD conexion = ConexionBD.getConector();

    public static DAO d = new DAO();

    public void agregar(Registrable r) throws IllegalAccessException, SQLException {
        String tipo = r.getClass().getSimpleName();
        String[] nombres = ModeloBD.obtenerCampoNombresDe(r.getClass());

        //generar el prepared statement
        String sql = "INSERT INTO " + tipo + " VALUES(";
        for (int i = 0; i < nombres.length; i++) {
            sql+="?, ";
        }
        sql = sql.substring(0, sql.length()-2)+")";

        //obtener los valores del modelo
        Object[] datos = r.obtenerDatos();

        conexion.prepararStatement(sql, datos);
        conexion.ejecutarDML();
    }
}
