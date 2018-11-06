package tasks.other;

import static main.Main.rand;

import java.util.stream.Collectors;

import map.Town;
import people.Person;
import tasks.Task;
import util.Weight;

public class Travel extends Task {
	public Travel(boolean temp) {
		super(temp, 1);
	}

	@Override
	protected double addWeightSub(Person p) {
		if(p.getTown().residents.size() < 5) return 0;
		else return 0;
	}

	@Override
	public boolean work(Person p) {
		if (rand.nextInt(10) == 0) {
			if(rand.nextInt(10) == 0) {
				Town town = p.getTown();
				town.remove(p);
				town.add(p);
				p.record("Settled down in "+town);
			} else {
				Town town = Weight.choose(p.getTown().destinations());
				p.getTown().remove(p);
				town.addTraveller(p);
				p.record("Travelled to "+town);
			}
		}
		assert p.getTown() != null;
		return false;
	}

	@Override
	public void add(Person p) {
		super.add(p);
		p.getTasks().removeAll(p.getTasks().stream()
				.filter(Task::townTask)
				.collect(Collectors.toList())
				);
	}

	@Override
	public void update() {
	}

	@Override
	public void updatePost() {
	}
}
