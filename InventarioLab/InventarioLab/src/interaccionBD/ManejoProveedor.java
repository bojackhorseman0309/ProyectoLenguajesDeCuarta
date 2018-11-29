/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interaccionBD;

import Objetos.Proveedor;
import Objetos.Rol;
import conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author alonso
 */
public class ManejoProveedor {
    
     static Connection con;
    static CallableStatement stmt;
    
     public static void crearProveedor(){
        con = Conexion.getConexion();
        stmt=null;

        String sql ="{call insertaProveedores(?,?, ?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, Proveedor.getNomProv());
            stmt.setString(2, Proveedor.getTelProv());
            stmt.setString(3, Proveedor.getCorreoProv());
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
            JOptionPane.showMessageDialog(null, "Se ha creado el proveedor exitosamente");
        } 
    }
    
}
