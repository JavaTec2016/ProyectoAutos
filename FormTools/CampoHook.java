package FormTools;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class CampoHook {
    public JComponent componente;
    public CampoHook parent;
    public HashMap<String, CampoHook> children = new HashMap<String, CampoHook>();
    public String id;

    public FormHook form;

    public CampoHook(){};
    public CampoHook(JComponent compo){
        componente = compo;
    }
    public CampoHook setFont(Font f){
        componente.setFont(f);
        return this;
    }
    public CampoHook setAlignment(int vertical, int horizontal){
        if(componente instanceof JLabel){
            ((JLabel) componente).setVerticalAlignment(vertical);
            ((JLabel) componente).setHorizontalAlignment(horizontal);
        }
        return this;
    }
    public CampoHook setForeground(Color c){
        componente.setForeground(c);
        return this;
    }
    public CampoHook setBackground(Color c){
        componente.setBackground(c);
        return this;
    }
    public CampoHook setIcon(String ruta){
        if(componente instanceof JLabel){
            ((JLabel) componente).setIcon(new ImageIcon(Objects.requireNonNull(ImageIcon.class.getResource(ruta))));
        }
        return this;
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
            child.id = id;
            componente.add(child.getComponente());
        }
        if(parent != null) parent.appendChild(id, child);
    }
    public void appendChild(String id, CampoHook child, Object constraints){
        children.put(id, child);
        if(child.parent == null){
            child.parent = this;
            child.id = id;
            componente.add(child.getComponente(), constraints);
        }
        //if(parent != null) parent.appendChild(id, child);
    }
    public void removeChild(String id){
        componente.remove(children.get(id).componente);
        children.remove(id);
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
    public CampoHook setBounds(int x, int y, int w, int h){
        componente.setBounds(x,y,w,h);
        return this;
    }
    public CampoHook setPreferredSize(Dimension d){
        componente.setPreferredSize(d);
        return this;
    }
    public CampoHook setMaximumSize(Dimension d){
        componente.setMaximumSize(d);
        return this;
    }
    public CampoHook setSize(Dimension d){
        componente.setSize(d);
        return this;
    }
}
