package presupuesto_personal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
      
      public boolean actualizarPresupuesto(int id, String nombre, int anioInicio, int mesInicio,
                                     int anioFin, int mesFin, String modificadoPor){
    String sql = "{ call dba.sp_actualizar_presupuesto(?, ?, ?, ?, ?, ?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return false;

        cs.setInt(1, id);
        cs.setString(2, nombre);
        cs.setInt(3, anioInicio);
        cs.setInt(4, mesInicio);
        cs.setInt(5, anioFin);
        cs.setInt(6, mesFin);
        cs.setString(7, modificadoPor);

        cs.execute();
        return true;

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
        return false;
    }
}

      public boolean eliminarPresupuesto(int id, String modificadoPor){
    String sql = "{ call dba.sp_eliminar_presupuesto(?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return false;

        cs.setInt(1, id);
        cs.setString(2, modificadoPor);

        cs.execute();
        return true;

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
        return false;
    }
}

      public Presupuesto consultarPresupuesto(int id){
    String sql = "{ call dba.sp_consultar_presupuesto(?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return null;

        cs.setInt(1, id);

        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                Presupuesto p = new Presupuesto();

                p.setId_presupuesto(rs.getInt("id_presupuesto"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setNombre_descriptivo(rs.getString("nombre_descriptivo"));
                p.setAnio_inicio(rs.getInt("anio_inicio"));
                p.setMes_inicio(rs.getInt("mes_inicio"));
                p.setAnio_fin(rs.getInt("anio_fin"));
                p.setMes_fin(rs.getInt("mes_fin"));
                p.setTotal_ingresos(rs.getDouble("total_ingresos"));
                p.setTotal_gastos(rs.getDouble("total_gastos"));
                p.setTotal_ahorro(rs.getDouble("total_ahorro"));
                p.setEstado_presupuesto(rs.getString("estado_presupuesto"));

                return p;
            }
        }

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return null;
}

      public List<Presupuesto> listarPresupuestosUsuario(int idUsuario, String estado){
    List<Presupuesto> lista = new ArrayList<>();
    String sql = "{ call dba.sp_listar_presupuestos_usuario(?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return lista;

        cs.setInt(1, idUsuario);
        cs.setString(2, estado);

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Presupuesto p = new Presupuesto();

                p.setId_presupuesto(rs.getInt("id_presupuesto"));
                p.setNombre_descriptivo(rs.getString("nombre_presupuesto"));
                p.setAnio_inicio(rs.getInt("anio_inicio"));
                p.setMes_inicio(rs.getInt("mes_inicio"));
                p.setEstado_presupuesto(rs.getString("estado_presupuesto"));

                lista.add(p);
            }
        }

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return lista;
}

      public List<Presupuesto> listarPresupuestosCatalogo(){
    List<Presupuesto> lista = new ArrayList<>();
    String sql = "{ call dba.sp_listar_Presupuestos_catalogo() }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return lista;

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Presupuesto p = new Presupuesto();

                p.setId_presupuesto(rs.getInt("id_presupuesto"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setNombre_descriptivo(rs.getString("nombre_descriptivo"));
                p.setAnio_inicio(rs.getInt("anio_inicio"));
                p.setMes_inicio(rs.getInt("mes_inicio"));
                p.setEstado_presupuesto(rs.getString("estado_presupuesto"));

                lista.add(p);
            }
        }

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return lista;
}


}
