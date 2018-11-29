/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz.interaccionInterfaz;

import conexion.Sesion;
import java.awt.CardLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author mar_8
 */
public class ManejoInterfaz extends JPanel {

    public void cambioPanel(JPanel cards, String nombrePanel) {
        CardLayout cardLayout = (CardLayout) cards.getLayout();
        cardLayout.show(cards, nombrePanel);
    }

    public void cambioFrame(JDialog nuevo) {
        JDialog diag = new JDialog();
        diag = nuevo;
        diag.setLocationRelativeTo(null);
        diag.setVisible(true);
    }

    public void disableButtons(ArrayList<JButton> buttons) {
        if (Sesion.getRol() == 2) {
            buttons.forEach((n) -> n.setEnabled(false));
        }
    }

}
