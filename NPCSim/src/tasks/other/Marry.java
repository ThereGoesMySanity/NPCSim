package tasks.other;

import people.Person;
import tasks.Task;

import java.util.Comparator;
import java.util.function.Consumer;

import static main.Main.taskMan;
import static main.Main.vars;
import static util.Variables.Doubles.MARRIAGE_WEIGHT;

public class Marry extends Task {
    public Person spouse1, spouse2;
    private int work;

    public Marry(boolean t) {
        super(t);
        work = 0;
    }

    @Override
    public double weightSub(Person p) {
        if (p.getAge() < 18 || p.spouse != null) return 0;
        Person spouse = getSpouseCandidate(p);
        if (spouse == null) return 0;
        return Math.max(0, p.getRel(spouse)
                * vars.get(MARRIAGE_WEIGHT)
                * (spouse.getRace() != p.getRace() ? 0.5 : 1));
    }

    @Override
    public double getWorkWeight(Person p) {
        if (spouse1 == null || spouse2 == null) return 0;
        return Math.pow(3, p.getRel(getOther(p)));
    }

    private static Person getSpouseCandidate(Person p) {
        return p.getTown().residents()
                .filter(p1 ->
                        p1.spouse == null
                                && p1.gender() != p.gender()
                                && p1.getAge() >= Math.max(18, p.getAge() / 2 + 7)
                )
                .max(Comparator.comparingDouble(p::getRel)).orElse(null);
    }

    @Override
    public boolean work(Person p) {
        boolean marry = ++work >= 10;
        if (marry) {
            forBoth(p1 -> p1.marry(getOther(p1)));
            forBoth(p1 -> p1.record("Married " + getOther(p1)));
            Task married = taskMan.newTask(Married.class);
            forBoth(p1 -> p1.addTask(married));
            Person.interact(spouse1, spouse2, 5, 2);
        }
        return marry;
    }

    @Override
    public void end() {
        super.end();
        spouse1 = spouse2 = null;
    }

    @Override
    public void update() {
    }

    @Override
    public void updatePost() {
    }

    @Override
    public void add(Person person) {
        super.add(person);
        if (person.spouse != null) end();
        if (spouse1 != null || spouse2 != null) return;
        spouse1 = getSpouseCandidate(person);
        spouse2 = person;
        forBoth(p -> p.record(spouse2 + " proposed to " + spouse1 + "!"));
        if (getSpouseCandidate(spouse1) == person
                && !spouse1.getTasks().stream().anyMatch(Marry.class::isInstance)) {
            forBoth(p -> p.record(spouse1 + " accepted!"));
            spouse1.addTask(this);
        } else {
            forBoth(p -> p.record(spouse1 + " rejected..."));
            end();
        }
    }

    public void forBoth(Consumer<Person> c) {
        c.accept(spouse1);
        c.accept(spouse2);
    }

    public Person getOther(Person p) {
        if (spouse2.equals(p)) return spouse1;
        return spouse2;
    }

    @Override
    public String toString() {
        return "The marriage of " + spouse1 + " and " + spouse2;
    }
}
