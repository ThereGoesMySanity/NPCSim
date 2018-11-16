package ui.map;

import ui.map.DetailsPanel.Type;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

import static ui.map.DetailsPanel.DetailsObject;

public class DetailsListener implements ListSelectionListener, TreeSelectionListener {
    public interface DLListener {
        void onChange(DetailsObject o);
        boolean listening(Type t);
    }

    private ArrayList<DLListener> listeners = new ArrayList<>();

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList)e.getSource();
        onChange((DetailsObject)list.getSelectedValue());
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if(e.getNewLeadSelectionPath() != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof DetailsObject) {
                onChange((DetailsObject) node.getUserObject());
            }
        }
    }
    private void onChange(DetailsObject det) {
        for(DLListener l : listeners) {
            if(l.listening(det.getType())) {
                l.onChange(det);
            }
        }
    }
    public void addListener(DLListener d) {
        listeners.add(d);
    }
}
