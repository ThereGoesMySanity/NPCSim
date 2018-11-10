package tasks.other;

import people.Person;
import tasks.Task;
import tasks.town.Fight;

import static main.Main.rand;
import static main.Main.vars;
import static util.Variables.Ints.ADVENTURER_WEIGHT;

public class Adventurer extends Travel {
    public Adventurer(boolean temp) {
        super(temp);
    }
    @Override
    protected double weightSub(Person p) {
        return p.level > 2 && p.hashCode() % vars.get(ADVENTURER_WEIGHT) == 0 ? 1 : 0;
    }

    @Override
    public double getWorkWeight(Person p) {
        if(!p.canAdd(Fight.class)) {
            return 0;
        }
        return 1;
    }

    @Override
    public void add(Person p) {
        super.add(p);
        p.setAttack("1d10");
    }

    @Override
    public boolean work(Person p) {
        if(!p.canAdd(Fight.class)) {
            if (rand.nextInt(5) == 0) {
                return travel(p);
            }
        } else {
            Task monster = p.getTown().getRandomTask(Fight.class);
            if (monster != null) {
                p.addTask(monster);
            }
        }
        return false;
    }

    @Override
    public void update() {
        super.update();
    }
}
