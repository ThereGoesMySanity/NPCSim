package tasks.town;

import map.Town;
import people.Person;
import tasks.TownTask;
import things.Item;
import ui.map.TaskDetailsPanel;
import util.Convert;

import javax.swing.*;
import java.util.HashMap;

import static main.Main.rand;
import static people.Stats.Stat.CHA;

public class Shop extends TownTask {
    private static final String[] columnNames = {"Item", "Quantity"};
    private final HashMap<Item, Integer> quantities = new HashMap<>();
    private JTable table;

    public Shop(Town t) {
        super(rand.nextInt(3) + 1, t);
    }

    @Override
    protected double weightSub(Person p) {
        return getStatWeight(p, CHA) * 5;
    }

    @Override
    public boolean work(Person p) {
        p.getTown().travellers().forEach(p1 -> Person.interact(p, p1, 0, 1));
        return false;
    }

    public void addItem(Item i) {
        if (!quantities.containsKey(i)) {
            quantities.put(i, 0);
        }
        quantities.put(i, quantities.get(i) + i.quantity());
    }

    @Override
    public void update() {}

    @Override
    public void updatePost() {}

    @Override
    public void addToPane(TaskDetailsPanel pane) {
        super.addToPane(pane);
        JScrollPane example = new JScrollPane();
        pane.addTab("New tab", example);

        table = new JTable();
        example.setViewportView(table);
        example.setPreferredSize(table.getPreferredSize());
    }

    @Override
    public void updatePane() {
        Convert.mapToTable(quantities, table, columnNames);
    }
}
