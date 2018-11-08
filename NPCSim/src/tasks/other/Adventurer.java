package tasks.other;

import people.Person;
import people.Stats.Stat;
import tasks.Task;
import tasks.town.Fight;

import java.util.Arrays;

import static main.Main.rand;

public class Adventurer extends Travel {
    private Task monster;
    public Adventurer(boolean temp) {
        super(temp);
    }

    @Override
    protected double weightSub(Person p) {
        return super.weightSub(p)
                * Arrays.stream(Stat.values()).mapToDouble(s -> getStatWeight(p, s)).max().orElse(1);
    }

    @Override
    public void add(Person p) {
        super.add(p);
        p.setAttack("1d10");
    }

    @Override
    public boolean work(Person p) {
        if(!p.getTasks().contains(monster)) {
            if(rand.nextInt(5) == 0) {
                return travel(p);
            } else {
                monster = p.getTown().getRandomTask(Fight.class);
                if(monster != null) p.addTask(monster);
            }
        }
        return false;
    }
}
