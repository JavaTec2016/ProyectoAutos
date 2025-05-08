package Vista;

import FormTools.PanelHook;
import Modelo.ModeloBD;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Registro extends PanelHook {
    public ModeloBD asociado;
    public JButton btnEditar;
    public JButton btnEliminar;

    public Registro(PanelHook reg){
        this(reg, (ModeloBD) reg.asociados.get("modelo"));
    }
    public Registro(PanelHook reg, ModeloBD modelo){
        componente = reg.componente;
        asociado = modelo;
        btnEditar = (JButton)reg.getChild("btnEditar").componente;
        btnEliminar = (JButton)reg.getChild("btnEliminar").componente;
    }
    public void configurarEliminar(ActionListener ac){
        btnEliminar.addActionListener(ac);
    }
    public void configurarEditar(ActionListener ac){
        btnEditar.addActionListener(ac);
    }
}
