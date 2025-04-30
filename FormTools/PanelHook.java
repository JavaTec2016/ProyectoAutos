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
    public void setBounds(int w, int h){
        componente.setBounds(componente.getX(), componente.getY(), w, h);
    }
    public void setBounds(int x, int y, int w, int h){
        componente.setBounds(x,y,w,h);
    }
}
