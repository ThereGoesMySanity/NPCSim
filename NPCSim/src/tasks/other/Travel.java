package tasks.other;

import map.Town;
import people.Person;
import tasks.Task;
import util.Weight;

import static main.Main.rand;

public class Travel extends Task {
    public Travel(boolean temp) {
        super(temp, 1);
    }

    @Override
    protected double weightSub(Person p) {
        if (p.getTown().residents.size() < 5) return 0;
        else return 0;
    }

    @Override
    public boolean work(Person p) {
        if (rand.nextInt(10) == 0) {
            return travel(p);
        }
        return false;
    }

    boolean travel(Person p) {
        if (rand.nextInt(10) == 0) {
            Town town = p.getTown();
            town.remove(p);
            town.add(p);
            p.record("Settled down in " + town);
            return true;
        } else {
            Town town = Weight.choose(p.getTown().destinations());
            p.getTown().remove(p);
            town.addTraveller(p);
            p.record("Travelled to " + town);
            return false;
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {
    }
}
