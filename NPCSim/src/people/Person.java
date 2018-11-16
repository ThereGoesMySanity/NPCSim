package people;

import map.Town;
import people.Stats.Stat;
import tasks.Task;
import util.Dice;
import util.TreePath;
import util.Weight;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static main.Main.*;
import static people.Stats.Stat.*;
import static ui.map.DetailsPanel.DetailsObject;
import static ui.map.DetailsPanel.Type;
import static util.Variables.Doubles.CHA_REL_WEIGHT;
import static util.Variables.Doubles.TASK_CHANCE;
import static util.Variables.Ints.TASKS;

public class Person implements DetailsObject, Serializable, TreePath.Children<Person> {
    public final Stats stats = new Stats();
    private boolean dead = false;
    private String notes = "";
    public String personality = tables.getGenerator("personality").generate();
    private String first, last;
    public int level;
    public int xp;
    public final int gender = rand.nextInt(2);
    private int months;
    private Town town;
    private final Race race;
    public Person spouse;
    private Dice attack = new Dice("1d6");
    public final HashMap<Person, Double> relationships = new HashMap<>();
    private final ArrayList<Person> children = new ArrayList<>();
    private final ArrayList<String> history = new ArrayList<>();
    private final ArrayList<Task> tasks = new ArrayList<>();
    private final Alignment alignment = new Alignment();
    private final ArrayList<PersonListener> listeners = new ArrayList<>();

    private static final int[] levels = {
            0, 300, 900, 2700, 6500, 14000, 23000, 34000,
            48000, 64000, 85000, 100000, 120000, 130000,
            165000, 195000, 225000, 265000, 305000, 355000
    };

    public Person(Town t) {
        this(t, Weight.weightedChoice(Race::getChance, Race.values()));
    }

    private Person(Town t, Race r) {
        this(t, r.getRandomAge(), r);
    }

    public Person(Town t, int age) {
        this(t, age, Weight.weightedChoice(Race::getChance, Race.values()));
    }

    public Person(Town t, int age, Race r) {
        this(t, tables.getLastName(t, r), age, r);
    }

    public Person(Town t, String last, int age, Race r) {
        first = tables.getFirstName(gender, r);
        race = r;
        this.last = last;
        this.months = age * 12;
        level = 1 + (age>20?(int)(rand.nextGaussian() * 0.1):0);
        if(level < 1) level = 1;
        xp = levels[level-1];
        t.add(this);
    }

    public void addTask(Task t) {
        addTaskFinal(taskMan.newTask(t));
    }

    private void addTaskFinal(Task t) {
        record("Added task " + t);
        tasks.add(t);
        t.add(this);
    }

    public void removeTask(Task t) {
        record("Removed task " + t);
        t.remove(this);
        tasks.remove(t);
    }

    private boolean canAdd(Task t) {
        return !t.isUnique() || canAdd(t.getClass());
    }

    public boolean canAdd(Class<? extends Task> clazz) {
        return tasks.stream().noneMatch(t -> t.isUnique() && clazz.isInstance(t));
    }

    public void update() {
        if (getAge() >= 15 && town != null) {
            while (rand.nextDouble() * vars.get(TASK_CHANCE) - tasks.size() > 0) {
                Task t = Weight.weightedChoice(this::taskAddWeight,
                        () -> Stream.concat(
                                Arrays.stream(taskMan.tasks()),
                                town.jobs(this)
                        ).filter(this::canAdd));
                if (t != null) addTask(t);
                else break;
            }
        }  //kids are boring
        if (getAge() > race.getAge()) {
            if (rand.nextDouble() < getAge() / (100. * race.getAge())) {
                die("old age");
            }
        }
        months++;
    }

    public void addXP(int xp) {
        this.xp += xp;
        record("Gained " + xp + " xp");
        if (this.xp >= levels[level]) {
            level++;
        }
    }

    public void work() {
        for (int i = 0; !dead && !tasks.isEmpty() && i < vars.get(TASKS); i++) {
            workOn(Weight.weightedChoice(this::taskWorkWeight, tasks));
        }
    }
    private void workOn(Task t) {
        if (t != null) {
            record("Working on " + t);
            if (t.work(this)) {
                t.people().forEach(p -> p.record("Completed " + t));
                t.end();
            }
        }
    }

    public void die(String reason) {
        listeners.forEach(pl -> pl.onDeath(this));
        listeners.clear();
        if (spouse != null) spouse.spouse = null;
        record("Died at " + getAge() + " of " + reason);
        tasks.clear();
        dead = true;
    }

    public double getRel(Person p) {
        if (p == this || !relationships.containsKey(p)) {
            return 0.;
        }
        return relationships.get(p);
    }

    private void interact(Person p, double mean, double stdev) {
        if (p != this) changeRel(p, rand.nextGaussian() * stdev
                + mean
                + (getRel(p)
                  + p.getStatMod(CHA) * vars.get(CHA_REL_WEIGHT)
                ) * stdev / 10);
    }

    public static void interact(Person p1, Person p2, double mean, double stdev) {
        double newMean = rand.nextGaussian() * stdev + mean;
        p1.interact(p2, newMean, stdev / 4);
        p2.interact(p1, newMean, stdev / 4);
    }

    private void changeRel(Person p, double delta) {
        relationships.put(p, (getRel(p) + delta) / 1.8);
    }

    public void marry(Person p) {
        spouse = p;
        if (gender == 1) {
            last = spouse.last;
        }
        listeners.forEach(pl -> pl.onMarry(this, p));
    }
    public void addListener(PersonListener pl) {
        listeners.add(pl);
    }
    public void removeListener(PersonListener pl) {
        listeners.remove(pl);
    }

    public void addChild(Person p) {
        children.add(p);
        listeners.forEach(pl -> pl.onChild(this, p));
    }

    public ArrayList<Person> getChildren() {return children;}

    public void divorce() {
        spouse = null;
    }

    public Race getRace() {
        return race;
    }

    public int gender() {
        return gender;
    }

    public int getAge() {
        return months / 12;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town t) {
        this.town = t;
    }

    public void record(String s) {
        history.add(s);
    }

    public int getStat(Stat s) {
        return stats.get(s);
    }

    public int getStatMod(Stat s) {
        return stats.getMod(s);
    }

    public void setStat(Stat s, int val) {
        stats.set(s, val);
    }

    private double taskWeight(Task t) {
        return 1;
        //TODO
    }

    private double taskAddWeight(Task t) {
        return t.getAddWeight(this) * taskWeight(t);
    }

    private double taskWorkWeight(Task t) {
        return t.getWorkWeight(this) * taskWeight(t);
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void setName(String f, String l) {
        first = f;
        last = l;
    }
    public void newLastName(String file) {
        last = tables.chooseName("surnames_"+file);
    }

    public String getLastName() {
        return last;
    }

    @Override
    public String toString() {
        return first + " " + getLastName();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public int attack() {
        return attack.roll() + level * 2 + Math.max(getStatMod(STR), getStatMod(DEX));
    }

    public void setAttack(String attack) {
        this.attack = new Dice(attack);
    }

    @Override
    public Type getType() {
        return Type.PERSON;
    }

    @Override
    public Stream<Person> children() {
        return relationships.keySet().stream();
    }
}