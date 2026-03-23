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
}
