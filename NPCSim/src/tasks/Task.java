package tasks;

import people.Person;
import people.Stats.Stat;
import ui.map.TaskPane;

import java.util.ArrayList;

import static main.Main.taskMan;
import static main.Main.vars;
import static util.Variables.Doubles.TASK_MOD_WEIGHT;

public abstract class Task {
    private boolean template;
    private ArrayList<Person> participants = new ArrayList<>();
    private int maxSize;
    private int id;
    private boolean unique;

    public Task(boolean temp, int size) {
        this(temp, size, true);
    }
    public Task(boolean temp, int size, boolean unique) {
        template = temp;
        maxSize = size;
        this.unique = unique;
    }

    public Task(boolean temp) {
        this(temp, -1);
    }

    public void add(Person p) {
        participants.add(p);
    }

    public void remove(Person p) {
        participants.remove(p);
    }

    public int size() {
        return participants.size();
    }

    public ArrayList<Person> people() {
        return participants;
    }

    public void end() {
        taskMan.endTask(this);
        ArrayList<Person> pr = participants;
        participants = new ArrayList<Person>();
        pr.forEach(p -> p.removeTask(this));
    }

    public void groupInteract(double mean, double stdev) {
        participants.stream()
                .flatMap(p -> participants.stream().map(p1 -> new Person[]{p, p1}))
                .forEach(p -> Person.interact(p[0], p[1], mean, stdev));
    }

    public boolean isTemplate() {
        return template;
    }

    public double getStatWeight(Person p, Stat s) {
        return Math.pow(vars.get(TASK_MOD_WEIGHT), p.getStatMod(s));
    }

    public double getWorkWeight(Person p) {
        return weightSub(p);
    }

    public double getAddWeight(Person p) {
        if (maxSize != -1 && size() >= maxSize) return 0;
        //Can't add two tasks of the same type if unique
        if (unique && p.getTasks().stream()
                .anyMatch(t -> t.getClass().isInstance(this))) return 0;
        else return weightSub(p);
    }

    public int getID() {
        return id;
    }

    protected void setID(int id) {
        this.id = id;
    }

    public boolean townTask() {
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public String toString(Person p) {
        return toString() + " (" + getWorkWeight(p) + ")";
    }

    public void addToPane(TaskPane pane) {
    }

    public void updatePane() {
    }

    /**
     * Override to return the add/work weight of the given task.
     * To have different add/work weights, override getAddWeight or getWorkWeight
     * @param p The person to weight
     * @return The weight of the given task [0, inf)
     */
    protected abstract double weightSub(Person p);

    /**
     * Works on this
     * @param p The person working
     * @return Whether or not the task was completed (triggers end of task if true)
     */
    public abstract boolean work(Person p);

    public abstract void update();

    public abstract void updatePost();
}
