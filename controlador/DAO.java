package controlador;

import Modelo.ModeloBD;
import Modelo.Registrable;
import conexion.ConexionBD;

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
                fil+= filtroNombres[i]+"=?, ";
            }
            fil = fil.substring(0, fil.length()-2);
            sql = sql+fil;
        }

        System.out.println(sql);
        conexion.prepararStatement(sql, val);
        rs = conexion.ejecutarSQL();
        return rs;
    }
    public ArrayList<ModeloBD> obtenerRegistros(String tabla, String[] selecNombres, String[] filtroNombres, Object[] filtroValores) throws SQLException {
        consultar(tabla, selecNombres, filtroNombres, filtroValores);
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

            System.out.println(sql+where);
            System.out.println(Arrays.toString(valoresFinal));
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
                where+=nombres[idx]+"=?, ";
            }
            int i = 0;
            for (Object filtrado : filtrados) {
                filtrados[i] = valores[primariasIdx[i]];
            }
            where = where.substring(0, where.length()-2);
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
