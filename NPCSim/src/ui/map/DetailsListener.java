package ui.map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import static ui.map.DetailsPanel.DetailsObject;

class DetailsListener implements ListSelectionListener, TreeSelectionListener {
    private final MapPanel mapPanel;

    DetailsListener(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList)e.getSource();
        mapPanel.add((DetailsObject) list.getSelectedValue());
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if(e.getNewLeadSelectionPath() != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof DetailsObject) {
                mapPanel.add((DetailsObject) node.getUserObject());
            }
        }
    }
}
