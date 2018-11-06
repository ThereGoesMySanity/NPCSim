package map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class MapData {
	private Town[] towns = {
			//		 name				farms 	startPop 	danger 	monsters/names
			new Town("Northcastle",		1,		10,			6,		"desert", "mountain"),
			new Fort("Northcastle Keep",1,		10,			10,		"soldier", "desert", "mountain"),
			new Town("Hillborough",		2,		20,			5,		"forest", "hill", "grassland"),
			new Town("Oxingford",		3, 		20,			4,		"grassland", "hill"),
			new Town("Kerenford",		20,		40,			3,		"river", "city", "grassland"),
			new Town("Beren",			8,		40,			1, 		"sea", "city", "underwater", "grassland"),
			new Town("Leiderford",		8, 		30,			5,		"river", "forest", "city", "hill"),
			new Town("South Lintry",	5,		10,			8,		"river", "forest", "mountain", "city", "hill"),
			new Town("Bilsburg",		5,		20,			3,		"hill", "grassland"),
			new Town("Norport",			3, 		30,			1,		"sea", "underwater", "grassland"),
			new Town("Middenbury",		13,		20,			3,		"city", "grassland"),
			new Town("Leandea",			10, 	40,			4,		"river", "city", "mountain", "grassland"),
			new Town("Amfield",			5,		10,			4,		"desert", "grassland"),
			new Town("East Dorburg",	3,		10,			6,		"forest", "desert", "hill", "grassland"),
			new Town("Covender",		5,		10,			3,		"forest", "grassland"),
			new Town("Casterham",		10,		10,			5,		"river", "city", "grassland"),
			new Town("Marisbury", 		5,		10,			4,		"grassland", "mountain"),
			new Town("Southpool", 		6,		10,			9,		"swamp", "river", "city", "mountain"),
			new Town("Southfort City", 	6,		10,			7,		"forest", "city", "desert", "hill"),
			new Fort("Southfort", 		1,		10,			10,		"forest", "desert", "soldier", "mountain"),
	};
	private String[][] connections = {
			{"Northcastle Keep",	"Northcastle"},
			{"Northcastle",			"Hillborough"},
			{"Hillborough",			"Oxingford"},
			{"Hillborough",			"Kerenford"},
			{"Oxingford",			"Kerenford"},
			{"Kerenford",			"Beren"},
			{"Beren",				"Bilsburg"},
			{"Kerenford",			"Leiderford"},
			{"Leiderford",			"Bilsburg"},
			{"Leiderford",			"South Lintry"},
			{"Bilsburg",			"Norport"},
			{"Bilsburg",			"Middenbury"},
			{"Norport",				"Leandea"},
			{"Leandea",				"Amfield"},
			{"Middenbury",			"Amfield"},
			{"Amfield",				"East Dorburg"},
			{"East Dorburg",		"Casterham"},
			{"Middenbury",			"Covender"},
			{"Covender",			"Casterham"},
			{"Casterham",			"Marisbury"},
			{"Marisbury",			"Southpool"},
			{"Northcastle Keep",	"Southfort City"},
			{"Southfort City",		"Southfort"},
	};
	private HashMap<String, Town> names = new HashMap<>(towns.length);
	public Town get(String name) {
		return names.get(name);
	}
	public MapData() {
		stream().forEach(t -> names.put(t.toString(), t));
		for(String[] c : connections) {
			get(c[0]).addRoad(get(c[1]));
			get(c[1]).addRoad(get(c[0]));
		}
	}
	public Stream<Town> stream() {
		return Arrays.stream(towns);
	}
	public Town[] towns() {
		return towns;
	}
}