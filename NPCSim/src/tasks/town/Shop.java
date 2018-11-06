package tasks.town;

import static main.Main.rand;
import static people.Stats.Stat.CHA;

import java.util.HashMap;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import map.Town;
import people.Person;
import tasks.TownTask;
import things.Item;
import ui.map.TaskPane;
import util.Convert;

public class Shop extends TownTask {
	private static final String[] columnNames = {"Item", "Quantity"};
	public HashMap<Item, Integer> quantities = new HashMap<>();
	private JTable table;
	public Shop(Town t) {
		super(rand.nextInt(3) + 1, t);
	}

	@Override
	protected double addWeightSub(Person p) {
		return getStatWeight(p, CHA) * 5;
	}

	@Override
	public boolean work(Person p) {
		p.getTown().travellers().forEach(p1 -> Person.interact(p, p1, 0, 1));
		return false;
	}
	public void addItem(Item i) {
		if(!quantities.containsKey(i)) {
			quantities.put(i, 0);
		} 
		quantities.put(i, quantities.get(i)+i.quantity());
	}

	@Override
	public void update() {}

	@Override
	public void updatePost() {}

	@Override
	public void addToPane(TaskPane pane) {
		super.addToPane(pane);
		JTabbedPane jtp = pane.getTabs();
		JScrollPane example = new JScrollPane();
		jtp.addTab("New tab", null, example, null);
		
		table = new JTable();
		example.setViewportView(table);
		example.setPreferredSize(table.getPreferredSize());
	}
	@Override
	public void updatePane() {
		Convert.mapToTable(quantities, table, columnNames);
	}
}
