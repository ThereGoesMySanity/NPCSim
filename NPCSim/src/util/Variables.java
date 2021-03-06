package util;

import java.util.EnumMap;

import static util.Variables.Doubles.*;
import static util.Variables.Ints.*;

public class Variables {
    public enum Doubles {
        TASK_MOD_WEIGHT,
        CHA_REL_WEIGHT,
        TASK_CHANCE,
        TASK_WEIGHT,
        MARRIAGE_WEIGHT,
        FIGHT_WEIGHT,
        MOVE_WEIGHT,
        MONSTER_LEVEL_CHANCE,
        SPAWN_CHANCE,
    }

    public enum Ints {
        TASKS,
        MONSTER_CHANCE,
        ADVENTURER_WEIGHT,
        BIRTH_CHANCE,
    }

    private final EnumMap<Doubles, Double> vars = new EnumMap<>(Doubles.class);
    private final EnumMap<Ints, Integer> varInts = new EnumMap<>(Ints.class);

    public Variables() {
        vars.put(TASK_MOD_WEIGHT, 2.);
        vars.put(CHA_REL_WEIGHT, 1.);
        vars.put(TASK_CHANCE, 4.);
        vars.put(TASK_WEIGHT, 1.1);
        vars.put(MARRIAGE_WEIGHT, 0.1);
        vars.put(FIGHT_WEIGHT, 0.1);
        vars.put(MOVE_WEIGHT, 0.1);
        vars.put(MONSTER_LEVEL_CHANCE, 0.3);
        vars.put(SPAWN_CHANCE, 0.2);
        varInts.put(ADVENTURER_WEIGHT, 20);
        varInts.put(TASKS, 1);
        varInts.put(MONSTER_CHANCE, 10);
        varInts.put(BIRTH_CHANCE, 50);
    }

    public double get(Doubles v) {
        return vars.get(v);
    }

    public int get(Ints v) {
        return varInts.get(v);
    }

    public void set(Doubles v, double d) {
        vars.put(v, d);
    }

    public void set(Ints v, int i) {
        varInts.put(v, i);
    }
}
