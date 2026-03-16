package presupuesto_personal;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    public Connection conexion() {

        String url = "jdbc:sqlanywhere:DBF=C:\\Users\\Lenovo\\Desktop\\TeoriaBDatosI\\database\\DDL\\miproyecto.db;uid=dba;pwd=sql";

        try {
            return DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
