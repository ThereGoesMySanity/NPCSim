package tasks;

import main.Main;
import people.Person;
import people.PersonListener;
import people.Stats.Stat;
import ui.map.TaskDetailsPanel;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

import static main.Main.taskMan;
import static main.Main.vars;
import static ui.map.DetailsPanel.DetailsObject;
import static ui.map.DetailsPanel.Type;
import static util.Variables.Doubles.TASK_MOD_WEIGHT;

public abstract class Task implements DetailsObject, Serializable, PersonListener {
    private final boolean template;
    private final ArrayList<Person> participants = new ArrayList<>();
    private final int maxSize;
    private final boolean unique;
    private transient TaskDetailsPanel tdp;

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
        p.addListener(this);
    }

    public void remove(Person p) {
        participants.remove(p);
        p.removeListener(this);
    }

    public int size() {
        return participants.size();
    }

    public ArrayList<Person> people() {
        return participants;
    }

    public void end() {
        if(tdp != null) tdp.addLabel(new JLabel("Ended " + Main.time));
        taskMan.endTask(this);
        ArrayList<Person> pr = new ArrayList<>(participants);
        pr.forEach(p -> p.removeTask(this));
    }

    protected void groupInteract(double mean, double stdev) {
        participants.stream()
                .flatMap(p -> participants.stream().map(p1 -> new Person[]{p, p1}))
                .forEach(p -> Person.interact(p[0], p[1], mean, stdev));
    }

    boolean isTemplate() {
        return template;
    }

    protected double getStatWeight(Person p, Stat s) {
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

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public String toString(Person p) {
        return toString() + " (" + getWorkWeight(p) + ")";
    }

    public boolean isUnique() {
        return unique;
    }


    public void addToPane(TaskDetailsPanel pane) {
        tdp = pane;
    }
    public void removePane() {
        tdp = null;
    }

    public void updatePane() { }

    @Override
    public Type getType() {return Type.TASK;}

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

    @Override
    public void onChild(Person p, Person child) { }

    @Override
    public void onMarry(Person p, Person spouse) { }

    @Override
    public void onDeath(Person p) {
        participants.remove(p);
    }
}
