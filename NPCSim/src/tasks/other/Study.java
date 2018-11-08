package tasks.other;

import people.Person;
import tasks.Task;

import static main.Main.rand;
import static people.Stats.Stat.INT;

public class Study extends Task {

    public Study(boolean temp) {
        super(temp);
    }

    @Override
    protected double weightSub(Person p) {
        return 1;
    }

    @Override
    public boolean work(Person p) {
        if (rand.nextInt(20) == 0) {
            p.setStat(INT, p.getStat(INT) + 1);
        }
        return true;
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {
    }
}
