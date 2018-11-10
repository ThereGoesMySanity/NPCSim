package main;

import map.AreaMap;
import tasks.TaskManager;
import util.Tables;
import util.Time;
import util.Variables;

import java.io.*;
import java.util.Random;
import java.util.function.BooleanSupplier;

public class Main {
    public static Random rand = new Random();
    public static final Variables vars = new Variables();
    public static TaskManager taskMan = new TaskManager();
    public static final Tables tables = new Tables();
    public static Time time = new Time(1700);
    public AreaMap map = new AreaMap();

    public void update() {
        taskMan.updateAll();
        map.update();
        taskMan.updateAllPost();
        time.pass();
    }

    public void update(BooleanSupplier b) {
        while(b.getAsBoolean()) update();
    }

    public void update(int i) {
        for (int j = 0; j < i; j++) update();
    }

    public void save(File file) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(rand);
            oos.writeObject(taskMan);
            oos.writeObject(map);
            oos.writeObject(time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void load(File file) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            rand = (Random) ois.readObject();
            taskMan = (TaskManager) ois.readObject();
            map = (AreaMap) ois.readObject();
            time = (Time) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
