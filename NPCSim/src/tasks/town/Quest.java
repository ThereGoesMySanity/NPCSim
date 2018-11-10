package tasks.town;

import map.Town;
import people.Person;
import tasks.TownTask;
import ui.map.TaskDetailsPanel;

import javax.swing.*;

import static main.Main.rand;
import static main.Main.tables;

public class Quest extends TownTask {
    private int progress;
    private final int difficulty;
    private final String reason;
    public Quest(Town t) {
        super(5, t);
        difficulty = rand.nextInt(40) + 10;
        reason = tables.getGenerator("quest").generate();
    }

    @Override
    protected double weightSub(Person p) {
        return 0;
    }

    @Override
    public boolean work(Person p) {
        return ++progress < difficulty;
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {
    }

    @Override
    public void addToPane(TaskDetailsPanel tp) {
        super.addToPane(tp);
        tp.addLabel(new JLabel(reason));
    }
}
