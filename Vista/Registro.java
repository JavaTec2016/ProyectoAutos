package Vista;

import ErrorHandlin.Call;
import FormTools.CampoHook;
import FormTools.FormHook;
import FormTools.PanelHook;
import Modelo.ModeloBD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Registro extends PanelHook {
    public ModeloBD asociado;
    public JButton btnEditar;
    public JButton btnEliminar;
    public Color colorOriginal;
    public Color colorHover;
    public Color colorSeleccion;

    public Color colorSinEnfoque;
    public Color colorEnfocado;

    public Call clicAccion;
    public Call mouseEnter;
    public Call mouseExit;

    public Registro(){

        initColores();
        setDefaultMouseEnter();
        setDefaultMouseExit();
    }
    public Registro(GridBagLayout l){
        componente = new JPanel(l);
        layout = l;
        initColores();

        setDefaultMouseEnter();
        setDefaultMouseExit();
    }
    public Registro(PanelHook reg){
        this(reg, (ModeloBD) reg.asociados.get("modelo"));
    }
    public Registro(PanelHook reg, ModeloBD modelo){
        componente = reg.componente;
        asociado = modelo;
        btnEditar = (JButton)reg.getChild("btnEditar").getChild("dato").componente;
        btnEliminar = (JButton)reg.getChild("btnEliminar").getChild("dato").componente;

        initColores();
        setDefaultMouseEnter();
        setDefaultMouseExit();
    }
    /**
     * Establece la accion predeterminada del registro cuando el cursor entra a su área
     */
    public void setDefaultMouseEnter(){
        mouseEnter = new Call() {
            @Override
            public void run(Object... data) {
                if(colorSinEnfoque.equals(colorOriginal)){
                    setBackground(colorEnfocado);
                }else {
                    setBackground(colorSeleccion);
                }
            }
        };
    }

    /**
     * Establece la accion predeterminada del registro cuando el cursor sale de su área
     */
    public void setDefaultMouseExit(){
        mouseExit = data -> {
            if(colorSinEnfoque.equals(colorOriginal)){
                setBackground(colorSinEnfoque);
            }else setBackground(colorSeleccion);
        };
    }

    /**
     * Establece colores predeterminados del registro
     */
    public void initColores(){
        colorOriginal = Color.white;
        colorHover = Color.lightGray;
        colorSeleccion = new Color(100 ,255 ,100);

        colorSinEnfoque = colorOriginal;
        colorEnfocado = colorHover;
    }
    /**
     * Establece los colores de enfoque y desenfoque a sus respectivos colores originales del registro
     */
    public void setColorsNormal(){
        colorSinEnfoque = colorOriginal;
        colorEnfocado = colorHover;
        componente.setBackground(colorSinEnfoque);
    }

    /**
     * Establece los colores de enfoque y desenfoque al color de selección del registro
     */
    public void setColorsSeleccion(){
        colorSinEnfoque = colorSeleccion;
        colorEnfocado = colorSeleccion;
        componente.setBackground(colorEnfocado);
    }

    /**
     * Configura la acción del botón eliminar del registro
     * @param ac ActionListener con la logica deseada
     */
    public void configurarEliminar(ActionListener ac){
        for (ActionListener actionListener : btnEliminar.getActionListeners()) {
            btnEliminar.removeActionListener(actionListener);
        }
        btnEliminar.addActionListener(ac);
    }

    /**
     * Configura la acción del botón editar del registro
     * @param ac ActionListener con la logica deseada
     */
    public void configurarEditar(ActionListener ac){
        for (ActionListener actionListener : btnEditar.getActionListeners()) {
            btnEditar.removeActionListener(actionListener);
        }
        btnEditar.addActionListener(ac);
    }

    /**
     * Configura la acción del registro cuando se le da clic
     * @param c
     */
    public void setClicAccion(Call c){
        clicAccion = c;
    }
    /**
     * Configura la acción del registro cuando el cursor entra a su área
     * @param c
     */
    public void setMouseEnter(Call c){
        mouseEnter = c;
    }

    /**
     * Configura la acción del registro cuando el cursor sale de su área
     * @param c
     */
    public void setMouseExit(Call c){
        mouseExit = c;
    }

    /**
     * Envuelve el dato en un panel con los constraints especificados y lo agrega al registro.
     * @param Id nombre del panel que envuelve al elemento
     * @param element Elemento a agregar, llamado "dato" dentro del panel que lo envuelve
     * @param c {@link GridBagConstraints} para agregar al panel
     */
    @Override
    public void addElementConstraint(String Id, CampoHook element, Object c) {
        PanelHook area = FormHook.makeGridBagPanel();
        GridBagConstraints gc = FormHook.makeConstraint(0, 0, 0, 0, GridBagConstraints.NONE);

        //insets negativos para combatir los insets por defecto de los labels
        gc.insets = new Insets(0, -100, 0, -100);
        area.appendChild("dato", element, gc);
        area.setOpaque(false);
        super.addElementConstraint(Id, area, c);
    }

    public void addElementConstraint(String Id, JComponent element, Object c) {
        addElementConstraint(Id, new CampoHook(element), c);
    }
}
