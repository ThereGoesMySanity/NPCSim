package ui.map;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import map.Map;
import map.Town;
import people.Person;

public class ListPanel extends JPanel implements Town.Listener {
	Map map;
	DefaultMutableTreeNode root;
	DefaultTreeModel model;
	private HashMap<Town, DefaultMutableTreeNode> towns = new HashMap<>();
	private HashMap<Person, DefaultMutableTreeNode> people = new HashMap<>();
	/**
	 * Create the panel.
	 */
	public ListPanel(Map m, PersonDetailsPanel pdp, TownDetailsPanel tdp, JPanel cards) {
		map = m;
		setLayout(new BorderLayout(0, 0));
		root = new DefaultMutableTreeNode(map);
		map.stream().forEach(t -> {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(t);
			towns.put(t, node);
			t.people().map(this::personToNode).forEach(node::add);
			t.addListener(this);
			root.add(node);
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		model = new DefaultTreeModel(root);
		JTree tree = new JTree(model);
		tree.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node == null) return;
			Object o = node.getUserObject();
			CardLayout cl = (CardLayout) cards.getLayout();
			if(o instanceof Town) {
				Town t = (Town)o;
				tdp.setTown(t);
				cl.show(cards, "Town");
			} else if(o instanceof Person) {
				Person p = (Person)o;
				pdp.setPerson(p);
				cl.show(cards, "Person");
			} else {
				cl.show(cards, "blank");
			}
		});
		scrollPane.setViewportView(tree);
	}
	private DefaultMutableTreeNode personToNode(Person p) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(p);
		people.put(p, node);
		return node;
	}
	@Override
	public void onAdd(Person p) {
		DefaultMutableTreeNode parent = towns.get(p.getTown());
		model.insertNodeInto(personToNode(p), parent, 0);
	}
	@Override
	public void onRemove(Person p) {
		model.removeNodeFromParent(people.get(p));
		people.remove(p);
	}
}
