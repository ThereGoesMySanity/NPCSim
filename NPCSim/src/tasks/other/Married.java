package tasks.other;

import people.Person;
import people.Race;
import tasks.Task;

import static main.Main.rand;
import static main.Main.vars;
import static util.Variables.Ints.BIRTH_CHANCE;

public class Married extends Task {

    public Married(boolean temp) {
        super(temp);
    }
    private int work = 0;
    @Override
    public double weightSub(Person p) {
        return 0;
    }

    @Override
    public double getWorkWeight(Person p) {
        return 1;
    }

    @Override
    public boolean work(Person p) {
        if(p.spouse == null) return true;
        work++;
        groupInteract(2, 2);
        Race babbyRace = Race.getRace(p.getRace(), p.spouse.getRace());
        if (work > 20
                && babbyRace != null
                && p.getAge() < p.getRace().getAge() / 2
                && p.gender == 1
                && rand.nextInt(vars.get(BIRTH_CHANCE)) == 0) {
            Person babby = new Person(p.getTown(), p.getLastName(), 0, babbyRace);
            people().forEach(p1 -> {
                p1.record("BABBY HAS CREATE");
                p1.addChild(babby);
            });
            work = 0;
        }
        return false;
    }

    @Override
    public void remove(Person p) {
        super.remove(p);
        end();
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {
    }
}
