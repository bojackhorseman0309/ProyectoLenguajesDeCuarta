/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mar_8
 */
public class Conexion{
    
    
    private static String url = "jdbc:oracle:thin:@localhost:1521:XE";    
    private static String driverName = "oracle.jdbc.OracleDriver";   
    private static String username="hr";   
    private static String password="hr";
    private static Connection con;
    
    public static Connection getConexion() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println("Usuario o contraeña incorrectos"); 
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Error de conexión a la base de datos"); 
        }
        return con;
    }
    
    
}
