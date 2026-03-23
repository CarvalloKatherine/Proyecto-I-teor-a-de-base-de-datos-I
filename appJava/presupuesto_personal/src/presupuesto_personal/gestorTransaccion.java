package presupuesto_personal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public class gestorTransaccion {
    
    public boolean registrar(Transaccion t) {
        String sql = "{ call dba.sp_registrar_transaccion_completa(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        
        try (Connection con = Conexion.getConexion(); 
             CallableStatement cs = con.prepareCall(sql)) {
            
            if (con == null) return false;
            
            cs.setInt(1, t.getId_presupuesto_detalle());
            cs.setInt(2, t.getAnio());
            cs.setInt(3, t.getMes());
            cs.setString(4, t.getTipo_transaccion());
            cs.setString(5, t.getDescripcion());
            cs.setDouble(6, t.getMonto());
            cs.setDate(7, t.getFecha());
            cs.setString(8, t.getMetodo_pago());
            if(t.getNum_factura() != null){
            cs.setInt(9, t.getNum_factura());
             }else{
                cs.setNull(9, java.sql.Types.INTEGER);
            }
            
            cs.setString(10, t.getObservaciones());
            cs.setString(11, t.getCreado_por());
            if(t.getId_obligacion() != null){
            cs.setInt(12, t.getId_obligacion());
             }else{
                cs.setNull(12, java.sql.Types.INTEGER);
            }

            cs.execute();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; 
        }
    }
    
    public boolean validarVigencia(java.sql.Date fecha, int idPresupuesto) {
    String sql = "{ ? = call dba.fn_validar_vigencia_presupuesto(?, ?) }";
    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.registerOutParameter(1, java.sql.Types.INTEGER);
        cs.setDate(2, fecha);
        cs.setInt(3, idPresupuesto);
        cs.execute();
        int resultado = cs.getInt(1);
        if (resultado == 1) {
         return true;
        } else {
       return false;
        }

    } catch (SQLException ex) {
        System.out.println("Error validando vigencia: " + ex.getMessage());
        return false;
    }
}
    
    
    
}
