package map;

import java.util.stream.Stream;

public class Map {
	private MapData data = new MapData();
	public void update() {
		data.stream().forEach(Town::update);
	}
	public Town[] towns() {
		return data.towns();
	}
	public Stream<Town> stream() {
		return data.stream();
	}
	@Override
	public String toString() {
		return "Map Name"; //TODO
	}
}
