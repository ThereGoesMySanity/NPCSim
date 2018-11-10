package tasks.other;

import people.Person;
import tasks.Task;

public class Idle extends Task {
    public Idle(boolean temp) {
        super(temp);
    }

    @Override
    protected double weightSub(Person p) {
        return 1;
    }

    @Override
    public boolean work(Person p) {
        return true;
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {
    }
}
