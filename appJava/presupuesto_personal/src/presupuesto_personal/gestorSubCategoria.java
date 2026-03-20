package presupuesto_personal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.List;     
import java.util.ArrayList; 

public class gestorSubCategoria {
    public boolean registrarCategoria(SubCategoria sc){
    String sql = "{ call dba.sp_insertar_subcategoria(?, ?, ?, ?, ?) }";
        
        try (Connection con = Conexion.getConexion(); 
             CallableStatement cs = con.prepareCall(sql)) {
            
            if (con == null) return false;
            
            cs.setInt(1, sc.getId_categoria());
            cs.setString(2, sc.getNombre_subcategoria());
            cs.setString(3, sc.getDescripcion_detallada_sub());
            cs.setString(5, sc.getCreado_por());
            cs.setBoolean(4, sc.isPor_defecto());
            

            cs.execute();
            return true;

        } catch (SQLException ex) {
              ex.getMessage();
              return  false; 
        }
    }
    
    public List<SubCategoria> listarSubcategoriasPorCategoria(int idCategoria) {
        List<SubCategoria> lista = new ArrayList<>();
        String sql = "{ call dba.sp_listar_subcategorias_por_categoria(?) }";

        try (Connection con = Conexion.getConexion(); 
             CallableStatement cs = con.prepareCall(sql)) {

            if (con == null) return lista;

            
            cs.setInt(1, idCategoria);

            
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    
                    SubCategoria sc = new SubCategoria();
                    sc.setId_subcategoria(rs.getInt("id_subcategoria"));
                    sc.setId_categoria(rs.getInt("id_categoria"));
                    sc.setNombre_subcategoria(rs.getString("nombre_subcategoria"));
                    sc.setDescripcion_detallada_sub(rs.getString("descripcion_detallada_sub"));
                    sc.setEstado_sub(rs.getBoolean("estado_sub"));
                    sc.setPor_defecto(rs.getBoolean("por_defecto"));
                    sc.setCreado_por(rs.getString("creado_por"));
                    sc.setCreado_en(rs.getDate("creado_en"));
                    lista.add(sc);
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return lista;
    }
    
}
