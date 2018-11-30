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
     
     /*  CREATE OR REPLACE PROCEDURE muestraTodoUsuariosEspec(correo in varchar2          
                                                                     ,todoUsuarioEspec OUT SYS_REFCURSOR) */
     
       public ArrayList<Usuario> buscaUsuarioEspec(String correo) {
        con = Conexion.getConexion();
        stmt = null;
        ArrayList<Usuario> listaUsuario = new ArrayList<Usuario>();
        String sql = "{call muestraTodoUsuariosEspec(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, correo);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.executeUpdate();
            ResultSet rs = (ResultSet) stmt.getObject(2);
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
       
          public void obtenerUsuarioEspec(JTable tabla, String correo) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        if (!correo.equalsIgnoreCase("")) {
            ArrayList<Usuario> lista = buscaUsuarioEspec(correo);
            Object[] linea = new Object[5];
            for (int i = 0; i < lista.size(); i++) {
                linea[0] = lista.get(i).getIdUsuario();
                linea[1] = lista.get(i).getNomUsuario();
                linea[2] = lista.get(i).getApelUsuario();
                linea[3] = lista.get(i).getCorreoUsuario();
                linea[4] = lista.get(i).getConUsuario();
                model.addRow(linea);
            }
        }else{
           JOptionPane.showMessageDialog(null, "Debe ingresar un correo para buscar", "Error", 0); 
        }
    }
          
            public ArrayList<Usuario> buscaUsuarioEspecNom(String nom) {
        con = Conexion.getConexion();
        stmt = null;
        ArrayList<Usuario> listaUsuario = new ArrayList<Usuario>();
        String sql = "{call muestraTodoUsuariosEspecNom(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, nom);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.executeUpdate();
            ResultSet rs = (ResultSet) stmt.getObject(2);
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
       
          public void obtenerUsuarioEspecNom(JTable tabla, String nom) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        if (!nom.equalsIgnoreCase("")) {
            ArrayList<Usuario> lista = buscaUsuarioEspecNom(nom);
            Object[] linea = new Object[5];
            for (int i = 0; i < lista.size(); i++) {
                linea[0] = lista.get(i).getIdUsuario();
                linea[1] = lista.get(i).getNomUsuario();
                linea[2] = lista.get(i).getApelUsuario();
                linea[3] = lista.get(i).getCorreoUsuario();
                linea[4] = lista.get(i).getConUsuario();
                model.addRow(linea);
            }
        }else{
           JOptionPane.showMessageDialog(null, "Debe ingresar un nombre para buscar", "Error", 0); 
        }
    }
          
            public ArrayList<Usuario> buscaUsuarioEspecApel(String apel) {
        con = Conexion.getConexion();
        stmt = null;
        ArrayList<Usuario> listaUsuario = new ArrayList<Usuario>();
        String sql = "{call muestraTodoUsuariosEspecApel(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, apel);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.executeUpdate();
            ResultSet rs = (ResultSet) stmt.getObject(2);
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
       
          public void obtenerUsuarioEspecApel(JTable tabla, String apel) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        if (!apel.equalsIgnoreCase("")) {
            ArrayList<Usuario> lista = buscaUsuarioEspecApel(apel);
            Object[] linea = new Object[5];
            for (int i = 0; i < lista.size(); i++) {
                linea[0] = lista.get(i).getIdUsuario();
                linea[1] = lista.get(i).getNomUsuario();
                linea[2] = lista.get(i).getApelUsuario();
                linea[3] = lista.get(i).getCorreoUsuario();
                linea[4] = lista.get(i).getConUsuario();
                model.addRow(linea);
            }
        }else{
           JOptionPane.showMessageDialog(null, "Debe ingresar un apellido para buscar", "Error", 0); 
        }
    }
          
          /*  create or replace procedure modificaClientes (idUsuario in number, nomCli in varchar2, apelCli in varchar2, correoCli in varchar2, conCli in varchar2) */

    public void editarUsuario(int id, String nombre, String apellido, String correo, String contra) {
        con = Conexion.getConexion();
        stmt = null;
        String sql = "{call modificaClientes(?,?,?,?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setInt(1, id);
            stmt.setString(2, nombre);
            stmt.setString(3, apellido);
            stmt.setString(4, correo);
            stmt.setString(5, contra);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Editado correctamente");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hubo un error, no se pudo editar", "Error", 0);
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
            }
        }
    }
    
     public void editaUsuarioTabla(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        try {
            String id = (String) model.getValueAt(tabla.getSelectedRow(), 0);
            String nombre = model.getValueAt(tabla.getSelectedRow(), 1).toString();
            String apellido = model.getValueAt(tabla.getSelectedRow(), 2).toString();
            String correo = model.getValueAt(tabla.getSelectedRow(), 3).toString();
            String contra = model.getValueAt(tabla.getSelectedRow(), 4).toString();
            editarUsuario(Integer.parseInt(id), nombre, apellido, correo, contra);
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para editar", "Error", 0);
        }
    }

     
     /*   
            create or replace procedure borraCliente (idUsuario IN number) */
     
    public void borraUsuario(int id) {
        con = Conexion.getConexion();
        stmt = null;
        String sql = "{call borraCliente(?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Borrado exitosamente");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hubo un error, no se pudo borrar", "Error", 0);
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
            }
        }
    }

    
     public void borraUsuarioTabla(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        try {
            String id = (String) model.getValueAt(tabla.getSelectedRow(), 0);
            borraUsuario(Integer.parseInt(id));
            model.removeRow(tabla.getSelectedRow());
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para borrar", "Error", 0);
        }
    }
    
}
