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
    protected double weightSub(Person p) {
        return (p.level - 2) / 5. + 1;
    }

    @Override
    public double getWorkWeight(Person p) {
        if(!p.canAdd(Fight.class)) return 0;
        return super.getWorkWeight(p);
    }

    @Override
    public void add(Person p) {
        super.add(p);
        p.setAttack("1d10");
    }

    @Override
    public boolean work(Person p) {
        if(p.canAdd(Fight.class)) {
            Fight monster = p.getTown().getRandomTask(Fight.class);
            if (monster != null && monster.getCR() <= p.level) p.addTask(monster);
        }
        return false;
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {

    }
}
