package tasks.town;

import map.Town;
import people.Person;
import tasks.TownTask;

public class Farm extends TownTask {

	public Farm(Town t) {
		super(3, t);
	}

	@Override
	public double addWeightSub(Person p) {
		return getTown().foodWeight();
	}

	@Override
	public boolean work(Person p) {
		p.getTown().farm();
		return false;
	}

	@Override
	public void update() {}

	@Override
	public void updatePost() {
		groupInteract(0.1, 1);
	}
	@Override
	public boolean equals(Object o) {
		return o instanceof Farm;
	}
}