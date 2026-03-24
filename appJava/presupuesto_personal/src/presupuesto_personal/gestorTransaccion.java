package presupuesto_personal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    
    public boolean actualizarTransaccion(int id, int anio, int mes, String descripcion,
                                     double monto, java.sql.Date fecha,
                                     String metodoPago, int numFactura,
                                     String observaciones, String modificadoPor){

    String sql = "{ call dba.sp_actualizar_transaccion(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return false;

        cs.setInt(1, id);
        cs.setInt(2, anio);
        cs.setInt(3, mes);
        cs.setString(4, descripcion);
        cs.setDouble(5, monto);
        cs.setDate(6, fecha);
        cs.setString(7, metodoPago);
        cs.setInt(8, numFactura);
        cs.setString(9, observaciones);
        cs.setString(10, modificadoPor);

        cs.execute();
        return true;

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
        return false;
    }
}

    public boolean eliminarTransaccion(int id){
    String sql = "{ call dba.sp_eliminar_transaccion(?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return false;

        cs.setInt(1, id);

        cs.execute();
        return true;

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
        return false;
    }
}

    public Transaccion consultarTransaccion(int id){
    String sql = "{ call dba.sp_consultar_transaccion(?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return null;

        cs.setInt(1, id);

        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                Transaccion t = new Transaccion();

                t.setId_transaccion(rs.getInt("id_transaccion"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setMonto(rs.getDouble("monto"));
                t.setFecha(rs.getDate("fecha"));
                t.setMetodo_pago(rs.getString("metodo_pago"));
                t.setObservaciones(rs.getString("observaciones"));

                return t;
            }
        }

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return null;
}

    public List<Transaccion> listarTransacciones(int idPresupuesto){
    List<Transaccion> lista = new ArrayList<>();
    String sql = "{ call dba.sp_listar_transacciones_presupuesto(?, ?, ?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return lista;

        cs.setInt(1, idPresupuesto);
        cs.setNull(2, java.sql.Types.INTEGER);
        cs.setNull(3, java.sql.Types.INTEGER);
        cs.setNull(4, java.sql.Types.VARCHAR);

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Transaccion t = new Transaccion();

                t.setId_transaccion(rs.getInt("id_transaccion"));
                t.setFecha(rs.getDate("fecha"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setMonto(rs.getDouble("monto"));
                t.setTipo_transaccion(rs.getString("tipo_transaccion"));

                lista.add(t);
            }
        }

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return lista;
}

    public List<Transaccion> listarTransaccionesCatalogo(){
    List<Transaccion> lista = new ArrayList<>();
    String sql = "{ call dba.sp_listar_transaccion_catalogo() }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return lista;

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Transaccion t = new Transaccion();

                t.setId_transaccion(rs.getInt("id_transaccion"));
                t.setFecha(rs.getDate("fecha"));
                t.setMonto(rs.getDouble("monto"));
                t.setDescripcion(rs.getString("descripcion"));

                lista.add(t);
            }
        }

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return lista;
}

    
    
}
