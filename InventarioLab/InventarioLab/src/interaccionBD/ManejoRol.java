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
public class ManejoRol {
    
     Connection con;
     CallableStatement stmt;
    
     public void crearRol(String nomRol, String descrip){
        con = Conexion.getConexion();
        stmt=null;

        String sql ="{call crearRol(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, nomRol);
            stmt.setString(2, descrip);
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
     
     
     
      /*   create or replace procedure spVerifNomRol (nomRol in varchar2, msj out varchar2) */
     
       public void verificaRol(String nomRol, String descrip){
        con = Conexion.getConexion();
        stmt=null; String verif=" ";

        String sql ="{call spVerifNomRol(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, nomRol);
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR);
            stmt.executeUpdate();
            verif=stmt.getString(2);
            if (verif.equals("false")) {
                crearRol(nomRol, descrip);
            } else{
                    JOptionPane.showMessageDialog(null, "Ya hay un rol con estos datos");
            }
            
            
            
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
       
   
     
     
     /*  CREATE OR REPLACE PROCEDURE muestraTodoRol(          
                    todoRol OUT SYS_REFCURSOR)AS     */
     
     public ArrayList<Rol> buscaTodoRol() {
        con = Conexion.getConexion();
        stmt = null;
        ArrayList<Rol> listaRol = new ArrayList<Rol>();
        String sql = "{call muestraTodoRol(?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.executeUpdate();
            ResultSet rs = (ResultSet) stmt.getObject(1);
            Rol rol;
            while (rs.next()) {
                rol = new Rol();
                rol.setIdRol(rs.getString("id_rol"));
                rol.setNomRol(rs.getString("nombre_rol"));
                rol.setDescRol(rs.getString("descripcion_rol"));
                listaRol.add(rol);
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
        return listaRol;
    }
    
     public void obtenerRoles(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        ArrayList<Rol> lista = buscaTodoRol();
        Object[] linea = new Object[3];
        for (int i = 0; i < lista.size(); i++) {
            linea[0] = lista.get(i).getIdRol();
            linea[1] = lista.get(i).getNomRol();
            linea[2] = lista.get(i).getDescRol();
            model.addRow(linea);
        }
    }
     
     /* CREATE OR REPLACE PROCEDURE muestraTodoRolEspec(nomRol in varchar2,         
                    todoRolEspec OUT SYS_REFCURSOR) */
     
             public ArrayList<Rol> buscarRolEspec(String nomRol) {
        con = Conexion.getConexion();
        stmt = null;
        ArrayList<Rol> listaRol = new ArrayList<Rol>();
        String sql = "{call muestraTodoRolEspec(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, nomRol);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.executeUpdate();
            ResultSet rs = (ResultSet) stmt.getObject(2);
            Rol rol;
            while (rs.next()) {
                rol = new Rol();
                rol.setIdRol(rs.getString("id_rol"));
                rol.setNomRol(rs.getString("nombre_rol"));
                rol.setDescRol(rs.getString("descripcion_rol"));
                listaRol.add(rol);
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
        return listaRol;
    }
       
          public void obtenerRolEspec(JTable tabla, String nom) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        if (!nom.equalsIgnoreCase("")) {
            ArrayList<Rol> lista = buscarRolEspec(nom);
            Object[] linea = new Object[3];
            for (int i = 0; i < lista.size(); i++) {
                linea[0] = lista.get(i).getIdRol();
                 linea[1] = lista.get(i).getNomRol();
                 linea[2] = lista.get(i).getDescRol();
                model.addRow(linea);
            }
        }else{
           JOptionPane.showMessageDialog(null, "Debe ingresar un nombre para buscar", "Error", 0); 
        }
    }
          
          /*   create or replace procedure modificarRol (idRol in number, nombreRol in varchar2, descripcionRol in varchar2) */

    public void editarRol(int id, String nombre, String desc) {
        con = Conexion.getConexion();
        stmt = null;
        String sql = "{call modificarRol(?,?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setInt(1, id);
            stmt.setString(2, nombre);
            stmt.setString(3, desc);
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
    
    
      public void verificaRolEditado(int id, String nombre, String desc){
        con = Conexion.getConexion();
        stmt=null; String verif=" ";

        String sql ="{call spVerifNomRol(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, nombre);
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR);
            stmt.executeUpdate();
            verif=stmt.getString(2);
            if (verif.equals("false")) {
                editarRol(id, nombre, desc);
            } else{
                    JOptionPane.showMessageDialog(null, "Ya hay un rol con estos datos");
            }
            
            
            
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
    
     public void editaRolTabla(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        try {
            String id = (String) model.getValueAt(tabla.getSelectedRow(), 0);
            String nombre = model.getValueAt(tabla.getSelectedRow(), 1).toString();
            String desc = model.getValueAt(tabla.getSelectedRow(), 2).toString();
            verificaRolEditado(Integer.parseInt(id), nombre, desc);
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para editar", "Error", 0);
        }
    }

     
     /*   
            create or replace procedure borrarRol (idRol in number) */
     
    public void borraRol(int id) {
        con = Conexion.getConexion();
        stmt = null;
        String sql = "{call borrarRol(?)}";
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

    
     public void borraRolTabla(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        try {
            String id = (String) model.getValueAt(tabla.getSelectedRow(), 0);
            borraRol(Integer.parseInt(id));
            model.removeRow(tabla.getSelectedRow());
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para borrar", "Error", 0);
        }
    }
    
}
