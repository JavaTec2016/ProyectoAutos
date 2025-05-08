package Vista;

import ErrorHandlin.Call;
import FormTools.PanelHook;
import Modelo.ModeloBD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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

    public Registro(){

        initColores();
    }
    public Registro(GridBagLayout l){
        componente = new JPanel(l);
        layout = l;

        initColores();
    }
    public Registro(PanelHook reg){
        this(reg, (ModeloBD) reg.asociados.get("modelo"));
    }
    public Registro(PanelHook reg, ModeloBD modelo){
        componente = reg.componente;
        asociado = modelo;
        btnEditar = (JButton)reg.getChild("btnEditar").componente;
        btnEliminar = (JButton)reg.getChild("btnEliminar").componente;

        initColores();
    }
    public void initColores(){
        colorOriginal = Color.white;
        colorHover = Color.lightGray;
        colorSeleccion = new Color(100 ,255 ,100);

        colorSinEnfoque = colorOriginal;
        colorEnfocado = colorHover;
    }
    public void setColorsNormal(){
        colorSinEnfoque = colorOriginal;
        colorEnfocado = colorHover;
        componente.setBackground(colorSinEnfoque);
    }
    public void setColorsSeleccion(){
        colorSinEnfoque = colorSeleccion;
        colorEnfocado = colorSeleccion;
        componente.setBackground(colorEnfocado);
    }
    public void configurarEliminar(ActionListener ac){
        btnEliminar.addActionListener(ac);
    }
    public void configurarEditar(ActionListener ac){
        btnEditar.addActionListener(ac);
    }
    public void setClicAccion(Call c){
        clicAccion = c;
    }

}
