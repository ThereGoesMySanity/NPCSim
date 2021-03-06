package tasks.other;

import people.Person;
import tasks.Task;
import tasks.town.Shop;
import things.Item;
import ui.map.TaskDetailsPanel;

import javax.swing.*;

import static main.Main.tables;

public class Craft extends Task {
    private Item item;
    private int work;
    private JLabel workLabel;

    public Craft(boolean temp) {
        super(temp);
        if (!temp) item = tables.getCraft();
    }

    @Override
    protected double weightSub(Person p) {
        return 1;
    }

    @Override
    public boolean work(Person p) {
        work += 5;
        if (work >= item.value()) {
            Shop s = p.getTown().getRandomTask(Shop.class);
            for (int i = 0; i < work / item.value(); i++) {
                s.addItem(item);
            }
            s.people().forEach(p1 -> Person.interact(p, p1, 0, 0.2));
        }
        return work >= item.value();
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {
    }

    @Override
    public String toString() {
        return "Crafting a(n) " + item;
    }

    @Override
    public void updatePane() {
        workLabel.setText(work + "");
    }

    @Override
    public void addToPane(TaskDetailsPanel pane) {
        workLabel = new JLabel(work + "");
        pane.addLabel(new JLabel("Work:"), workLabel);
    }
}
