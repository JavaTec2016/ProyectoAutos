package conexion;

import ErrorHandlin.ErrorHandler;
import Lectura.Lector;
import Modelo.ModeloBD;
import Modelo.Usuario;
import com.ibm.db2.jcc.am.Connection;
import controlador.DAO;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.function.Consumer;

public class ConexionBD {
    static Connection conexion = null;
    static ConexionBD conector = null;
    public PreparedStatement st = null;

    //usuario y esas cosas
    public static String puertoUsuarios = "60000";
    private Usuario usr;
    private ConexionBD(){}

    /**
     * Recupera el usuario actual de la sesión
     * @return usuario en sesión
     */
    public Usuario getUsr() {
        return usr;
    }

    public void abrirConexion(String usuario, String contra, String BD) throws SQLException {
        if(conexion != null && !conexion.isClosed()){
            System.out.println("ya hay una conexion");
            return;
        }
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            System.out.println("Driver prendido");
            Properties p = new Properties();
            p.put("user", usuario);
            p.put("password", contra);
            p.put("securityMechanism", "9");
            conexion = (Connection) DriverManager.getConnection("jdbc:db2:"+BD, p);

            if(conexion != null && !conexion.isClosed()){
                System.out.println("Conexion exitosa");

                //extraer el usuario para permisos y esas cosas

            }else {
                System.out.println("CONEXION FALLIDA");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("No hay driver");
            return;
        } catch (SQLException e){
            System.out.println("Error al conectar:");
            throw e;
        }
    }
    public void abrirConexionInstancia(String usuario, String contra, String BD, String puerto) throws SQLException {
        if(conexion != null && !conexion.isClosed()){
            System.out.println("ya hay una conexion");
            return;
        }
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            System.out.println("Driver prendido");
            Properties p = new Properties();
            p.put("user", usuario);
            p.put("password", contra);
            //p.put("traceFile", "db2trace.out");
            //p.put("traceLevel", "-1");
            //p.put("securityMechanism", "9");
            String url = "jdbc:db2://localhost:"+puerto+"/"+BD;
            conexion = (Connection) DriverManager.getConnection(url, p);

            if(conexion != null && !conexion.isClosed()){
                System.out.println("Conexion exitosa");

                //extraer el usuario para permisos y esas cosas

            }else {
                System.out.println("CONEXION FALLIDA");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("No hay driver");
            return;
        } catch (SQLException e){
            System.out.println("Error al conectar:");
            throw e;
        }
    }

