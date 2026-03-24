package presupuesto_personal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
            cs.setString(5, pd.getCreado_por());

            cs.execute();
            return true;

        } catch (SQLException ex) {
              ex.getMessage();
              return  false; 
        }
    }
    
    public int obtenerIdDetalle(int idPresupuesto, int idSubcategoria) {
    String sql = "{ ? = call dba.fn_obtener_id_detalle(?, ?) }";
    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {
        cs.registerOutParameter(1, java.sql.Types.INTEGER); 
        cs.setInt(2, idPresupuesto);
        cs.setInt(3, idSubcategoria);
        cs.execute();
        return cs.getInt(1);

    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
        return -1;
    }}
    
    public boolean actualizarDetalle(int idDetalle, double monto, String observaciones, String modificadoPor){
    String sql = "{ call dba.sp_actualizar_presupuesto_detalle(?, ?, ?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return false;

        cs.setInt(1, idDetalle);
        cs.setDouble(2, monto);
        cs.setString(3, observaciones);
        cs.setString(4, modificadoPor);

        cs.execute();
        return true;

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
        return false;
    }
}

    public boolean eliminarDetalle(int idDetalle){
    String sql = "{ call dba.sp_eliminar_presupuesto_detalle(?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return false;

        cs.setInt(1, idDetalle);

        cs.execute();
        return true;

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
        return false;
    }
}
public PresupuestoDetalle consultarDetalle(int idDetalle){
    String sql = "{ call dba.sp_consultar_presupuesto_detalle(?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return null;

        cs.setInt(1, idDetalle);

        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                PresupuestoDetalle pd = new PresupuestoDetalle();

                pd.setId_presupuesto_detalle(rs.getInt("id_presupuesto_detalle"));
                pd.setMonto_mensual_asignado(rs.getDouble("monto_mensual_asignado"));
                pd.setObservaciones(rs.getString("observaciones"));

                return pd;
            }
        }

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return null;
}

public List<PresupuestoDetalle> listarDetalles(int idPresupuesto){
    List<PresupuestoDetalle> lista = new ArrayList<>();
    String sql = "{ call dba.sp_listar_detalles_presupuesto(?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return lista;

        cs.setInt(1, idPresupuesto);

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                PresupuestoDetalle pd = new PresupuestoDetalle();

                pd.setId_presupuesto_detalle(rs.getInt("id_presupuesto_detalle"));
                pd.setMonto_mensual_asignado(rs.getDouble("monto_mensual_asignado"));
                pd.setObservaciones(rs.getString("observaciones"));

                lista.add(pd);
            }
        }

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return lista;
}

    public List<PresupuestoDetalle> listarDetallesCatalogo(){
    List<PresupuestoDetalle> lista = new ArrayList<>();
    String sql = "{ call dba.sp_listar_detalle_catalogo() }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return lista;

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                PresupuestoDetalle pd = new PresupuestoDetalle();

                pd.setId_presupuesto_detalle(rs.getInt("id_presupuesto_detalle"));
                pd.setId_presupuesto(rs.getInt("id_presupuesto"));
                pd.setId_subcategoria(rs.getInt("id_subcategoria"));
                pd.setMonto_mensual_asignado(rs.getDouble("monto_mensual_asignado"));

                lista.add(pd);
            }
        }

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return lista;
}

  
}
