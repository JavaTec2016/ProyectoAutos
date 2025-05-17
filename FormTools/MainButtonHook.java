package FormTools;

import ErrorHandlin.Call;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainButtonHook extends PanelHook {
    PanelHook img;
    public CampoHook separator;
    PanelHook textosArea;
    ArrayList<CampoHook> textos;

    Color foreground;
    Color background;

    Call mouseClick;
    Call mouseEnter;
    Call mouseExit;


    public MainButtonHook(){
        componente = new JPanel(new GridBagLayout());
        img = new PanelHook().setOpaque(false);
        separator = new CampoHook(new JSeparator());
        textosArea = FormHook.makeGridBagPanel().setOpaque(false);
        iniciarListener();
    }
    private void agregarElementos(double imagenRatio){
        GridBagConstraints constraint = FormHook.makeConstraint(0, -1, 1F, (float) (imagenRatio-0.05), GridBagConstraints.BOTH);
        constraint.insets = new Insets(0,10,0,10);

        appendChild("img", img, FormHook.makeConstraint(0, -1, 0.7f, 0.8f, GridBagConstraints.BOTH));
        constraint.weightx = 0.85;
        constraint.weighty = 0;
        appendChild("separator", separator, constraint);
        constraint.weighty = 0.4;
        appendChild("textos", textosArea, constraint);
    }
    public MainButtonHook(String[] textoLineas, String imagenNombre, Dimension d, Color bg, Color fg, double imagenRatio){
        this();
        updateBackground(bg);
        updateForeground(fg);
        setPreferredSize(d);

        setLineBorder(Color.darkGray, 3, true);
        setImagen(FormHook.crearImagen(imagenNombre));
        setTextos(textoLineas);
        setTextosColor(fg);
        attachText(FormHook.makeConstraint(0, -1, 1, 1, GridBagConstraints.BOTH));

        setDefaultEnter();
        setDefaultExit();
        iniciarListener();

        agregarElementos(imagenRatio);
    }
    public void setDefaultEnter(){
        setMouseEnter(new Call() {
            @Override
            public void run(Object... data) {
                setBackground(background.darker());
            }
        });
    }
    public void setDefaultExit(){
        setMouseExit(new Call() {
            @Override
            public void run(Object... data) {
                setBackground(background);
            }
        });
    }

    public MainButtonHook setImagen(ImageHook imagen){
        img.setComponente(imagen).setOpaque(false);
        return this;
    }
    @Override
    public MainButtonHook setBackground(Color c) {
        return (MainButtonHook) super.setBackground(c);
    }
    @Override
    public MainButtonHook setForeground(Color c) {
        return (MainButtonHook) super.setForeground(c);
    }
    public MainButtonHook updateForeground(Color c){
        foreground = c;
        return setForeground(foreground);
    }
    public MainButtonHook updateBackground(Color c){
        background = c;
        return setBackground(background);
    }

    public Color getForeground() {
        return foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setTextos(String[] textos){
        this.textos = CampoHook.crearMultiLinea(textos, foreground, new Font("Segoe UI", Font.PLAIN, 14));
    }
    public void attachText(GridBagConstraints constraint){
        int i = 1;
        for (CampoHook texto : textos) {
            texto.setAlignment(SwingConstants.CENTER, SwingConstants.CENTER);
            textosArea.appendChild("linea"+i, texto, constraint);
            i++;
        }
    }
    public MainButtonHook setTextosColor(Color c){
        for (CampoHook texto : textos) {
            texto.setForeground(c);
        }
        return this;
    }
    public MainButtonHook setTextosFont(Font f){
        for (CampoHook texto : textos) {
            texto.setFont(f);
        }
        return this;
    }
    public void setMouseClick(Call call){
        mouseClick = call;
    }
    public void setMouseEnter(Call call){
        mouseEnter = call;
    }
    public void setMouseExit(Call call){
        mouseExit = call;
    }

    public void iniciarListener(){
        componente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClick.run(e);
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mouseEnter.run(e);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseExit.run(e);
                super.mouseExited(e);
            }
        });
    }

}
