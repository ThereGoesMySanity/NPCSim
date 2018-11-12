package ui.map;

import map.AreaMap;
import map.Town;
import people.Person;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.HashMap;

public class ListPanel extends JPanel implements Town.Listener {
    private final DefaultMutableTreeNode root;
    private final DefaultTreeModel model;
    private final HashMap<Town, DefaultMutableTreeNode> towns = new HashMap<>();
    private final HashMap<Person, DefaultMutableTreeNode> people = new HashMap<>();
    private final AreaMap map;

    /**
     * Create the panel.
     */
    ListPanel(AreaMap m, MapPanel mapPanel) {
        map = m;
        setLayout(new BorderLayout(0, 0));
        root = new DefaultMutableTreeNode(m);
        model = new DefaultTreeModel(root);
        reload();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        JTree tree = new JTree(model);
        tree.setCellRenderer(new ListTreeRenderer());
        tree.addTreeSelectionListener(mapPanel.listener);
        scrollPane.setViewportView(tree);
    }

    private DefaultMutableTreeNode personToNode(Person p) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(p);
        people.put(p, node);
        return node;
    }
    void reload() {
        root.removeAllChildren();
        map.stream().forEach(t -> {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(t);
            towns.put(t, node);
            t.people().map(this::personToNode).forEach(n1 -> {
                int index = 0;
                while(index < node.getChildCount() &&
                        ((DefaultMutableTreeNode)node.getChildAt(index)).getUserObject().toString()
                                .compareTo(n1.getUserObject().toString()) < 0) index++;
                model.insertNodeInto(n1, node, index);
            });
            t.addListener(this);
            root.add(node);
        });
    }

    @Override
    public void onAdd(Person p) {
        DefaultMutableTreeNode parent = towns.get(p.getTown());
        int index = 0;
        while(index < parent.getChildCount() &&
                ((DefaultMutableTreeNode)parent.getChildAt(index)).getUserObject().toString()
                .compareTo(p.toString()) < 0) index++;
        model.insertNodeInto(personToNode(p), parent, index);
    }

    @Override
    public void onRemove(Person p) {
        model.removeNodeFromParent(people.get(p));
        people.remove(p);
    }
}
