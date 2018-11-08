package tasks.town;

import map.Town;
import people.Person;
import tasks.TownTask;

public class Farm extends TownTask {

    public Farm(Town t) {
        super(3, t);
    }

    @Override
    public double weightSub(Person p) {
        return getTown().foodWeight();
    }

    @Override
    public boolean work(Person p) {
        p.getTown().farm(10);
        return false;
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {
        groupInteract(0.1, 1);
    }
}
