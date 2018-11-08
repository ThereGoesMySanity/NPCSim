package tasks.keep;

import map.Town;
import people.Person;
import tasks.TownTask;

public class Soldier extends TownTask {

    public Soldier(Town t) {
        super(t);
    }

    @Override
    protected double addWeightSub(Person p) {
        return 0;
    }

    @Override
    public boolean work(Person p) {
        return false;
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {

    }
}
