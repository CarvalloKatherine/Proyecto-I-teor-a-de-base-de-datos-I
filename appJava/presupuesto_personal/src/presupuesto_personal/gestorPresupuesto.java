package presupuesto_personal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class gestorPresupuesto {
    
    
    public int registrarPresupuesto(Presupuesto p){
        String sql = "{ call dba.sp_insertar_presupuesto(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        
        try (Connection con = Conexion.getConexion(); 
             CallableStatement cs = con.prepareCall(sql)) {
            
            if (con == null) return -1;
            
            cs.setInt(1, p.getId_usuario());
            cs.setString(2, p.getNombre_descriptivo());
            cs.setInt(3, p.getAnio_inicio());
            cs.setInt(4, p.getMes_inicio());
            cs.setInt(5, p.getAnio_fin());
            cs.setInt(6, p.getMes_fin());
            cs.setDouble(7, p.getTotal_ingresos());
            cs.setDouble(8, p.getTotal_gastos());
            cs.setDouble(9, p.getTotal_ahorro());
            cs.setString(10, p.getCreado_por());
            cs.registerOutParameter(11, java.sql.Types.INTEGER);

            cs.execute();
            return cs.getInt(11);

        } catch (SQLException ex) {
            System.out.println("Error al registrar presupuesto: " + ex.getMessage());
            return -1; 
        }
    }
    
      public Presupuesto obtenerPresupuesto(int idPresupuesto) {
    String sql = "{ call dba.sp_consultar_presupuesto(?) }";
    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idPresupuesto);
        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                Presupuesto p = new Presupuesto();
                p.setMes_inicio(rs.getInt("mes_inicio"));
                p.setAnio_inicio(rs.getInt("anio_inicio"));
                p.setMes_fin(rs.getInt("mes_fin"));
                p.setAnio_fin(rs.getInt("anio_fin"));
                return p;
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return null;
}
  

}
