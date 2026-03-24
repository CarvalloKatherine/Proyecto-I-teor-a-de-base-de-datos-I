package presupuesto_personal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class gestorCategoria {
    public boolean registrarCategoria(Categoria c){
    String sql = "{ call dba.sp_insertar_categoria(?, ?, ?, ?, ?, ?, ?) }";
        
        try (Connection con = Conexion.getConexion(); 
             CallableStatement cs = con.prepareCall(sql)) {
            
            if (con == null) return false;
            
            cs.setString(1, c.getNombre_categoria());
            cs.setString(2, c.getDescripcion_detallada());
            cs.setString(3, c.getTipo_categoria());
            cs.setString(4, c.getNombre_icono());
            cs.setString(5, c.getColor_hex());
            cs.setString(6, c.getOrden_presentacion());
            cs.setString(7, c.getCreado_por());
            

            cs.execute();
            return true;

        } catch (SQLException ex) {
              ex.getMessage();
              return  false; 
        }
    }
    
    public List<Categoria> listarCategoriasParaCatalogo() {
    List<Categoria> lista = new ArrayList<>();
    String sql = "{ call dba.sp_listar_categorias_catalogo() }";

    try (Connection con = Conexion.getConexion(); 
         CallableStatement cs = con.prepareCall(sql)) {

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId_categoria(rs.getInt("id_categoria"));
                cat.setNombre_categoria(rs.getString("nombre_categoria"));
                cat.setTipo_categoria(rs.getString("tipo_categoria"));
                cat.setColor_hex(rs.getString("color_hex"));
                lista.add(cat);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return lista;
}
    
    public boolean actualizarCategoria(Categoria c) {
    String sql = "{ call dba.sp_actualizar_categoria(?, ?, ?, ?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return false;

        cs.setInt(1, c.getId_categoria());
        cs.setString(2, c.getNombre_categoria());
        cs.setString(3, c.getDescripcion_detallada());
        cs.setString(4, c.getColor_hex());
        cs.setString(5, c.getModificado_por());
        cs.execute();
        return true;

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
        return false;
    }
}

public boolean eliminarCategoria(int idCategoria) {
    String sql = "{ call dba.sp_eliminar_categoria(?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return false;

        cs.setInt(1, idCategoria);
        cs.execute();
        return true;

    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
        return false;
    }
}

public Categoria consultarCategoria(int idCategoria) {
    String sql = "{ call dba.sp_consultar_categoria(?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return null;

        cs.setInt(1, idCategoria);
        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                Categoria c = new Categoria();
                c.setId_categoria(rs.getInt("id_categoria"));
                c.setNombre_categoria(rs.getString("nombre_categoria"));
                c.setDescripcion_detallada(rs.getString("descripcion_detallada"));
                c.setTipo_categoria(rs.getString("tipo_categoria"));
                c.setNombre_icono(rs.getString("nombre_icono"));
                c.setColor_hex(rs.getString("color_hex"));
                c.setOrden_presentacion(rs.getString("orden_presentacion"));
                c.setCreado_por(rs.getString("creado_por"));
                return c;
            }
        }
    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return null;
}

public List<Categoria> listarCategorias(int idUsuario, String tipo) {
    String sql = "{ call dba.sp_listar_categorias(?, ?) }";
    List<Categoria> lista = new ArrayList<>();

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return lista;

        cs.setInt(1, idUsuario);
        cs.setString(2, tipo);
        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId_categoria(rs.getInt("id_categoria"));
                c.setNombre_categoria(rs.getString("nombre_categoria"));
                c.setDescripcion_detallada(rs.getString("descripcion_detallada"));
                c.setTipo_categoria(rs.getString("tipo_categoria"));
                c.setNombre_icono(rs.getString("nombre_icono"));
                c.setColor_hex(rs.getString("color_hex"));
                c.setOrden_presentacion(rs.getString("orden_presentacion"));
                c.setCreado_por(rs.getString("creado_por"));
                lista.add(c);
            }
        }
    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return lista;
}
    
    
    
    
}
