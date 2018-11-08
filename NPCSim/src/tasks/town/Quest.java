package tasks.town;

import map.Town;
import people.Person;
import tasks.TownTask;

import static main.Main.rand;
import static main.Main.tables;

public class Quest extends TownTask {
    private int progress, difficulty;
    private String reason;
    public Quest(Town t) {
        super(5, t);
        difficulty = rand.nextInt(40) + 10;
        if(t != null) reason = tables.getGenerator("quest").generate();
    }

    @Override
    protected double addWeightSub(Person p) {
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
    public String toString() {
        return reason;
    }
}
