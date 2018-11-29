/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interaccionBD;

import Objetos.Usuario;
import conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author alonso
 */
public class ManejoUsuario {
    
     Connection con;
     CallableStatement stmt;
    
    /*   create or replace procedure creaClientes (idUsuario IN number, nomCli in varchar2, 
                                                        apelCli in varchar2, correoCli in varchar2,
                                                        conCli in varchar2) */
    
     public void setUsuario(String nomUsuario, String apelUsuario, String correo, String contra){
        con = Conexion.getConexion();
        stmt=null;

        String sql ="{call creaClientes(?,?,?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, nomUsuario);
            stmt.setString(2, apelUsuario);
            stmt.setString(3, correo);
            stmt.setString(4, contra);
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
            JOptionPane.showMessageDialog(null, "Se ha creado el usuario exitosamente");
        } 
    }
     
     /*   create or replace procedure SpSacaId (correo in varchar2,idUsuario out int) */
     
      public void sacaId(String correo, int idRol){
        con = Conexion.getConexion();
        stmt=null;
        int idUsuario=0;
        String sql ="{call SpSacaId(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, correo);
            stmt.registerOutParameter(2, java.sql.Types.INTEGER);
            stmt.executeUpdate();
            idUsuario=stmt.getInt(2); 
            asignaRol(idRol, idUsuario);
            
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
    }
     
     /*   create or replace procedure asignaRol (idUsuario in number, idRol in number)  */
     
     public void asignaRol (int idRol, int idUsuario){
          con = Conexion.getConexion();
        stmt=null;

        String sql ="{call asignaRol(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idRol);
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
        } 
     }
     
     
    public ArrayList<Usuario> buscaTodoUsuario() {
        con = Conexion.getConexion();
        stmt = null;
        ArrayList<Usuario> listaUsuario = new ArrayList<Usuario>();
        String sql = "{call muestraTodoUsuarios(?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.executeUpdate();
            ResultSet rs = (ResultSet) stmt.getObject(1);
            Usuario usuario;
            while (rs.next()) {
                usuario = new Usuario();
                usuario.setNomUsuario(rs.getString("nombre_usuario"));
                usuario.setIdUsuario(rs.getString("id_usuario"));
                usuario.setApelUsuario(rs.getString("apellidos_usuario"));
                usuario.setCorreoUsuario(rs.getString("correo_usuario"));
                usuario.setConUsuario(rs.getString("contrasena"));
                listaUsuario.add(usuario);
            }
            rs.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hubo un error, no se pudo buscar", "Error", 0);
            return null;
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
            }
        }
        return listaUsuario;
    }
    
     public void obtenerUsuarios(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        ArrayList<Usuario> lista = buscaTodoUsuario();
        Object[] linea = new Object[5];
        for (int i = 0; i < lista.size(); i++) {
            linea[0] = lista.get(i).getIdUsuario();
            linea[1] = lista.get(i).getNomUsuario();
            linea[2] = lista.get(i).getApelUsuario();
            linea[3] = lista.get(i).getCorreoUsuario();
            linea[4] = lista.get(i).getConUsuario();
            model.addRow(linea);
        }
    }
    
}
