/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interaccionBD;

import Objetos.Proveedor;
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
public class ManejoProveedor {
    
     Connection con;
     CallableStatement stmt;
    
     public void crearProveedor(String nomProv, String telProv, String correo){
        con = Conexion.getConexion();
        stmt=null;

        String sql ="{call insertaProveedores(?,?, ?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, nomProv);
            stmt.setString(2, telProv);
            stmt.setString(3, correo);
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
     
     
      /*    create or replace procedure spVerifNomProv (nomProv in varchar2, msj out varchar2) */
     
       public void verificaProveedor(String nomProv, String telProv, String correo){
        con = Conexion.getConexion();
        stmt=null; String verif=" ";

        String sql ="{call spVerifNomProv(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, nomProv);
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR);
            stmt.executeUpdate();
            verif=stmt.getString(2);
            if (verif.equals("false")) {
                crearProveedor(nomProv, telProv, correo);
            } else{
                    JOptionPane.showMessageDialog(null, "Ya hay un Proveedor con estos datos");
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

     /*  CREATE OR REPLACE PROCEDURE muestraTodoProv(todoProv OUT SYS_REFCURSOR) */
     
       public ArrayList<Proveedor> buscaTodoProv() {
        con = Conexion.getConexion();
        stmt = null;
        ArrayList<Proveedor> listaProv = new ArrayList<Proveedor>();
        String sql = "{call muestraTodoProv(?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.executeUpdate();
            ResultSet rs = (ResultSet) stmt.getObject(1);
            Proveedor prov;
            while (rs.next()) {
                prov = new Proveedor();
                prov.setIdProv(rs.getString("id_proveedor"));
                prov.setNomProv(rs.getString("nombre_proveedor"));
                prov.setCorreoProv(rs.getString("correo_proveedor"));
                prov.setTelProv(rs.getString("telefono_proveedor"));
                listaProv.add(prov);
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
        return listaProv;
    }
    
     public void obtenerProv(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        ArrayList<Proveedor> lista = buscaTodoProv();
        Object[] linea = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            linea[0] = lista.get(i).getIdProv();
            linea[1] = lista.get(i).getNomProv();
            linea[2] = lista.get(i).getTelProv();
            linea[3] = lista.get(i).getCorreoProv();
            model.addRow(linea);
        }
    }
     
     /*      
            CREATE OR REPLACE PROCEDURE muestraTodoProvEspec(nomProv in varchar2, todoProvEspec OUT SYS_REFCURSOR) */
     
             public ArrayList<Proveedor> buscarProvEspec(String nom) {
        con = Conexion.getConexion();
        stmt = null;
        ArrayList<Proveedor> listaProv = new ArrayList<Proveedor>();
        String sql = "{call muestraTodoProvEspec(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, nom);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.executeUpdate();
            ResultSet rs = (ResultSet) stmt.getObject(2);
            Proveedor prov;
            while (rs.next()) {
               prov = new Proveedor();
                prov.setIdProv(rs.getString("id_proveedor"));
                prov.setNomProv(rs.getString("nombre_proveedor"));
                prov.setCorreoProv(rs.getString("correo_proveedor"));
                prov.setTelProv(rs.getString("telefono_proveedor"));
                listaProv.add(prov);
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
        return listaProv;
    }
       
          public void obtenerProvEspec(JTable tabla, String nom) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        if (!nom.equalsIgnoreCase("")) {
            ArrayList<Proveedor> lista = buscarProvEspec(nom);
            if (buscarProvEspec(nom)==null || buscarProvEspec(nom).isEmpty() || buscarProvEspec(nom).equals("")) {
                
            }else{
                 Object[] linea = new Object[4];
            for (int i = 0; i < lista.size(); i++) {
            linea[0] = lista.get(i).getIdProv();
            linea[1] = lista.get(i).getNomProv();
            linea[2] = lista.get(i).getTelProv();
            linea[3] = lista.get(i).getCorreoProv();
                model.addRow(linea);
            }
            }
           
        }else{
           JOptionPane.showMessageDialog(null, "Debe ingresar un nombre para buscar", "Error", 0); 
        }
    }
          
          /*    create or replace procedure modificaProveedores (idProveedor in number, nombreProveedor in varchar2, 
                                                            telefonoProveedor in varchar2,  correoProveedor in varchar2 )  */

    public void editarProv(int id, String nombre, String tel, String correo) {
        con = Conexion.getConexion();
        stmt = null;
        String sql = "{call modificaProveedores(?,?,?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setInt(1, id);
            stmt.setString(2, nombre);
            stmt.setString(3, tel);
            stmt.setString(4, correo);
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
    
    
     public void verificaProveedorEditado(int id, String nombre, String tel, String correo){
        con = Conexion.getConexion();
        stmt=null; String verif=" ";

        String sql ="{call spVerifNomProv(?,?)}";
        try {
            stmt = con.prepareCall(sql);
            stmt.setString(1, nombre);
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR);
            stmt.executeUpdate();
            verif=stmt.getString(2);
            if (verif.equals("false")) {
                editarProv(id, nombre, tel, correo);
            } else{
                    JOptionPane.showMessageDialog(null, "Ya hay un Proveedor con estos datos");
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
    
     public void editaProvTabla(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        try {
            String id = (String) model.getValueAt(tabla.getSelectedRow(), 0);
            String nombre = model.getValueAt(tabla.getSelectedRow(), 1).toString();
            String tel = model.getValueAt(tabla.getSelectedRow(), 2).toString();
            String correo = model.getValueAt(tabla.getSelectedRow(), 3).toString();
            editarProv(Integer.parseInt(id), nombre, tel, correo);
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para editar", "Error", 0);
        }
    }

     
     /*   
             create or replace procedure borraProveedores (idProveedor in number) */
     
    public void borraProv(int id) {
        con = Conexion.getConexion();
        stmt = null;
        String sql = "{call borraProveedores(?)}";
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

    
     public void borraProvTabla(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        try {
            String id = (String) model.getValueAt(tabla.getSelectedRow(), 0);
            borraProv(Integer.parseInt(id));
            model.removeRow(tabla.getSelectedRow());
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para borrar", "Error", 0);
        }
    }
    
    
}
