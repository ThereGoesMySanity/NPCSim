package tasks.other;

import people.Person;
import people.Race;
import tasks.Task;

import java.util.stream.Stream;

import static main.Main.rand;

public class Married extends Task {

    public Married(boolean temp) {
        super(temp);
    }

    @Override
    public double addWeightSub(Person p) {
        return -1;
    }

    @Override
    public double getWorkWeight(Person p) {
        return Math.max(0, p.getRel(p.spouse));
    }

    @Override
    public boolean work(Person p) {
        groupInteract(2, 2);
        Race babbyRace = Race.getRace(p.getRace(), p.spouse.getRace());
        if (babbyRace != null && p.getAge() < 40 && p.gender == 1 && rand.nextInt(50) == 0) {
            new Person(p.getTown(), p.getLastName(), 0, babbyRace);
            Stream.of(p, p.spouse).forEach(p1 -> p1.record("BABBY HAS CREATE"));
        }
        return false;
    }

    @Override
    public void update() {
        //		people().stream()
        //		.filter(p -> p.getRel(p.spouse) < 0)
        //		.findFirst().ifPresent(p -> {
        //			p.divorce();
        //			end();
        //		});
    }

    @Override
    public void add(Person p) {
        super.add(p);
    }

    @Override
    public void updatePost() {
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Married;
    }
}
