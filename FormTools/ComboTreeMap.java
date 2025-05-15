package FormTools;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class ComboTreeMap<T> implements MutableComboBoxModel<T>, Serializable {
    private TreeMap<Integer, T> entries;
    T selectedObject;
    @Override
    public void setSelectedItem(Object anItem) {
        Integer idx = getIndexOf(anItem);
        if(idx == -1) return;
        selectedObject = entries.get(idx);

    }
    public int getIndexOf(Object obj){
        ArrayList<Object> indexable = new ArrayList<>(entries.values());
        return indexable.indexOf(obj);
    }
    @Override
    public Object getSelectedItem() {
        return selectedObject;
    }

    @Override
    public int getSize() {
        return entries.size();
    }

    @Override
    public T getElementAt(int index) {
        if(index < 0 || index >= getSize()) return null;
        return entries.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }

    @Override
    public void addElement(T item) {
        entries.put(getSize(), item);
        setSelectedItem(item);
    }


    @Override
    public void removeElement(Object obj) {
        Integer i = getIndexOf(obj);
        entries.remove(i);
    }


    @Override
    public void insertElementAt(T item, int index) {
        entries.put(index, item);
    }

    @Override
    public void removeElementAt(int index) {
        entries.remove(index);
    }
}
