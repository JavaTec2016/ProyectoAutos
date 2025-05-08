package Vista;

import FormTools.FormHook;
import FormTools.PanelHook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Login extends PanelHook {
    public Login(){
        super();
        componente = FormHook.makeGridBagPanel().componente;
        this.layout = componente.getLayout();
    }
    public void accionLogear(ActionListener ac){
        ((JButton)form.getBoton("btnLogear").componente).addActionListener(ac);
    }
}
