package FormTools;

import javax.swing.*;

public class ComboHook<E> extends CampoHook{

    public ComboHook(){
        componente = new JComboBox<>();
    }
    public void addItem(E item){
        ((JComboBox)componente).addItem(item);
    }
    public Object getSelectedItem(){
        return ((JComboBox)componente).getSelectedItem();
    }
}
