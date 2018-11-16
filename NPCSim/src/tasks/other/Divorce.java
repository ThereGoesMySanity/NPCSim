package tasks.other;

import people.Person;
import tasks.Task;

public class Divorce extends Task {

    public Divorce(boolean temp) {
        super(temp);
    }

    @Override
    public double weightSub(Person p) {
        return 0;
    }

    @Override
    public boolean work(Person p) {
        p.setSpouse(null);
        //TODO
        return true;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void updatePost() {
        // TODO Auto-generated method stub

    }
}
