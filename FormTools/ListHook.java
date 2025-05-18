package FormTools;

import ErrorHandlin.Call;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;


public class ListHook<K, V> extends JComboBox<Item<K, V>> {
    Map<K, Item<K, V>> items;
    Call accion;

    public ListHook(){
        super();
        items = new LinkedHashMap<>();
        setRenderer(new ItemRenderer<K, V>());

        setDefaultAccion();
        setListener();
    }
    public ListHook(Map<K, V> source){
        this();
        cargarItems(source);
    }

    /**
     * Agrega todos los items de un mapa al combobox
     * @param source mapa de items
     */
    public void cargarItems(Map<K, V> source){
        source.forEach(this::addItem);
    }

    /**
     * Agrega un nuevo item al combobox
     * @param key llave del item
     * @param val valor del item
     */
    public void addItem(K key, V val){
        Item<K, V> item = new Item<>(key, val);
        items.put(key, item);
        super.addItem(item);
    }

    @Override
    public void removeItem(Object anObject) {
        if(anObject instanceof Item<?,?>){
            K key = (K) ((Item<?, ?>) anObject).getKey();
            items.remove(key);
        }
        super.removeItem(anObject);
    }

    /**
     * Remueve el item con la llave especificada del combobox
     * @param key  llave del objeto a remover
     */
    public void removeItemByKey(K key){
        removeItem(items.get(key));
    }
    public void removaAll(){
        super.removeAll();
    }
    public K getSelectedKey(){
        if(getSelectedItem() == null) return null;
        return ((Item<K, V>)getSelectedItem()).getKey();
    }
    public V getSelectedValue(){
        if(getSelectedItem() == null) return null;
        return ((Item<K, V>)getSelectedItem()).getValue();
    }

    @Override
    public void setSelectedItem(Object anObject) {
        super.setSelectedItem(anObject);
    }
    public void setSelectedKey(Object key){
        Item<K, V> item = items.get(key);
        setSelectedItem(item);
    }
    /**
     * Establece la accion por defecto al seleccionar un item
     */
    void setDefaultAccion(){
        accion = data -> {
            Item<K, V> item = (Item<K, V>) getSelectedItem();
            System.out.println(item);
        };
    }
    public void setAccion(Call c){
        accion = c;
    }
    /**
     * Habilita la función de selección del componente
     */
    void setListener(){
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accion.run(e);
            }
        });
    }
}
class Item<K, V> {
    private K key;
    private V value;

    public Item(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Verifica que tanto la llave como el valor de este {@link Item} retornen verdadero al compararlas con
     * la llave y valor de otro {@link Item}. Compara las llaves mediante {@link K#equals(K)} y los valores mediante {@link V#equals(V)}
     * @param item Item a comparar
     * @return true si sus elementos son iguales, false si no
     */
    public boolean equals(Item<K, V> item){
        return key.equals(item.key) && value.equals(item.value);
    }
    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
    public String keyToString(){
        if(key == null) return "";
        return key.toString();
    }
    @Override
    public String toString() {
        if(value == null) return "";
        return value.toString();
    }
}

class ItemRenderer<K, V> extends BasicComboBoxRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value != null){
            Item<K, V> item = (Item<K, V>) value;
            setText(item.toString());
        }else{
            setText("Sin opciones..");
            return this;
        }
        if(index == -1){
            Item<K, V> item = (Item<K, V>) value;
            setText(item.toString());
        }
        return this;
    }
}