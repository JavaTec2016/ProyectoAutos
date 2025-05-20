package conexion;

import ErrorHandlin.ErrorHandler;
import Instalador.Config;
import Instalador.DB2Ejecutor;
import Instalador.Install;
import Modelo.ModeloBD;
import Modelo.Userio;
import com.ibm.db2.jcc.am.Connection;
import controlador.DAO;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class ConexionBD {
    static Connection conexion = null;
    static ConexionBD conector = null;
    public PreparedStatement st = null;

    private Userio usr;
    private ConexionBD(){}

    /**
     * Recupera el usuario actual de la sesión
     * @return usuario en sesión
     */
    public Userio getUsr() {
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
            conexion = (Connection) DriverManager.getConnection("jdbc:db2://localhost:50000/"+BD, p);

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
            String url = "jdbc:db2://127.0.0.1:"+puerto+"/"+BD;
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
            abrirConexionInstancia(usuario, pass, Userio.class.getSimpleName(), puerto);
            ResultSet rs = DAO.d.consultar("Usuarios", null, new String[]{"nombre", "password"}, new Object[]{usuario, pass});
            boolean st = rs.next();

            cerrarConexion();
            if(st) return ErrorHandler.OK;
            return ErrorHandler.LOGIN_NO_USER;

        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }
    public int probarCredenciales(String usuario, String pass){
        try {
            abrirConexion(Config.USER, Config.PASS, Userio.class.getSimpleName());
            ArrayList<ModeloBD> regs = DAO.d.obtenerRegistros(Userio.class.getSimpleName(), null, new String[]{"nombre", "password"}, new Object[]{usuario, pass});
            cerrarConexion();
            if(!regs.isEmpty()) return ErrorHandler.OK;
            return ErrorHandler.LOGIN_NO_USER;
        } catch (SQLException e) {
            e.printStackTrace();
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
    public Userio obtenerUsuario(String usuario, String pass) throws SQLException {
        abrirConexion(Config.USER, Config.PASS, Userio.class.getSimpleName());
        ArrayList<ModeloBD> ar = DAO.d.obtenerRegistros(Userio.class.getSimpleName(), null, new String[]{"nombre", "password"}, new Object[]{usuario, pass});
        cerrarConexion();
        return (Userio)ar.get(0);
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
    public int abrirConexion2(String usuario,String pass, String BD){
        int ocurrencia = probarCredenciales(usuario, pass);

        if(ocurrencia==ErrorHandler.OK){
            try {
                usr = obtenerUsuario(usuario, pass);
                abrirConexion(Config.USER, Config.PASS, BD);
                setAutoCommit(false);
            } catch (SQLException e) {
                ocurrencia = e.getErrorCode();
                e.printStackTrace();
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
            usr = null;
            setAutoCommit(true);
            System.out.println("Conexion cerrada");
            if(st != null) st.close();
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
    public static Connection getConexion() {
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
        if(dato == null) {
            //System.out.println("CONEXION PREPARED NULO " + idx);
            st.setObject(idx, null);
            return;
        };
        switch (dato.getClass().getSimpleName()){
            case "Integer": st.setInt(idx, (Integer)dato); break;
            case "Short": st.setShort(idx, (Short) dato); break;
            case "Byte": st.setByte(idx, (Byte)dato); break;
            case "String": st.setString(idx, (String)dato); break;
            case "Date": st.setDate(idx, (Date)dato); break;
            case "BigDecimal":
                System.out.println("COLOCA EN POS: " + idx + ": " + dato);
                st.setBigDecimal(idx, (BigDecimal)dato); break;
            case "Float": st.setFloat(idx, (Float)dato); break;
            case "Double": st.setDouble(idx, (Double) dato); break;
            case "Boolean": st.setBoolean(idx, (Boolean)dato); break;
            default:
                System.out.println("PREPARED STATEMENT Tipo de dato desconocido: " + dato.getClass().getSimpleName());
                st.setObject(idx, dato);
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
     * Ejecuta scripts para crear la instancia de usuarios si no existe y cargar las bases de datos necesarias en sus respectivas instancias
     * @throws IOException Si falla la lectura de un script
     * @throws InterruptedException si algun proceso de inicializacion es cerrado repentinamente
     */
    public void inicializar() throws IOException, InterruptedException {
        Install.crearInstanciaUsuario();

    }
    public boolean autoInstalar() throws IOException, InterruptedException {
        try {
            abrirConexion(Config.USER, Config.PASS, Userio.class.getSimpleName());
            cerrarConexion();
            abrirConexion(Config.USER, Config.PASS, "Autos");
            cerrarConexion();
        } catch (SQLException e) {
            DB2Ejecutor.instalarBasesSencillo();
            return true;
        }
        return false;
    }
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        //getConector().autoInstalar();
        DB2Ejecutor.instalarBasesSencillo();
        //getConector().abrirConexionInstancia(Config.USER, Config.PASS, Userio.class.getSimpleName(), Config.INSTANCIA_USUARIOS_PUERTO);
    }
}
