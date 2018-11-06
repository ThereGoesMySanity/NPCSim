package main;

import java.util.Random;

import map.Map;
import tasks.TaskManager;
import util.Tables;
import util.Time;
import util.Variables;

public class Main {
	public static Random rand = new Random();
	public static Variables vars = new Variables();
	public static TaskManager taskMan = new TaskManager();
	public static Tables tables = new Tables();
	public static Time time = new Time(1700);
	public Map map = new Map();
	public void update() {
		taskMan.updateAll();
		map.update();
		taskMan.updateAllPost();
		time.pass();
	}
	public void update(int i) {
		for(int j = 0; j < i; j++) update();
	}
}
