package controlador;

import FormTools.Extractor;
import Modelo.ModeloBD;
import Modelo.Registrable;
import conexion.ConexionBD;

import javax.xml.transform.Result;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class DAO {
    ConexionBD conexion = ConexionBD.getConector();
    ResultSet rs = null;
    public static DAO d = new DAO();
    private DAO(){}

    /**
     * Agrega un modelo a la base de datos
     * @param r Modelo a agregar
     * @throws IllegalAccessException si los datos del modelo no son accesibles
     * @throws SQLException si falla la adición
     */
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
        System.out.println(sql + " :: " + Arrays.toString(datos));
        conexion.ejecutarDML();
    }

    /**
     * Realiza una consulta a la Base de datos
     * @param tabla nombre de la tabla a consultar
     * @param selecNombres columnas a seleccionar de la consulta
     * @param filtroNombres columnas a filtrar en la consulta
     * @param filtroValores valores de las columnas a filtrar
     * @return ResultSet con los registros
     * @throws SQLException si falla la consulta
     */
    public ResultSet consultar(String tabla, String[] selecNombres, String[] filtroNombres, Object[] filtroValores) throws SQLException {
        Object[] val = new Object[0];
        String sql = "SELECT ";
        if(selecNombres == null) sql += "* ";
        else{
            for (String selecNombre : selecNombres) {
                sql+=selecNombre+", ";
            }
            sql = sql.substring(0, sql.length()-2)+" ";
        }
        sql += "FROM " + tabla;
        if(filtroNombres != null && filtroValores != null){
            val = filtroValores;
            sql += " WHERE ";
            String fil = "";
            for (int i = 0; i < filtroNombres.length; i++) {
                Object valor = filtroValores[i];
                String v = "";
                if(valor instanceof String || valor instanceof Date) v = "'"+valor.toString()+"'";
                //fil += filtroNombres[i]+"="+v+", ";
                fil+= filtroNombres[i]+"=? AND ";
            }
            fil = fil.substring(0, fil.length()-5);
            sql = sql+fil;
        }

        conexion.prepararStatement(sql, val);
        return conexion.ejecutarSQL();
    }
    public ResultSet consultarGroup(String tabla, String[] selecNombres, String[] filtroNombres, Object[] filtroValores, String grupoNombre) throws SQLException {
        Object[] val = new Object[0];
        String sql = "SELECT ";
        if(selecNombres == null) sql += "* ";
        else{
            for (String selecNombre : selecNombres) {
                sql+=selecNombre+", ";
            }
            sql = sql.substring(0, sql.length()-2)+" ";
        }
        sql += "FROM " + tabla;
        if(filtroNombres != null && filtroValores != null){
            val = filtroValores;
            sql += " WHERE ";
            String fil = "";
            for (int i = 0; i < filtroNombres.length; i++) {
                Object valor = filtroValores[i];
                String v = "";
                if(valor instanceof String || valor instanceof Date) v = "'"+valor.toString()+"'";
                //fil += filtroNombres[i]+"="+v+", ";
                fil+= filtroNombres[i]+"=? AND ";
            }
            fil = fil.substring(0, fil.length()-5);
            sql = sql+fil;
        }
        sql += " GROUP BY "+ grupoNombre;
        conexion.prepararStatement(sql, val);
        return conexion.ejecutarSQL();
    }
    /**
     * Realiza una consulta a la Base de datos, busca coincidencias aproximadas a los filtros.
     * Utiliza LIKE y agrega comodines '%' al final de cada valor a filtrar
     * @param tabla nombre de la tabla a consultar
     * @param selecNombres columnas a seleccionar de la consulta
     * @param filtroNombres columnas a filtrar en la consulta
     * @param filtroValores valores de las columnas a filtrar
     * @return ResultSet con los registros
     * @throws SQLException si falla la consulta
     */
    public ResultSet consultarLike(String tabla, String[] selecNombres, String[] filtroNombres, Object[] filtroValores) throws SQLException {
        Object[] val = new Object[0];
        String[] likes = new String[0];
        String sql = "SELECT ";
        if(selecNombres == null) sql += "* ";
        else{
            for (String selecNombre : selecNombres) {
                sql+=selecNombre+", ";
            }
            sql = sql.substring(0, sql.length()-2)+" ";
        }
        sql += "FROM " + tabla;
        if(filtroNombres != null && filtroValores != null){
            val = filtroValores;
            likes = new String[val.length];

            sql += " WHERE ";
            String fil = "";
            for (int i = 0; i < filtroNombres.length; i++) {
                fil += filtroNombres[i]+" LIKE ? AND ";
                likes[i] = "%";
                if(filtroValores[i] != null) {
                    String txt = filtroValores[i].toString();
                    if(filtroValores[i] instanceof Boolean) txt = txt.toUpperCase();
                    likes[i] = txt.concat("%");
                }
                //likes[i] = filtroValores[i].toString().concat("%");
            }
            fil = fil.substring(0, fil.length()-5);
            sql = sql+fil;
        }
        //System.out.println("CONSUTA LIKE "+Arrays.toString(likes) + " sql " + sql);
        conexion.prepararStatement(sql, likes);
        return conexion.ejecutarSQL();
    }
    public ArrayList<ModeloBD> obtenerRegistros(String tabla, String[] selecNombres, String[] filtroNombres, Object[] filtroValores) throws SQLException {
        ResultSet rs = consultar(tabla, selecNombres, filtroNombres, filtroValores);
        Object[] datos = new Object[rs.getMetaData().getColumnCount()];
        ArrayList<ModeloBD> modelos = new ArrayList<>();

        while (rs.next()){
            for (int i = 0; i < datos.length; i++) {
                datos[i] = rs.getObject(i+1);
            }
            modelos.add(ModeloBD.instanciar(tabla, datos));
        }
        return modelos;
    }
    public ArrayList<ModeloBD> obtenerRegistrosGroup(String tabla, Class<? extends Registrable> modelo, String[] selecNombres, String[] filtroNombres, Object[] filtroValores, String grupo) throws SQLException {
        ResultSet rs = consultarGroup(tabla, selecNombres, filtroNombres, filtroValores, grupo);
        ArrayList<ModeloBD> modelos = new ArrayList<>();

        String[] argNames = ModeloBD.obtenerCampoNombresDe(modelo);
        Class[] paramTypes = ModeloBD.obtenerCampoTiposDe(modelo);
        Object[] args = new Object[argNames.length];

        while (rs.next()){
            for (int i = 0; i < args.length; i++) {
                String columna = rs.getMetaData().getColumnName(i+1);

                //si los campos del modelo no coinciden con el resultSet, entonces se pone un nulo
                if(!columna.equalsIgnoreCase(argNames[i])){
                    System.out.println("DAO campos irregulares: esperaba '"+argNames[i]+"', recibio '"+columna+"'");
                    args[i] = null;
                }else {
                    args[i] = Extractor.convertir(rs.getObject(i+1).toString(), paramTypes[i]);

                }
            }
            System.out.println("DAO Instanciando GROUP:" + Arrays.toString(args));
            modelos.add(ModeloBD.instanciar(modelo.getSimpleName(), args));
        }
        return modelos;
    }
    public ArrayList<ModeloBD> obtenerRegistrosLike(String tabla, String[] selecNombres, String[] filtroNombres, Object[] filtroValores) throws SQLException {
        ResultSet rs = consultarLike(tabla, selecNombres, filtroNombres, filtroValores);
        Object[] datos = new Object[rs.getMetaData().getColumnCount()];
        ArrayList<ModeloBD> modelos = new ArrayList<>();

        while (rs.next()){
            for (int i = 0; i < datos.length; i++) {
                datos[i] = rs.getObject(i+1);
            }
            modelos.add(ModeloBD.instanciar(tabla, datos));
        }
        return modelos;
    }
    public void modificar(ModeloBD reg, Object[] primariasOriginales) throws SQLException {
        try {
            String nom = reg.getClass().getSimpleName();
            Integer[] primariasIdx = ModeloBD.obtenerPrimariasDe(nom);
            String[] nombres = ModeloBD.obtenerCampoNombresDe(reg.getClass());
            Object[] valores = reg.obtenerDatos();
            Object[] valoresFinal = new Object[valores.length+primariasOriginales.length];

            String sql = "UPDATE " + nom + " SET ";
            for (String nombre : nombres) {
                sql += nombre +"=?, ";
            }
            sql = sql.substring(0, sql.length()-2);

            int i = 0;
            for (; i < valores.length; i++) {
                valoresFinal[i] = valores[i];
            }
            for (int j = 0; j < primariasOriginales.length; j++){
                valoresFinal[i+j] = primariasOriginales[j];
            }
            //armar el where con las primarias originales del registro
            String where = " WHERE ";
            for (int idx : primariasIdx) {
                where += nombres[idx]+"=? AND ";
            }
            where = where.substring(0, where.length()-5);


            conexion.prepararStatement(sql+where, valoresFinal);
            conexion.ejecutarDML();

        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void eliminar(ModeloBD reg) throws SQLException {
        String nom = reg.getClass().getSimpleName();
        try {
            Integer[] primariasIdx = ModeloBD.obtenerPrimariasDe(nom);
            String[] nombres = ModeloBD.obtenerCampoNombresDe(reg.getClass());
            Object[] valores = reg.obtenerDatos();
            Object[] filtrados = new Object[primariasIdx.length];
            String sql = "DELETE FROM " + nom;
            String where = " WHERE ";
            for (Integer idx : primariasIdx) {
                where+=nombres[idx]+"=? AND ";
            }
            int i = 0;
            for (Object filtrado : filtrados) {
                filtrados[i] = valores[primariasIdx[i]];
            }
            where = where.substring(0, where.length()-5);
            conexion.prepararStatement(sql+where, filtrados);
            conexion.ejecutarDML();

        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
    public ResultSet getRs(){
        return rs;
    }
}
