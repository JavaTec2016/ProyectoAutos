package FormTools;

import javax.swing.*;
import java.awt.*;

public class PanelHook extends CampoHook {

    private LayoutManager layout;

    public PanelHook(){
        this(null);
    };
    public PanelHook(LayoutManager l){
        componente = new JPanel();
        setLayout(l);
    }
    public LayoutManager getLayout(){
        return layout;
    }
    public void setLayout(LayoutManager l){
        componente.setLayout(l);
        layout = l;

    }
    public PanelHook setPreferredSize(Dimension d){
        return (PanelHook)super.setPreferredSize(d);
    }
    public PanelHook setSize(Dimension d){
        return (PanelHook) super.setSize(d);
    }
    public PanelHook setForeground(Color c){
        return (PanelHook)super.setForeground(c);
    }
    public PanelHook setBackground(Color c){
        return (PanelHook)super.setBackground(c);
    }
    public void addElement(String Id, CampoHook element){
        appendChild(Id, element);
        componente.add(element.componente);
    }
    public void addElementConstraint(String Id, CampoHook element, Object c){
        appendChild(Id, element);
        componente.add(element.componente, c);
    }
    public void addElementConstraint(String Id, JComponent element, Object c){
        appendChild(Id, new CampoHook(element));
        componente.add(element, c);
    }
    public void addElement(String Id, JComponent element){
        addElement(Id, new CampoHook(element));
    }
    public PanelHook setBounds(int w, int h){
        componente.setBounds(componente.getX(), componente.getY(), w, h);
        return this;
    }
    public PanelHook setBounds(int x, int y, int w, int h){
        componente.setBounds(x,y,w,h);
        return this;
    }
}
