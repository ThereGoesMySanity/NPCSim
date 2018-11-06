package tasks;

import javax.swing.JLabel;

import map.Town;
import people.Person;
import ui.map.TaskPane;

public abstract class TownTask extends Task {
	private Town town;
	private JLabel townLabel;

	protected TownTask(int size, Town t) {
		super(false, size);
		town = t;
	}
	protected TownTask(Town t) {
		this(-1, t);
	}
	@Override
	public double getAddWeight(Person p) {
		if (!p.getTown().equals(town)) return 0;
		else return super.getAddWeight(p);
	}
	@Override
	public void end() {
		super.end();
		town.removeTask(this);
	}
	@Override
	public boolean townTask() 	{return true;}
	public Town getTown() 		{return town;}
	@Override
	public void addToPane(TaskPane tp) {
		townLabel = new JLabel(town.toString());
		tp.addLabel(townLabel);
	}
}
