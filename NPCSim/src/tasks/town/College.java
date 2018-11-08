package tasks.town;

import map.Town;
import people.Person;
import tasks.TownTask;

import java.util.HashMap;

import static main.Main.tables;
import static people.Stats.Stat.INT;

public class College extends TownTask {
    private String name;
    private HashMap<Person, Integer> work = new HashMap<>();
    public College(Town t) {
        super(t);
        if(t != null) name = tables.chooseName("college").replace("Town", t.toString());
    }

    @Override
    public void add(Person p) {
        super.add(p);
        work.put(p, 0);
    }

    @Override
    protected double addWeightSub(Person p) {
        return getStatWeight(p, INT);
    }

    @Override
    public boolean work(Person p) {
        int progress = work.get(p) + 1;
        work.put(p, progress);
        if(progress > 48) {
            p.setStat(INT, p.getStat(INT) + 2);
            p.removeTask(this);
        }
        return false;
    }

    @Override
    public void remove(Person p) {
        super.remove(p);
        work.remove(p);
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {
    }

    @Override
    public String toString() {
        return name;
    }
}
