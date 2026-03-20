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
}
