package map;

import people.Person;
import tasks.Task;
import tasks.TownTask;
import tasks.town.*;
import util.Weight;

import java.util.*;
import java.util.stream.Stream;

import static main.Main.*;
import static people.Stats.Stat.CHA;
import static people.Stats.Stat.CON;
import static util.Variables.Ints.MONSTER_CHANCE;

public class Town {
    public interface Listener {
        void onAdd(Person p);

        void onRemove(Person p);
    }

    public Set<Person> residents = new HashSet<>();
    private Set<Person> travellers = new HashSet<>();
    private ArrayList<Town> roads = new ArrayList<>();
    private ArrayList<Task> jobs = new ArrayList<>();
    private ArrayList<Listener> listeners = new ArrayList<>();
    private String name;
    public int food;
    public int danger;
    private String[] surnames;

    public Town(String name, Map<Class<? extends TownTask>, Integer> classMap, int startPop, int danger,
                String... surnames) {
        this.name = name;
        this.surnames = surnames;
        this.danger = danger;
        for (int i = 0; i < startPop; i++) { //populate
            new Person(this);
        }
        food = startPop * 12;
        modifyTasks(classMap);
        taskMan.addTasks(this, c -> classMap.getOrDefault(c, 0));
    }

    public void modifyTasks(Map<Class<? extends TownTask>, Integer> classMap) {
        if(residents.size() >= 30) {
            classMap.put(College.class, 1);
            classMap.put(Teach.class, 5);
        } else {
            classMap.put(Teach.class, residents.size() / 10);
        }
        classMap.put(Blacksmith.class, 1);
        classMap.put(Shop.class, 1);
    }

    public void update() {
        spawnMonster();
        socialize();
        food -= residents.size();
        if (food < 0) {
            Person p = Weight.weightedChoice(p1 -> Math.pow(0.2, p1.getStatMod(CON)), residents);
            p.die("starvation");
            food = 0;
        }
        List<Person> temp = new ArrayList<>(residents);
        temp.forEach(Person::update);
        temp.forEach(Person::work);
    }

    private void socialize() {
        for (int i = 0; i < rand.nextInt(residents.size() / 10 + 1); i++) {
            Person p = Weight.weightedChoice(p1 -> Math.pow(4, p1.getStatMod(CHA)), residents);
            Task t = taskMan.newTask(Socialize.class, this);
            p.addTask(t);
            jobs.add(t);
        }
    }

    protected void spawnMonster() {
        if (rand.nextInt(10 * vars.get(MONSTER_CHANCE)) < danger) {
            Task t = taskMan.newTask(Fight.class, this);
            addTask(t);
        }
    }

    public void farm(int i) {
        food += i;
    }

    public double foodWeight() {
        if (food < Math.max(residents.size() * 2, 10)) return 10000;
        return (double) residents.size() * 120 / food;
    }

    public void addRoad(Town t) {
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
        p.setTown(null);
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

    public Stream<Task> jobs() {
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

}
