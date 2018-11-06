package tasks;
import static main.Main.rand;
import static main.Main.taskMan;
import static main.Main.vars;
import static util.Variables.Doubles.TASK_MOD_WEIGHT;

import java.util.ArrayList;

import people.Person;
import people.Stats.Stat;
import ui.map.TaskPane;

public abstract class Task {
	private boolean template;
	private ArrayList<Person> participants = new ArrayList<>();
	private int maxSize;
	private int id;
	public Task(boolean temp, int size) {
		template = temp;
		maxSize = size;
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
	public Person getRandomPerson() {
		return size() > 0? participants.get(rand.nextInt(size())) : null;
	}
	public int size() {
		return participants.size();
	}
	public boolean inTask(Person p) {
		return participants.contains(p);
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
		.flatMap(p -> participants.stream().map(p1 -> new Person[] {p, p1}))
		.forEach(p -> Person.interact(p[0], p[1], mean, stdev));
	}
	public boolean isTemplate() {
		return template;
	}
	
	public double getStatWeight(Person p, Stat s) {
		return Math.pow(vars.get(TASK_MOD_WEIGHT), p.getStatMod(s));
	}
	public double getWorkWeight(Person p) {
		return addWeightSub(p);
	}
	public double getAddWeight(Person p) {
		if (maxSize != -1 && size() >= maxSize) return 0;
		if (p.getTasks().contains(this)) return 0;
		else return addWeightSub(p);
	}
	public int getID() {
		return id;
	}
	protected void setID(int id) {
		this.id = id;
	}
	public boolean townTask() {return false;}
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	public String toString(Person p) {
		return toString() + " ("+getWorkWeight(p)+")";
	}
	public void addToPane(TaskPane pane) {}
	public void updatePane() {}

	protected abstract double addWeightSub(Person p);
	public abstract boolean work(Person p);
	public abstract void update();	
	public abstract void updatePost();
}
