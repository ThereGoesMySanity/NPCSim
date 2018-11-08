package tasks.other;

import people.Person;
import tasks.Task;

import static people.Stats.Stat.DEX;

public class Hunt extends Task {

    public Hunt(boolean temp) {
        super(temp);
    }

    @Override
    protected double weightSub(Person p) {
        return 1;
    }

    @Override
    public boolean work(Person p) {
        p.getTown().farm(p.getStat(DEX) / 2);
        return true;
    }

    @Override
    public void update() {}

    @Override
    public void updatePost() {}
}
