package tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.ToIntFunction;

import javax.swing.JLabel;

import main.Main;
import map.Town;
import tasks.other.*;
import tasks.town.*;
import ui.map.TaskPane;

public class TaskManager {
	@FunctionalInterface
	public interface TaskFactory<T extends Task> {
		T newInstance(boolean t);
	}
	@FunctionalInterface
	public interface TownTaskFactory<T extends TownTask> {
		T newInstance(Town t);
	}
	private final TownTaskFactory<?>[] townTasks = {
			Blacksmith::new,
			Farm::new,
			Fight::new,
			Shop::new,
			Socialize::new,
	};
	private final TaskFactory<?>[] taskList = {
			Craft::new,
			Divorce::new,
			Hunt::new,
			Married::new,
			Marry::new,
			Study::new,
			Travel::new,
	};
	private Task[] templates = new Task[taskList.length];
	@SuppressWarnings("unchecked")
	private Class<? extends TownTask>[] townTaskClass = new Class[townTasks.length];
	private HashMap<Class<? extends Task>, Integer> taskMap = new HashMap<>(taskList.length);
	private HashMap<Class<? extends Task>, Integer> townTaskMap = new HashMap<>(townTasks.length);
	public HashMap<Task, TaskPane> panes = new HashMap<>();
	private ArrayList<Task> activeTasks = new ArrayList<>();
	private ArrayList<Task> toRemove = new ArrayList<>();
	public TaskManager() {
		for(int i = 0; i < templates.length; i++) {
			templates[i] = taskList[i].newInstance(true);
			templates[i].setID(i);
			taskMap.put(templates[i].getClass(), i);
		}
		for(int i = 0; i < townTasks.length; i++) {
			townTaskClass[i] = townTasks[i].newInstance(null).getClass();
			townTaskMap.put(townTaskClass[i], i);
		}
	}
	public Task[] tasks() { return templates; }
	public Task newTask(Task t) {
		if(t.isTemplate()) return newTask(t.getClass(), null);
		else return t;
	}
	public <T extends Task> T newTask(Class<T> clazz, Town t) {
		Task task;
		if(townTaskMap.containsKey(clazz)) {
			task = clazz.cast(newTask(townTaskMap.get(clazz), t));
		} else {
			task = newTask(taskMap.get(clazz));
		}
		return clazz.cast(task);
	}
	@SuppressWarnings("unchecked")
	public <T extends Task> T newTask(int i) {
		T task = (T) taskList[i].newInstance(false);
		task.setID(i);
		activeTasks.add(task);
		return newTaskFinal(task);
	}
	@SuppressWarnings("unchecked")
	public <T extends TownTask> T newTask(int i, Town t) {
		T task = (T) townTasks[i].newInstance(t);
		task.setID(i);
		return newTaskFinal(task);
	}
	public <T extends Task> T newTaskFinal(T task) {
		activeTasks.add(task);
		return task;
	}
	public void endTask(Task t) {
		if(panes.containsKey(t)) {
			TaskPane pane = panes.get(t);
			pane.update();
			pane.addLabel(new JLabel("Ended "+Main.time));
		}
		toRemove.add(t);
	}
	public void addTasks(Town t) {
		Arrays.stream(townTasks).map(ttf -> newTaskFinal(ttf.newInstance(t))).forEach(t::addTask);
	}
	public void addTasks(Town t, ToIntFunction<Class<? extends TownTask>> tif) {
		for(int i = 0; i < townTasks.length; i++) {
			for(int j = 0; j < tif.applyAsInt(townTaskClass[i]); j++) {
				t.addTask(newTask(i, t));
			}
		}
	}
	public void removeAll() {
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
