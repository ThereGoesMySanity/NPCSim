package tasks.town;

import map.Fort;
import map.Town;
import people.Person;
import tasks.TownTask;
import things.Monster;
import ui.map.TaskDetailsPanel;
import util.Convert;
import util.Weight;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static main.Main.*;
import static people.Stats.Stat.*;
import static util.Variables.Doubles.FIGHT_WEIGHT;
import static util.Variables.Doubles.MONSTER_LEVEL_CHANCE;

public class Fight extends TownTask {
    private static final String[] columnNames = {"Person", "HP"};
    private int hp;
    private final Monster monster;
    private final Set<Person> worked = new HashSet<>();
    private final HashMap<Person, Integer> squadHP = new HashMap<>();
    private JTable table;
    private JLabel hpLabel;

    public Fight(Town t) {
        super(t);
        double level = 0.5 + rand.nextGaussian() * vars.get(MONSTER_LEVEL_CHANCE);
        if(level < 0) level = 0;
        monster = tables.monsterGen.getMonster(t, (float) level);
        hp = monster.getHP();
    }

    @Override
    public double getAddWeight(Person p) {
        if(getTown() instanceof Fort) return 0;
        return super.getAddWeight(p);
    }

    @Override
    public double weightSub(Person p) {
        return (p.level - monster.getCR() * 2)
                * Math.max(getStatWeight(p, STR), getStatWeight(p, DEX))
                * vars.get(FIGHT_WEIGHT);
    }

    @Override
    public double getWorkWeight(Person p) {
        return p.level * vars.get(FIGHT_WEIGHT);
    }

    @Override
    public boolean work(Person p) {
        worked.add(p);
        int dmg = p.attack();
        hp -= dmg;
        if (hp <= 0) {
            people().forEach(o -> o.addXP(monster.getXP() / people().size()));
        }
        p.record("Did " + dmg + " damage in " + this);
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
        if (worked.size() > 0) {
            Person p = Weight.choose(worked);
            int newhp = squadHP.get(p) - monster.attack();
            if (newhp < 0) {
                p.die("their injuries");
            } else {
                squadHP.put(p, newhp);
            }
            groupInteract(2, 1.5);
        }
    }

    @Override
    public void add(Person p) {
        super.add(p);
        squadHP.put(p, 3 + (5 + p.getStatMod(CON)) * p.level);
    }

    @Override
    public void addToPane(TaskDetailsPanel pane) {
        super.addToPane(pane);
        hpLabel = new JLabel("HP: " + hp);
        pane.addLabel(hpLabel);
        JScrollPane example = new JScrollPane();
        pane.addTab("Squad HP", example);

        table = new JTable();
        example.setViewportView(table);
        example.setPreferredSize(table.getPreferredSize());
    }

    @Override
    public void updatePane() {
        Convert.mapToTable(squadHP, table, columnNames);
        hpLabel.setText("HP: " + hp);
    }

    @Override
    public String toString() {
        return monster + " (" + people().size() + " people)";
    }

    public double getCR() {
        return monster.getCR();
    }
}
