package people;

import static main.Main.rand;
import static main.Main.tables;
import static main.Main.taskMan;
import static main.Main.vars;
import static people.Stats.Stat.CHA;
import static util.Variables.Doubles.CHA_REL_WEIGHT;
import static util.Variables.Doubles.TASK_CHANCE;
import static util.Variables.Doubles.TASK_WEIGHT;
import static util.Variables.Ints.TASKS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import map.Town;
import people.Stats.Stat;
import tasks.Task;
import util.Weight;

public class Person {
	private String first, last;
	public Stats stats = new Stats();
	public int level = 1;
	public int xp;
	public int gender = rand.nextInt(2);
	private int months;
	public String notes = "";
	private Town town;
	public Person spouse;
	private boolean dead = false;
	private Race race;
	public HashMap<Person, Double> relationships = new HashMap<>();
	private ArrayList<Task> tasks = new ArrayList<>();
	private ArrayList<String> history = new ArrayList<>();
	private Alignment alignment = new Alignment();
	private double[] taskWeights = new double[taskMan.tasksSize()];

	private static final int[] levels = { 
			0, 300, 900, 2700, 6500, 14000, 23000, 34000, 
			48000, 64000, 85000, 100000, 120000, 130000, 
			165000, 195000, 225000, 265000, 305000, 355000
	};
	
	public Person(Town t) {
		this(t, Weight.weightedChoice(Race::getChance, Race.values()));
	}
	public Person(Town t, Race r) {
		this(t, tables.getLastName(t, r), r.getRandomAge(), r);
	}
	public Person(Town t, String last, int age, Race r) {
		gender = rand.nextInt(2);
		first = tables.getFirstName(gender, r);
		race = r;
		this.last = last;
		this.months = age * 12;
		for(int i = 0; i < taskWeights.length; i++) {
			taskWeights[i] = rand.nextDouble() * (rand.nextBoolean()? -1 : 1);
			taskWeights[i] = Math.pow(vars.get(TASK_WEIGHT), taskWeights[i]);
		}
		level = 1;
		if(age > 20) level += rand.nextInt(5);
		xp = levels[level];
		t.add(this);
	}
	public void addTask(Task t) {
		addTaskFinal(taskMan.newTask(t));
	}
	public void addTask(Class<? extends Task> c) {
		addTaskFinal(taskMan.newTask(c, town));
	}
	private void addTaskFinal(Task t) {
		record("Added task "+t);
		tasks.add(t);
		t.add(this);
	}
	public void removeTask(Task t) {
		record("Removed task "+t);
		t.remove(this);
		tasks.remove(t);
	}
	public void update() {
		if (getAge() >= 10 && town != null) {
			while(rand.nextDouble() < vars.get(TASK_CHANCE) / (tasks.size() + 1)) {
				Task t = Weight.weightedChoice(this::taskAddWeight, 
						() -> Stream.concat(
								Arrays.stream(taskMan.tasks()), 
								town.jobs(this)
								).filter(t1 -> !tasks.contains(t1)));
				if(t != null) addTask(t);
				else break;
			}
		} else {
			//kids are boring
		}
		if(getAge() > race.getAge()) {
			if(rand.nextDouble() < getAge() / (100. * race.getAge())) {
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
		for(int i = 0; !dead && !tasks.isEmpty() && i < vars.get(TASKS); i++) {
			Task t = Weight.weightedChoice(this::taskWorkWeight, tasks);
			if(t != null) {
				record("Working on "+t+" "+taskWorkWeight(t));
				if(t.work(this)) {
					t.people().forEach(p -> p.record("Completed "+t));
					t.end();
				}
			}
		}
	}
	public void die(String reason) {
		tasks.forEach(t -> t.remove(this));
		town.remove(this);
		if (spouse != null) spouse.spouse = null;
		record("Died at "+getAge()+" of "+reason);
		tasks.clear();
		dead = true;
	}
	public double getRel(Person p) {
		if(p == this || !relationships.containsKey(p)) {
			return 0.;
		}
		return relationships.get(p);
	}
	public void interact(Person p) {
		interact(p, 0, 1);
	}
	public void interact(Person p, double mean, double stdev) {
		if (p != this) changeRel(p, rand.nextGaussian() * stdev + mean + getRel(p)*stdev/10);
	}
	public static void interact(Person p1, Person p2, double mean, double stdev) {
		double newMean = rand.nextGaussian() * stdev + mean;
		p1.interact(p2, newMean, stdev/4);
		p2.interact(p1, newMean, stdev/4);
	}
	public void changeRel(Person p, double delta) {
		delta += p.getStatMod(CHA) * vars.get(CHA_REL_WEIGHT);
		relationships.put(p, (getRel(p) + delta)/2);
	}
	public void marry(Person p) {
		spouse = p;
		if(gender == 1) {
			last = spouse.last;
		}
	}
	public void divorce() 			{spouse = null;}
	public Race getRace()			{return race;}
	public int gender() 			{return gender;}
	public int getAge() 			{return months/12;}
	public Town getTown() 			{return town;}
	public void setTown(Town t)		{this.town = t;}
	public void record(String s) 	{history.add(s);}
	public int getStat(Stat s) 	 	{return stats.get(s);}
	public int getStatMod(Stat s)	{return stats.getMod(s);}
	public void setStat(Stat s, int val){stats.set(s, val);}
	public double taskWeight(Task t)	{return taskWeights[t.getID()];}
	public double taskAddWeight(Task t) {return t.getAddWeight(this) * taskWeight(t);}
	public double taskWorkWeight(Task t){return t.getWorkWeight(this) * taskWeight(t);}
	public double townWeight(Town t) 	{return t.moveWeight(this);}
	public Alignment getAlignment() 	{return alignment;}
	public String getNotes() 			{return notes;}
	public void setNotes(String notes) 	{this.notes = notes;}
	public ArrayList<String> getHistory()	{return history;}
	public void setName(String f, String l) {first = f; last = l;}
	public String getLastName() 			{return last;}
	@Override
	public String toString() {return first + " " + getLastName();}
	public ArrayList<Task> getTasks() {return tasks;}
}