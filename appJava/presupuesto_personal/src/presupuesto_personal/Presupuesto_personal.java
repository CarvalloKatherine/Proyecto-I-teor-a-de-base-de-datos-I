package presupuesto_personal;

import java.sql.Connection;

public class Presupuesto_personal {

    public static void main(String[] args) {
        Conexion c = new Conexion();
        Connection conn = c.conexion();

    if (conn != null) {
        System.out.println("yei");
    }
    }
    
}
