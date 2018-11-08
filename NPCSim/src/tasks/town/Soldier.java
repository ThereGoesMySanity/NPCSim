package tasks.town;

import map.Town;
import people.Person;
import people.Stats;
import tasks.TownTask;
import util.Weight;

import static main.Main.rand;

public class Soldier extends TownTask {

    public Soldier(Town t) {
        super(1, t);
        Person p = new Person(t);
        p.newLastName("soldier");
        p.setStat(Weight.choose(Stats.Stat.values(), 0, 2), rand.nextInt(5) + 11);
        p.addTask(this);
    }

    @Override
    protected double addWeightSub(Person p) {
        return p.level / 5. + 1;
    }

    @Override
    public void add(Person p) {
        super.add(p);
        p.setAttack("1d10");
    }

    @Override
    public boolean work(Person p) {
        Fight monster = p.getTown().getRandomTask(Fight.class);
        if(monster != null && !p.getTasks().contains(monster)) p.addTask(monster);
        return false;
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {

    }
}
