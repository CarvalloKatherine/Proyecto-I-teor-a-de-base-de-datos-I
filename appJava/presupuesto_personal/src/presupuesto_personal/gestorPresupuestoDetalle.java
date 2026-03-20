package presupuesto_personal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public class gestorPresupuestoDetalle {
    public boolean registrarPresupuestoDetalle(PresupuestoDetalle pd){
    String sql = "{ call dba.sp_insertar_presupuesto_detalle(?, ?, ?, ?, ?) }";
        
        try (Connection con = Conexion.getConexion(); 
             CallableStatement cs = con.prepareCall(sql)) {
            
            if (con == null) return false;
            
            cs.setInt(1, pd.getId_presupuesto());
            cs.setInt(2, pd.getId_subcategoria());
            cs.setDouble(3, pd.getMonto_mensual_asignado());
            cs.setString(4, pd.getObservaciones());
            cs.setString(5, pd.getObservaciones());

            cs.execute();
            return true;

        } catch (SQLException ex) {
              ex.getMessage();
              return  false; 
        }
    }
}
