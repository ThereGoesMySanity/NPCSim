package map;

import tasks.Task;
import tasks.TownTask;
import tasks.town.Soldier;
import tasks.town.Fight;

import java.util.Map;

import static main.Main.*;
import static util.Variables.Ints.MONSTER_CHANCE;

public class Fort extends Town {

    public Fort(String name, Map<Class<? extends TownTask>, Integer> classMap, int startPop, int danger,
                String... surnames) {
        super(name, classMap, startPop, danger, surnames);
    }

    @Override
    public void modifyTasks(Map<Class<? extends TownTask>, Integer> cm) {
        super.modifyTasks(cm);
        cm.put(Soldier.class, 3);
    }
    @Override
    protected void spawnMonster() {
        if (rand.nextInt(10 * vars.get(MONSTER_CHANCE)) < danger) {
            Task t = taskMan.newTask(Fight.class, this);
            addTask(t);
        }
    }

}
