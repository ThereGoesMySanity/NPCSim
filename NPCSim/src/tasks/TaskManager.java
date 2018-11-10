package tasks;

import main.Main;
import map.Town;
import tasks.other.*;
import tasks.town.*;
import ui.map.TaskDetailsPanel;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager implements Serializable {
    @SuppressWarnings("unchecked")
    private static final Class<? extends Task>[] taskList = new Class[]{
            Adventurer.class,
            Craft.class,
            Divorce.class,
            Hunt.class,
            Idle.class,
            Married.class,
            Marry.class,
            Study.class,
            Travel.class,
    };
    @SuppressWarnings("unchecked")
    private static final Class<? extends TownTask>[] townTasks = new Class[] {
            Blacksmith.class,
            College.class,
            Farm.class,
            Fight.class,
            Quest.class,
            Shop.class,
            Socialize.class,
            Soldier.class,
            Teach.class,
    };
    private final Task[] templates = new Task[taskList.length];
    public transient HashMap<Task, TaskDetailsPanel> panes = new HashMap<>();
    private final ArrayList<Task> activeTasks = new ArrayList<>();
    private transient ArrayList<Task> toRemove = new ArrayList<>();

    public TaskManager() {
        try {
            for (int i = 0; i < templates.length; i++) {
                templates[i] = taskList[i].getConstructor(boolean.class).newInstance(true);
            }
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Task[] tasks() {
        return templates;
    }

    public Task newTask(Task t) {
        if (t.isTemplate()) return newTask(t.getClass());
        else return t;
    }

    private <T extends Task> T newTask(Class<T> clazz, Town t) {
        try {
            return newTaskFinal(clazz.getConstructor(Town.class).newInstance(t));
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
    public <T extends Task> T newTask(Class<T> clazz) {
        try {
            return newTaskFinal(clazz.getConstructor(boolean.class).newInstance(false));
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T extends Task> T newTaskFinal(T task) {
        activeTasks.add(task);
        return task;
    }

    void endTask(Task t) {
        if (panes.containsKey(t)) {
            TaskDetailsPanel pane = panes.get(t);
            pane.refresh();
            pane.addLabel(new JLabel("Ended " + Main.time));
        }
        toRemove.add(t);
    }

    public void addTasks(Town t, Map<Class<? extends TownTask>, Integer> map) {
        for(Class<? extends TownTask> tt : townTasks) {
            for (int j = 0; j < map.getOrDefault(tt, 0); j++) {
                t.addTask(newTask(tt, t));
            }
        }
    }

    private void removeAll() {
        toRemove.forEach(activeTasks::remove);
        toRemove.clear();
    }

    public void updateAll() {
        activeTasks.forEach(Task::update);
        removeAll();
    }

    public void updateAllPost() {
        removeAll();
        activeTasks.forEach(Task::updatePost);
        removeAll();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        toRemove = new ArrayList<>();
        panes = new HashMap<>();
        in.defaultReadObject();
    }
}
