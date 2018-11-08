package tasks;

import main.Main;
import map.Town;
import tasks.other.*;
import tasks.town.*;
import ui.map.TaskPane;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.ToIntFunction;

public class TaskManager {
    @SuppressWarnings("unchecked")
    private final Class<? extends Task>[] taskList = new Class[]{
            Adventurer.class,
            Craft.class,
            Divorce.class,
            Hunt.class,
            Married.class,
            Marry.class,
            Study.class,
            Travel.class,
    };
    @SuppressWarnings("unchecked")
    private final Class<? extends TownTask>[] townTasks = new Class[] {
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
    private Task[] templates = new Task[taskList.length];
    public HashMap<Task, TaskPane> panes = new HashMap<>();
    private ArrayList<Task> activeTasks = new ArrayList<>();
    private ArrayList<Task> toRemove = new ArrayList<>();

    public TaskManager() {
        try {
            for (int i = 0; i < templates.length; i++) {
                templates[i] = taskList[i].getConstructor(boolean.class).newInstance(true);
                templates[i].setID(i);
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

    private Task newTask(int i) {
        return newTask(taskList[i]);
    }

    private TownTask newTask(int i, Town t) {
        return newTask(townTasks[i], t);
    }

    public <T extends Task> T newTask(Class<T> clazz, Town t) {
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
            TaskPane pane = panes.get(t);
            pane.update();
            pane.addLabel(new JLabel("Ended " + Main.time));
        }
        toRemove.add(t);
    }

    public void addTasks(Town t, ToIntFunction<Class<? extends TownTask>> tif) {
        for (int i = 0; i < townTasks.length; i++) {
            for (int j = 0; j < tif.applyAsInt(townTasks[i]); j++) {
                t.addTask(newTask(i, t));
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

    public int tasksSize() {
        return townTasks.length + taskList.length;
    }
}
