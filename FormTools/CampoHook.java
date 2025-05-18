package FormTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CampoHook {
    public JComponent componente;
    public CampoHook parent;
    public HashMap<String, CampoHook> children = new HashMap<String, CampoHook>();
    public String id;
    public HashMap<String, Object> asociados = new HashMap<>();
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
    public void removeChildren(){
        String[] keys = children.keySet().toArray(new String[0]);
        for (String key : keys) {
            removeChild(key);
        }
        componente.revalidate();
    }

    public JComponent getComponente() {
        return componente;
    }
    public CampoHook setComponente(JComponent componente){
        this.componente = componente;
        return this;
    }
    public CampoHook setOpaque(boolean estado){
        componente.setOpaque(estado);
        return this;
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
        if(componente == null){
            System.out.println("Componente nulo: " + id);
            return this;
        }
        componente.setPreferredSize(d);
        return this;
    }
    public CampoHook setMaximumSize(Dimension d){
        componente.setMaximumSize(d);
        return this;
    }
    public CampoHook setMinimumSize(Dimension d){
        componente.setMinimumSize(d);
        return this;
    }
    public CampoHook setSize(Dimension d){
        componente.setSize(d);
        return this;
    }
    public CampoHook setText(String texto){
        try {
            Method m = componente.getClass().getDeclaredMethod("setText", String.class);
            m.invoke(componente, texto);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {}
        return this;
    }
    /**
     * Recibe una ruta mediante la cual buscar un nodo dentro de una cadena de campos hijos
     * @param ruta ruta con todos los nombres de los campos hijos a atravesar, separados por diagonales
     * @return campo hijo final, o null si no existe
     */
    public CampoHook childPath(String ruta){
        String[] partes = ruta.split("/");
        CampoHook ret = this;
        for (String parte : partes) {
            if(ret.children.containsKey(parte)) ret = ret.getChild(parte);
            else return null;
        }
        return ret;
    }
    public void addActionListener(ActionListener a){
        if(!(componente instanceof JButton)) return;
        ((JButton) componente).addActionListener(a);
    }

    public static ArrayList<CampoHook> crearMultiLinea(String[] textos, Color color, Font fuente){
        ArrayList<CampoHook> campos = new ArrayList<>();
        for (String textoLinea : textos) {
            CampoHook linea = new CampoHook(new JLabel(textoLinea)).setFont(fuente).setForeground(color);
            campos.add(linea);
        }
        return campos;
    }
}
