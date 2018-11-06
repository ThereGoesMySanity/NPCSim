package tasks.town;

import static main.Main.rand;
import static main.Main.tables;
import static main.Main.vars;
import static people.Stats.Stat.CON;
import static people.Stats.Stat.DEX;
import static people.Stats.Stat.STR;
import static util.Variables.Doubles.FIGHT_WEIGHT;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import map.Town;
import people.Person;
import tasks.TownTask;
import things.Monster;
import ui.map.TaskPane;
import util.Convert;
import util.Weight;

public class Fight extends TownTask {
	private static final String[] columnNames = {"Person", "HP"};
	private int hp;
	private Monster monster;
	private Set<Person> worked = new HashSet<>();
	private HashMap<Person, Integer> squadHP = new HashMap<>();
	private JTable table;
	private JLabel hpLabel;
	public Fight(Town t) {
		super(t);
		if(t != null) {
			int level = 1;
			while (rand.nextBoolean()) level++;
			monster = tables.getMonster(t, level);
			hp = monster.getHP();
		}
	}
	@Override
	public double addWeightSub(Person p) {
		return (p.level - 2)
				* Math.max(getStatWeight(p, STR), getStatWeight(p, DEX))
				* vars.get(FIGHT_WEIGHT)
				/ monster.getCR();
	}
	@Override
	public boolean work(Person p) {
		worked.add(p);
		int dmg = Math.max(0, Math.max(p.getStatMod(STR), p.getStatMod(DEX)) 
								+ rand.nextInt(8) + (int)(p.level * 2));
		hp -= dmg;
		if (hp <= 0) {
			worked.forEach(o -> o.addXP(monster.getXP() / size()));
		}
		p.record("Did "+dmg+" damage in " + this);
		return hp <= 0;
	}
	@Override
	public void update() {
		worked.clear();
	}
	@Override
	public void remove(Person p) {
		squadHP.remove(p);
		super.remove(p);
	}
	@Override
	public void updatePost() {
		if(worked.size() > 0) {
			Person p = Weight.choose(worked);
			if(people().contains(p)) {
				//TODO: fix
				int newhp = squadHP.get(p) - monster.attack();
				if(newhp < 0) {
					p.die("their injuries");
					System.out.println("ahhhh");
				} else {
					squadHP.put(p, newhp); 
				}
			}
			groupInteract(2, 1.5);
		}
	}
	@Override
	public void add(Person p) {
		super.add(p);
		squadHP.put(p, 2+(4+p.getStatMod(CON)) * p.level);
	}
	@Override
	public void addToPane(TaskPane pane) {
		super.addToPane(pane);
		hpLabel = new JLabel("HP: " + hp);
		pane.addLabel(hpLabel);
		JTabbedPane jtp = pane.getTabs();
		JScrollPane example = new JScrollPane();
		jtp.addTab("Squad HP", null, example, null);
		
		table = new JTable();
		example.setViewportView(table);
		example.setPreferredSize(table.getPreferredSize());
	}
	@Override
	public void updatePane() {
		Convert.mapToTable(squadHP, table, columnNames);
		hpLabel.setText("HP: "+hp);
	}
	@Override
	public String toString() {
		return monster+" ("+people().size()+")";
	}
}
