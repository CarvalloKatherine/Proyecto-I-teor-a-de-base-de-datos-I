package presupuesto_personal;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class gestorObligacion {

    public int registrarObligacion(Obligación o) {
    String sqlInsert = "{ call dba.sp_insertar_obligacion(?, ?, ?, ?, ?, ?, ?, ?) }";
    String sqlId = "{ ? = call dba.fn_ultimo_id_obligacion() }";
    
    try (Connection con = Conexion.getConexion()) { // ← una sola conexión
        
        try (CallableStatement cs = con.prepareCall(sqlInsert)) {
            cs.setInt(1, o.getId_subcategoria());
            cs.setString(2, o.getNombre_obligacion());
            cs.setString(3, o.getDescripcion());
            cs.setBigDecimal(4, BigDecimal.valueOf(o.getMonto_fijo_mensual()));
            cs.setInt(5, o.getDia());
            cs.setDate(6, o.getFecha_inicio());
            if (o.getFecha_fin() != null) cs.setDate(7, o.getFecha_fin());
            else cs.setNull(7, java.sql.Types.DATE);
            cs.setString(8, o.getCreado_por());
            cs.execute();
        }

        try (CallableStatement cs = con.prepareCall(sqlId)) {
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.execute();
            return cs.getInt(1);
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        return -1;
    }
}
    
    public List<Obligación> listarObligaciones(String correo, boolean activo) {
        List<Obligación> lista = new ArrayList<>();
        String sql = "{ call dba.sp_listar_obligaciones_usuario(?, ?) }";

        try (Connection con = Conexion.getConexion(); 
             CallableStatement cs = con.prepareCall(sql)) {
                if (con == null) return lista;
                cs.setString(1, correo);
                cs.setBoolean(2, activo);
            
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    
                    Obligación o = new Obligación();
                    o.setId_obligacion(rs.getInt("id_obligacion"));
                    o.setNombre_obligacion(rs.getString("nombre_obligacion"));
                    o.setMonto_fijo_mensual(rs.getDouble("monto_fijo_mensual"));
                    o.setDia(rs.getInt("dia"));
                    o.setNombreSubcategoria(rs.getString("nombre_subcategoria"));
                    lista.add(o);
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return lista;
    }
    
     public boolean actualizarObligacion(Obligación o) {
        String sql = "{ call dba.sp_actualizar_obligacion(?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection con = Conexion.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            if (con == null) return false;

            cs.setInt(1, o.getId_obligacion());
            cs.setString(2, o.getNombre_obligacion());
            cs.setString(3, o.getDescripcion());
            cs.setDouble(4, o.getMonto_fijo_mensual());
            cs.setInt(5, o.getDia());
            cs.setDate(6, o.getFecha_fin());
            cs.setBoolean(7, o.isVigente());
            cs.setString(8, o.getModificado_por());

            cs.execute();
            return true;

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }
     
     public boolean eliminarObligacion(int idObligacion, String modificadoPor) {
        String sql = "{ call dba.sp_eliminar_obligacion(?, ?) }";

        try (Connection con = Conexion.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            if (con == null) return false;

            cs.setInt(1, idObligacion);
            cs.setString(2, modificadoPor);

            cs.execute();
            return true;

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

     public Obligación consultarObligacion(int idObligacion) {
        String sql = "{ call dba.sp_consultar_obligacion(?) }";

        try (Connection con = Conexion.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            if (con == null) return null;

            cs.setInt(1, idObligacion);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    Obligación o = new Obligación();

                    o.setId_obligacion(rs.getInt("id_obligacion"));
                    o.setId_subcategoria(rs.getInt("id_subcategoria"));
                    o.setNombre_obligacion(rs.getString("nombre_obligacion"));
                    o.setDescripcion(rs.getString("descripcion"));
                    o.setMonto_fijo_mensual(rs.getDouble("monto_fijo_mensual"));
                    o.setDia(rs.getInt("dia"));
                    o.setVigente(rs.getBoolean("vigente"));
                    o.setFecha_inicio(rs.getDate("fecha_inicio"));
                    o.setFecha_fin(rs.getDate("fecha_fin"));

                    return o;
                }
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }

     public List<Obligación> listarObligacionesUsuario(String correo, boolean activo) {
        List<Obligación> lista = new ArrayList<>();
        String sql = "{ call dba.sp_listar_obligaciones_usuario(?, ?) }";

        try (Connection con = Conexion.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            if (con == null) return lista;

            cs.setString(1, correo);
            cs.setBoolean(2, activo);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Obligación o = new Obligación();

                    o.setId_obligacion(rs.getInt("id_obligacion"));
                    o.setNombre_obligacion(rs.getString("nombre_obligacion"));
                    o.setMonto_fijo_mensual(rs.getDouble("monto_fijo_mensual"));
                    o.setDia(rs.getInt("dia"));

                    lista.add(o);
                }
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return lista;
    }

     public List<Obligación> listarCatalogoObligaciones() {
        List<Obligación> lista = new ArrayList<>();
        String sql = "{ call dba.sp_listar_obligacion_catalogo() }";

        try (Connection con = Conexion.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            if (con == null) return lista;

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Obligación o = new Obligación();

                    o.setId_obligacion(rs.getInt("id_obligacion"));
                    o.setNombre_obligacion(rs.getString("nombre_obligacion"));
                    o.setMonto_fijo_mensual(rs.getDouble("monto_fijo_mensual"));
                    o.setVigente(rs.getBoolean("vigente"));

                    lista.add(o);
                }
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return lista;
    }

    
}