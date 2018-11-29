/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz.interaccionInterfaz;

import conexion.Sesion;
import interaccionBD.LoginBD;
import interfaz.Principal;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author mar_8
 */
public class ManejoLogin{
    LoginBD login =new LoginBD();
    Principal principal=new Principal();    
    public void conectarse(String user,String password, JFrame frame, JTextField us, JPasswordField pass){
        String respuesta=login.validar(user, password);       
        if (respuesta.equalsIgnoreCase("correcto")){
            frame.dispose();
            setUsuario(user);
            principal.setVisible(true);
        }else if (respuesta.equalsIgnoreCase("error")){
            JOptionPane.showMessageDialog(frame, "Password o contraseña incorrectos, intente de nuevo", "Error",0);
            limpiarLogin(us, pass);
        }else{
            JOptionPane.showMessageDialog(frame, "Password o contraseña incorrectos, intente de nuevo", "Error",0);
             limpiarLogin(us, pass);
        } 
    }   
    
    public void limpiarLogin(JTextField user,JPasswordField pass){
        user.setText("");
        pass.setText("");
    }
    
    
    public void setUsuario(String user){
        ArrayList<String> array=new ArrayList<String>();
        array=login.setUsuario(user);
        principal.setUsuarioLabelTex(array.get(0));  
        Sesion.setIdUsuario(Integer.parseInt(array.get(1)));
        Sesion.setRol(Integer.parseInt(array.get(2)));
        Sesion.setCorreo(user);            
    }
    
}

