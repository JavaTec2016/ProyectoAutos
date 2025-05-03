package FormTools;

import javax.swing.*;

public class ScrollHook extends CampoHook {
    private CampoHook view;
    private JScrollPane componente;

    public ScrollHook(){
        componente = new JScrollPane();
    };
    public ScrollHook(CampoHook v){
        componente = new JScrollPane();
        setView(v);
    }
    public ScrollHook(JComponent v){
        componente = new JScrollPane();
        setView(v);
    }
    public void setView(CampoHook v){
        componente.setViewportView(v.componente);
        view = v;
    }
    public void setView(JComponent v){
        componente.setViewportView(v);
        view = new CampoHook(v);
    }

    @Override
    public JScrollPane getComponente() {
        return componente;
    }
    @Override
    public CampoHook setBounds(int x, int y, int w, int h){
        componente.setBounds(x,y,w,h);
        return this;
    }

    public CampoHook getView() {
        return view;
    }
}
