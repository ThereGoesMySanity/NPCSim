package tasks.town;

import map.Town;
import people.Person;
import tasks.TownTask;
import things.Item;
import ui.map.TaskDetailsPanel;
import util.Convert;

import javax.swing.*;
import java.util.ArrayList;

import static main.Main.tables;

public class Blacksmith extends TownTask {
    private final ArrayList<Item> jobs = new ArrayList<>();
    private int work;
    private JList<Item> list;
    private JLabel labelWork;

    public Blacksmith(Town t) {
        super(3, t);
    }

    @Override
    protected double weightSub(Person p) {
        return 1;
    }

    @Override
    public boolean work(Person p) {
        if (jobs.size() > 0) {
            Item item = jobs.get(0);
            work += 5;
            if (work >= item.value()) {
                Shop s = getTown().getRandomTask(Shop.class);
                for (int i = 0; i < work / item.value(); i++) {
                    s.addItem(item);
                }
                s.people().forEach(p1 -> Person.interact(p, p1, 0, 0.2));
                jobs.remove(0);
                work = 0;
            }
        }
        return false;
    }

    private void addJob(Item i) {
        jobs.add(i);
    }

    @Override
    public void update() {
        if(jobs.size() == 0) addJob(tables.getSmith());
    }

    @Override
    public void updatePost() {
    }

    @Override
    public void addToPane(TaskDetailsPanel pane) {
        super.addToPane(pane);
        labelWork = new JLabel(work + "");
        pane.addLabel(labelWork);
        JScrollPane example = new JScrollPane();
        pane.addTab("Jobs", example);

        list = new JList<>();
        example.setViewportView(list);
        example.setPreferredSize(list.getPreferredSize());
    }

    @Override
    public void updatePane() {
        Convert.listToJList(jobs, list);
        labelWork.setText(work + "");
    }

}
