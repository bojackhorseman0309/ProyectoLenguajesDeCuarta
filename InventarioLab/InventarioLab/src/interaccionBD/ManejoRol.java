/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interaccionBD;

import Objetos.Rol;
import Objetos.Usuario;
import conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author alonso
 */
public class ManejoRol {
    
     static Connection con;
    static CallableStatement stmt;
    
     public static void crearRol(){
        con = Conexion.getConexion();
        stmt=null;

        String sql ="{call crearRol(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, Rol.getNomRol());
            stmt.setString(2, Rol.getDescRol());
            stmt.executeUpdate();
            
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                //e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Se ha creado el rol exitosamente");
        } 
    }
    
}
