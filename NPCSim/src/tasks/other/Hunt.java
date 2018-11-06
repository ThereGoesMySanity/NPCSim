package tasks.other;

import people.Person;
import tasks.Task;

public class Hunt extends Task{

	public Hunt(boolean temp) {
		super(temp);
	}

	@Override
	protected double addWeightSub(Person p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean work(Person p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePost() {
		// TODO Auto-generated method stub
		
	}
}
