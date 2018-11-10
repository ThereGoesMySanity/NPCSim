package tasks.town;

import map.Town;
import people.Person;
import tasks.TownTask;
import util.Weight;

import static people.Stats.Stat.CHA;

public class Socialize extends TownTask {
    public Socialize(Town t) {
        super(t);
        Person p = Weight.weightedChoice(p1 -> Math.pow(4, p1.getStatMod(CHA)), t.residents);
        p.addTask(this);
    }

    @Override
    public double getWorkWeight(Person p) {
        return 100;
    }

    @Override
    public double weightSub(Person p) {
        double sum = Math.max(0, people().stream().mapToDouble(p::getRel).sum());
        return Math.log1p(sum) + 1;
    }

    @Override
    public void updatePost() {
        groupInteract(0.5, 1);
        end();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Socialize");
        if (people().size() > 0) {
            sb.append(" with ");
            people().forEach(p -> sb.append(p).append(", "));
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    @Override
    public void update() {
    }

    @Override
    public boolean work(Person p) {
        return false;
    }
}
