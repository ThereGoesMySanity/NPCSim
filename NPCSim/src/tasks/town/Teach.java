package tasks.town;

import map.Town;
import people.Person;
import tasks.TownTask;

public class Teach extends TownTask {

    public Teach(Town t) {
        super(1, t);
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
