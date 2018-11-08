package map;

import tasks.TownTask;
import tasks.town.Soldier;

import java.util.Map;

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
}
