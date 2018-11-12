package ui.map;

import people.Person;
import tasks.other.Adventurer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.nio.file.Paths;

public class ListTreeRenderer extends DefaultTreeCellRenderer {
    private Icon adventurer = new ImageIcon(Paths.get("resources", "icons", "sword.png").toString());
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        if(obj instanceof Person) {
            Person p = (Person) obj;
            if(!p.canAdd(Adventurer.class)) {
//                setIcon(adventurer);
            }
        }
        return this;
    }
}
