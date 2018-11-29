/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

/**
 *
 * @author mar_8
 */
public class Sesion{
    
    private static String nombre;
    private static int idUsuario;
    private static String correo;
    private static int rol;

    public Sesion() {
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        Sesion.nombre = nombre;
    }

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(int idUsuario) {
        Sesion.idUsuario = idUsuario;
    }

    public static String getCorreo() {
        return correo;
    }

    public static void setCorreo(String correo) {
        Sesion.correo = correo;
    }

    public static int getRol() {
        return rol;
    }

    public static void setRol(int rol) {
        Sesion.rol = rol;
    }

    
  
          
    
}
