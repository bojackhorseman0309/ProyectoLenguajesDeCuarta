/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

/**
 *
 * @author alonso
 */
public class Proveedor {
    
    private static String nomProv;
    private static String telProv;
    private static String correoProv;

    public static String getNomProv() {
        return nomProv;
    }

    public static void setNomProv(String aNomProv) {
        nomProv = aNomProv;
    }

    public static String getTelProv() {
        return telProv;
    }

    public static void setTelProv(String aTelProv) {
        telProv = aTelProv;
    }

    public static String getCorreoProv() {
        return correoProv;
    }

    public static void setCorreoProv(String aCorreoProv) {
        correoProv = aCorreoProv;
    }

    public Proveedor() {
    }
    
    
    
}
