package presupuesto_personal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class gestorUsuario {
    
    public String registrar(Usuario u) {
        String sql = "{ call dba.sp_insertar_usuario(?, ?, ?, ?, ?, ?, ?, ?) }";
        
        try (Connection con = Conexion.getConexion(); 
             CallableStatement cs = con.prepareCall(sql)) {
            
            if (con == null) return "No se pudo conectar.";
            
            cs.setString(1, u.getClave());
            cs.setString(2, u.getPrimerNombre());
            cs.setString(3, u.getSegundoNombre());
            cs.setString(4, u.getPrimerApellido());
            cs.setString(5, u.getSegundoApellido());
            cs.setString(6, u.getCorreo());
            cs.setDouble(7, u.getSalario());
            cs.setString(8, u.getCreadoPor());

            cs.execute();
            return "Usuario registrado correctamente.";

        } catch (SQLException ex) {
            return  ex.getMessage();
        }
    }
    
    public Usuario validarLogIn(String correo, String clave){
    Usuario usuarioLogueado = null;
    String sql = "{ call dba.sp_validar_login(?, ?) }";
        
    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return null;

        cs.setString(1, correo);
        cs.setString(2, clave);

        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                usuarioLogueado = new Usuario();
                usuarioLogueado.setIdUsuario(rs.getInt("id_usuario"));
                usuarioLogueado.setPrimerNombre(rs.getString("primer_nombre"));
                usuarioLogueado.setPrimerApellido(rs.getString("primer_apellido"));
                usuarioLogueado.setCorreo(rs.getString("correo_electronico"));
                usuarioLogueado.setSalario(rs.getDouble("salario_mensual_base"));
                
                if (usuarioLogueado.getCorreo().equals("admin@sistema.com")) {
                    usuarioLogueado.setRol("admin");
                } else {
                    usuarioLogueado.setRol("usuario");
                }
            }
        }
    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return usuarioLogueado;
    }
    
    public boolean updateUsuario(Usuario u){
        String sql = "{ call dba.sp_actualizar_usuario(?, ?, ?, ?, ?, ?, ?) }";
        
        try (Connection con = Conexion.getConexion(); 
             CallableStatement cs = con.prepareCall(sql)) {
            
            if (con == null) return false;
            
            cs.setInt(1, u.getIdUsuario());
            cs.setString(2, u.getPrimerNombre());
            cs.setString(3, u.getSegundoNombre());
            cs.setString(4, u.getPrimerApellido());
            cs.setString(5, u.getSegundoApellido());
            cs.setDouble(6, u.getSalario());
            cs.setString(7, u.getModificadoPor());

            int resultado = cs.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return  false;
        }
    }
    
    
    public boolean eliminarUsuario(int idUsuario, String modificadoPor) {
    String sql = "{ call dba.sp_eliminar_usuario(?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return false;

        cs.setInt(1, idUsuario);
        cs.setString(2, modificadoPor);
        cs.execute();
        return true;
    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
        return false;
    }
}

public Usuario consultarUsuario(int idUsuario) {
    String sql = "{ call dba.sp_consultar_usuario(?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return null;

        cs.setInt(1, idUsuario);
        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setPrimerNombre(rs.getString("primer_nombre"));
                u.setSegundoNombre(rs.getString("segundo_nombre"));
                u.setPrimerApellido(rs.getString("primer_apellido"));
                u.setSegundoApellido(rs.getString("segundo_apellido"));
                u.setCorreo(rs.getString("correo_electronico"));
                u.setSalario(rs.getDouble("salario_mensual_base"));
                return u;
            }
        }
    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return null;
}

public List<Usuario> listarUsuarios() {
    String sql = "{ call dba.sp_listar_usuarios() }";
    List<Usuario> lista = new ArrayList<>();

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) return lista;

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setPrimerNombre(rs.getString("primer_nombre"));
                u.setSegundoNombre(rs.getString("segundo_nombre"));
                u.setPrimerApellido(rs.getString("primer_apellido"));
                u.setSegundoApellido(rs.getString("segundo_apellido"));
                u.setCorreo(rs.getString("correo_electronico"));
                u.setSalario(rs.getDouble("salario_mensual_base"));
                lista.add(u);
            }
        }
    } catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    return lista;
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
