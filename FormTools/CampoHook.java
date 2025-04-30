package FormTools;

import javax.swing.*;
import java.util.HashMap;

public class CampoHook {
    public JComponent componente;
    public CampoHook parent;
    public HashMap<String, CampoHook> children = new HashMap<String, CampoHook>();

    public CampoHook(){};
    public CampoHook(JComponent compo){
        componente = compo;
    }

    /**
     * agrega el componente hijo y, si tiene un nodo padre, tambien lo agrega
     * @param id id unico del componente hijo
     * @param child el comp
     */
    public void appendChild(String id, CampoHook child){
        children.put(id, child);
        if(child.parent == null){
            child.parent = this;
            componente.add(child.getComponente());
        }
        if(parent != null) parent.appendChild(id, child);
    }

    public JComponent getComponente() {
        return componente;
    }
    public CampoHook getChild(String id){
        return children.get(id);
    }
    public JComponent getChildComponent(String id){
        return getChild(id).getComponente();
    }
    public void setBounds(int x, int y, int w, int h){
        componente.setBounds(x,y,w,h);
    }
}
