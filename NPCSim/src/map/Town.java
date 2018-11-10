package map;

import people.Person;
import tasks.Task;
import tasks.TownTask;
import tasks.town.*;
import util.Weight;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

import static main.Main.*;
import static people.Stats.Stat.CON;
import static ui.map.DetailsPanel.DetailsObject;
import static ui.map.DetailsPanel.Type;
import static util.Variables.Ints.MONSTER_CHANCE;
import static util.Variables.Ints.SPAWN_WEIGHT;

public class Town implements DetailsObject, Serializable {
    public interface Listener {
        void onAdd(Person p);
        void onRemove(Person p);
    }

    public final Set<Person> residents = new HashSet<>();
    private final Set<Person> travellers = new HashSet<>();
    private final ArrayList<Town> roads = new ArrayList<>();
    private final ArrayList<Task> jobs = new ArrayList<>();
    private transient ArrayList<Listener> listeners = new ArrayList<>();
    private final String name;
    public int food;
    public final int danger;
    private final int startPop;
    private final String[] surnames;

    public Town(String name, Map<Class<? extends TownTask>, Integer> classMap, int startPop, int danger,
                String... surnames) {
        this.name = name;
        this.startPop = startPop;
        this.surnames = surnames;
        this.danger = danger;
        for (int i = 0; i < startPop; i++) { //populate
            new Person(this);
        }
        food = startPop * 12;
        Map<Class<? extends TownTask>, Integer> classMap_final = new HashMap<>(classMap);
        modifyTasks(classMap_final);
        taskMan.addTasks(this, classMap_final);
    }

    public void modifyTasks(Map<Class<? extends TownTask>, Integer> classMap) {
        if(rand.nextInt(2) + residents.size() / 10 > 3) {
            classMap.put(College.class, 1);
            classMap.put(Teach.class, 5);
        } else {
            classMap.put(Teach.class, residents.size() / 10);
        }
        classMap.put(Blacksmith.class, 1);
        classMap.put(Shop.class, 1);
    }

    public void update() {
        if(rand.nextInt(vars.get(SPAWN_WEIGHT) * 10 / startPop) == 0
                || residents.size() < 5) residents.add(new Person(this, 15));
        taskMan.addTasks(this, Map.of(
                Socialize.class, rand.nextInt(residents.size() / 10 + 1),
                Fight.class, rand.nextInt(10 * vars.get(MONSTER_CHANCE)) < danger? 1 : 0
        ));
        food -= residents.size();
        if (food < 0) {
            Person p = Weight.weightedChoice(p1 -> Math.pow(0.2, p1.getStatMod(CON)), residents);
            p.die("starvation");
            food = 0;
        }
        List<Person> temp = new ArrayList<>(residents);
        temp.addAll(travellers);
        temp.forEach(Person::update);
        temp.forEach(Person::work);
    }

    public void farm(int i) {
        food += i;
    }

    public double foodWeight() {
        if (food < Math.max(residents.size() * 2, 10)) return 10000;
        return (double) residents.size() * 120 / food;
    }

    void addRoad(Town t) {
        roads.add(t);
    }

    public void add(Person p) {
        residents.add(p);
        addBase(p);
    }

    public void addTraveller(Person p) {
        travellers.add(p);
        addBase(p);
    }

    private void addBase(Person p) {
        p.setTown(this);
        listeners.forEach(l -> l.onAdd(p));
    }

    public void remove(Person p) {
        residents.remove(p);
        travellers.remove(p);
        listeners.forEach(l -> l.onRemove(p));
        p.getTasks().removeIf(jobs::contains);
        p.setTown(null);
    }

    @Override
    public Type getType() {
        return Type.TOWN;
    }


    public void addListener(Listener pl) {
        listeners.add(pl);
    }

    public String[] locales() {
        return surnames;
    }

    public ArrayList<Town> destinations() {
        return roads;
    }

    public Stream<Person> residents() {
        return residents.stream();
    }

    public Stream<Person> travellers() {
        return travellers.stream();
    }

    public Stream<Person> people() {
        return Stream.concat(residents(), travellers());
    }

    public Stream<Task> jobs(Person p) {
        if (!residents.contains(p)) return Stream.of();
        else return jobs.stream();
    }

    private Stream<Task> jobs() {
        return jobs.stream();
    }

    public void addTask(Task t) {
        jobs.add(t);
    }

    public void removeTask(TownTask townTask) {
        jobs.remove(townTask);
    }

    public <T extends Task> T getRandomTask(Class<T> c) {
        return c.cast(Weight.choose(() -> jobs().filter(c::isInstance)));
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Task> getTasks() {
        return jobs;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        listeners = new ArrayList<>();
    }

}
