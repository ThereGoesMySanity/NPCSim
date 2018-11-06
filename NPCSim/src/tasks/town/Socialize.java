package tasks.town;

import map.Town;
import people.Person;
import tasks.TownTask;

public class Socialize extends TownTask {
	public Socialize(Town t) {
		super(t);
	}
	
	@Override
	public double getWorkWeight(Person p) {
		return 100;
	}
	@Override
	public double addWeightSub(Person p) {
		double sum = Math.max(0, people().stream().mapToDouble(p::getRel).sum());
		return Math.log1p(sum)+1;
	}

	@Override
	public void updatePost() {
		groupInteract(0.5, 1);
		end();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Socialize");
		if(people().size() > 0) {
			sb.append(" with ");
			people().forEach(p-> sb.append(p+", "));
			sb.setLength(sb.length()-2);
		}
		return sb.toString();
	}

	@Override
	public void update() {}
	@Override
	public boolean work(Person p) {return false;}
}
