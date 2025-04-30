import com.ibm.db2.jcc.am.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Prueba {
    static Connection conexion = null;

    public static void main(String[] args) {
        try{
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            System.out.println("Driver prendido");

        } catch (ClassNotFoundException e) {
            System.out.println("No hay driver");
            return;
        }

        PreparedStatement ps = null;

        try{
            conexion = (Connection) DriverManager.getConnection("jdbc:db2:sample");

            if(conexion != null && !conexion.isClosed()){
                System.out.println("Conexion jalona");
            }else{
                System.out.println("Conexion balio pilin");
                return;
            }
        } catch (SQLException e) {
            System.out.println("trono la mendiga");
            return;
        }

        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println("balio pilin desconectadas: ");
            e.printStackTrace();
        }
    }
}
