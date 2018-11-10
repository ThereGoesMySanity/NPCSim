package tasks;

import map.Town;
import people.Person;
import ui.map.TaskDetailsPanel;

import javax.swing.*;

public abstract class TownTask extends Task {
    private final Town town;

    protected TownTask(int size, Town t, boolean unique) {
        super(false, size, unique);
        town = t;
    }

    protected TownTask(int size, Town t) {
        this(size, t, true);
    }

    public TownTask(Town t) {
        this(-1, t);
    }

    @Override
    public double getAddWeight(Person p) {
        if (!p.getTown().equals(town)) return 0;
        else return super.getAddWeight(p);
    }

    @Override
    public void end() {
        super.end();
        town.removeTask(this);
    }

    public Town getTown() {
        return town;
    }

    @Override
    public void addToPane(TaskDetailsPanel tp) {
        tp.addLabel(new JLabel(town.toString()));
    }
}
