/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interaccionBD;

import conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author mar_8
 */
public class LoginBD {

    Connection con;
    CallableStatement stmt;
    
    public LoginBD(){
   
    }

    public String validar(String user, String password) {
         con = Conexion.getConexion();
         stmt=null;
        String respuesta="";
        String sql ="{call verificaCon(?,?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, user);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, java.sql.Types.VARCHAR);
            stmt.executeUpdate();
            respuesta = stmt.getString(3);

        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                //e.printStackTrace();
            }
        }
        return respuesta;
    }
    
    public ArrayList<String> setUsuario(String correo){
        con = Conexion.getConexion();
        stmt=null;
        ArrayList<String> array=new ArrayList<String>();
        String usuario="";
        int iduser=0;
        int rol=0;
        String sql ="{call setUser(?,?,?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, correo);
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR);
            stmt.registerOutParameter(3, java.sql.Types.INTEGER);
            stmt.registerOutParameter(4, java.sql.Types.INTEGER);
            stmt.executeUpdate();
            usuario = stmt.getString(2);
            iduser=stmt.getInt(3); 
            rol=stmt.getInt(4); 
            
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
                //e.printStackTrace();
            }
        }
        array.add(usuario);
        array.add(String.valueOf(iduser));
        array.add(String.valueOf(rol));
        return array;       
    }
    
}