    /**
     * Se conecta a la instancia que alberga usuarios y revisa si las credenciales coinciden
     * @param usuario usuario a probar
     * @param pass contraseña a probar
     * @param puerto puerto de la instancia que alberga usuarios
     * @return estado de la consulta, 0 si hay coincidencias o su respectivo código de error
     */
    public int probarCredenciales(String usuario, String pass, String puerto){
        try {
            abrirConexionInstancia(usuario, pass, "Usuario", puerto);
            DAO.d.consultar("Usuarios", null, new String[]{"usuario", "password"}, new Object[]{usuario, pass});
            boolean st = DAO.d.getRs().next();

            cerrarConexion();
            if(st) return ErrorHandler.OK;
            return ErrorHandler.LOGIN_NO_USER;

        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Obtiene el modelo de usuario de la instancia de usuarios
     * @param usuario nombre del usuario
     * @param pass contraseña del usuario
     * @return modelo de Usuario
     * @throws SQLException si ocurre un error durante la operación
     */
    public Usuario obtenerUsuario(String usuario, String pass) throws SQLException {
        abrirConexionInstancia(usuario, pass, "Usuario", puertoUsuarios);
        ArrayList<ModeloBD> ar = DAO.d.obtenerRegistros("Usuarios", null, new String[]{"usuario", "password"}, new Object[]{usuario, pass});
        return (Usuario)ar.get(0);
    }
    public void setAutoCommit(boolean estado) throws SQLException {
        conexion.setAutoCommit(estado);
    }

    /**
     * Guarda los cambios a la BD de forma permanente
     * @throws SQLException
     */
    public void commit() throws SQLException {
        conexion.commit();
    }

    /**
     * Revierte los cambios desde el último commit de la BD
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        conexion.rollback();
    }

    /**
     * Abre una conexion, comprobando que el usuario existe en la instancia de usuarios y guardando su modelo durante la sesion.
     * Deshabilita los commits automáticos
     * @param usuario nombre de usuario
     * @param pass contraseña
     * @return codigo de conexion, 0 si fue exitosa o su respectivo codigo de error
     */
    public int abrirConexion2(String usuario,String pass){
        int ocurrencia = probarCredenciales(usuario, pass, puertoUsuarios);
        if(ocurrencia==ErrorHandler.OK){
            try {
                usr = obtenerUsuario(usuario, pass);
                setAutoCommit(false);
            } catch (SQLException e) {
                ocurrencia = e.getErrorCode();
            }
        }
        return ocurrencia;
    }

    /**
     * Cierra la conexion actual si está abierta
     * @throws SQLException si falla la operación
     */
    public void cerrarConexion() throws SQLException {
        if(conexion != null && !conexion.isClosed()){
            System.out.println("Conexion cerrada");
            conexion.close();
        }
    }

    /**
     * Obtiene la instancia del conector
     * @return conector
     */
    public static ConexionBD getConector() {
        if(conector == null) conector = new ConexionBD();
        return conector;
    }

    /**
     * Obtiene la conexion a BD
     * @return objeto de conexion
     */
    private static Connection getConexion() {
        return conexion;
    }

    /**
     * Prepara una instrucción SQL segura con los datos especificados
     * @param sql instrucción SQL con placeholders
     * @param datos datos de la instrucción
     * @throws SQLException si falla la instrucción
     */
    public void prepararStatement(String sql, Object[] datos) throws SQLException {
        st = conexion.prepareStatement(sql);

        for (int i = 0; i < datos.length; i++) {
            colocarPrepared(i+1, datos[i]);
        }
    }

    /**
     * Coloca un dato en un índice de la instrucción SQL segura
     * @param idx indice del placeholder
     * @param dato dato a colocar
     * @throws SQLException si falla la inserción del dato en la instrucción
     */
    public void colocarPrepared(int idx, Object dato) throws SQLException {
        switch (dato.getClass().getSimpleName()){
            case "Integer": st.setInt(idx, (Integer)dato); break;
            case "Short": st.setShort(idx, (Short) dato); break;
            case "Byte": st.setByte(idx, (Byte)dato); break;
            case "String": st.setString(idx, (String)dato); break;
            case "Date": st.setDate(idx, (Date)dato); break;
            case "BigDecimal": st.setBigDecimal(idx, (BigDecimal)dato); break;
            case "Float": st.setFloat(idx, (Float)dato); break;
            case "Double": st.setDouble(idx, (Double) dato); break;
            default:
                System.out.println("Tipo de dato desconocido: " + dato.getClass().getSimpleName());
                break;
        }
    }

    /**
     * Ejecuta una instrucción DML segura en la BD
     * @throws SQLException si la ejecución falla
     */
    public void ejecutarDML() throws SQLException {
        st.executeUpdate();
    }
    /**
     * Ejecuta una instrucción SQL segura en la BD
     * @throws SQLException si la ejecución falla
     */
    public ResultSet ejecutarSQL() throws SQLException {
        return st.executeQuery();
    }

    /**
     * Ejecuta la instrucción DML dada
     * @param sql cadena con la instruccion
     * @throws SQLException error de SQL
     */
    private void ejecutar(String sql) throws SQLException {//no debe usarse en consultas de a deberas
        conexion.createStatement().executeUpdate(sql);
    }

    /**
     * Ejecuta la creacion de las tablas de la base de datos
     * @throws IOException si ocurre un error al acceder al script de creacion
     */
    public void ejecutarScriptInicial() throws IOException {
        StringBuilder scripo = new StringBuilder();
        Lector.abrirScript("bd");
        Lector.porCadaLinea(new Consumer<String>() {
            @Override
            public void accept(String line) {
                scripo.append(line);
                if (line.contains(";")){//fin de instruccion, ejecuta
                    scripo.deleteCharAt(scripo.length()-1);
                    System.out.println(scripo);
                    try {
                        ejecutar(scripo.toString());
                    } catch (SQLException e) {
                        System.out.println("La instruccion no se pudo completar ("+e.getErrorCode()+"): " + scripo.toString());
                    }
                    scripo.setLength(0);
                }
            }
        });
    }

    /**
     * Realiza una conexion a la instancia donde se almacenan usuarios y verifica las credenciales con los registros
     * @param usuario nombre de usuario
     * @param pass contraseña
     * @return true si existe la combinacion de credenciales, o false en caso contrario
     */
    public boolean comprobarCredenciales(String usuario, String pass){
        try {
            getConector().abrirConexionInstancia("SANTIAGO", "santiago", "Usuarios", "60000");
            ArrayList<ModeloBD> coincidencias = DAO.d.obtenerRegistros("Usuario", null,
                    new String[]{"nombre", "password"}, new Object[]{usuario, pass});

            return coincidencias.size() == 1;
        } catch (SQLException e) {
            System.out.println("ERROR AL COMPROBAR CREDENCIALES:");
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) throws SQLException {
        getConector().abrirConexionInstancia("SANTIAGO", "santiago", "Usuarios", "60000");
    }
}
