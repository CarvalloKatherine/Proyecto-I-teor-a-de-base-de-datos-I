package presupuesto_personal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;


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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
