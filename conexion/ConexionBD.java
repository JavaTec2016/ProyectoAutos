package conexion;

import com.ibm.db2.jcc.am.Connection;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;

public class ConexionBD {
    static Connection conexion = null;
    static ConexionBD conector = null;
    static PreparedStatement st = null;

    //usuario y esas cosas

    private ConexionBD(){

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
    public void cerrarConexion() throws SQLException {
        if(conexion != null && !conexion.isClosed()){
            System.out.println("Conexion cerrada");
            conexion.close();
        }
    }

    public static ConexionBD getConector() {
        if(conector == null) conector = new ConexionBD();
        return conector;
    }

    public static Connection getConexion() {
        return conexion;
    }
    public void prepararStatement(String sql, Object[] datos) throws SQLException {
        st = conexion.prepareStatement(sql);

        for (int i = 0; i < datos.length; i++) {
            colocarPrepared(i+1, datos[i]);
        }
    }
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
    public void ejecutarDML() throws SQLException {
        st.executeUpdate();
    }
    public ResultSet ejecutarSQL() throws SQLException {
        return st.executeQuery();
    }
    private void ejecutar(String sql) throws SQLException {//no debe usarse en consultas de a deberas
        conexion.createStatement().executeUpdate(sql);
    }
    public void ejecutarScriptInicial() throws IOException {
        String path = new File("").getAbsolutePath();
        BufferedReader r = new BufferedReader(new FileReader(path.concat("/src/sql/bd.sql")));
        String line = "";
        StringBuilder scripo = new StringBuilder();

        while((line = r.readLine())!=null){//saca linea por linea
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

    }
}
