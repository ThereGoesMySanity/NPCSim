package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

import map.Town;
import people.Race;
import things.Item;
import things.Monster;

public class Tables {
	public HashMap<String, String[]> names = new HashMap<>();
	public HashMap<String, Item[]> items = new HashMap<>();
	public HashMap<String, Monster[]> monsters = new HashMap<>();
	public Tables() {
		try {
			parseInto("names", names, a -> a, String[]::new, false);
			parseInto("items", items, Item::toItem, Item[]::new, true);
			parseMonsters();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public <T extends Comparable<T>> void parseInto(String folder, HashMap<String, T[]> out, 
						Function<String, T> func, IntFunction<T[]> arr, boolean sort) throws IOException {
			Files.list(Paths.get("resources", folder))
				.flatMap(p -> {
					try {
						return p.toFile().isDirectory()? Files.list(p) : Stream.of(p);
					} catch (IOException e1) {e1.printStackTrace();}
					return Stream.empty();
				})
				.forEach(p -> {
					try {
						String s = p.toFile().getName();
						s = s.substring(0, s.length()-4);
						T[] res = Files.lines(p).map(func).toArray(arr);
						if(sort) Arrays.sort(res);
						out.put(s, res);
					} catch (IOException e) {e.printStackTrace();}
			});
	}
	public void parseMonsters() {
		Supplier<Stream<String>> ss = () -> {
			try {
				return Files.lines(Paths.get("resources", "monsters", "all.tsv"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		};
		HashMap<String, ArrayList<Monster>> mons = new HashMap<>();
		String[] headers = ss.get().findFirst().get().split("\t");
		Arrays.stream(headers, 13, headers.length).map(String::toLowerCase).forEach(h -> mons.put(h, new ArrayList<Monster>()));
		ss.get().skip(1)
			.map(s -> s.split("\t", -1))
			.filter(s -> !(s[12].isEmpty()
						|| s[8].isEmpty()
						|| s[8].equals("N/A")
						|| s[8].equals("VARIES")))
			.map(s -> Monster.toMonster(s, headers))
			.forEach(m -> m.getEnvs().forEach(s -> mons.get(s).add(m)));
		mons.forEach((k, v) -> {
			v.sort(null);
			monsters.put(k, v.toArray(new Monster[0]));
		});
	}
	public Stream<String> getRaceNames(Race r) {
		
		switch (r) {
		case HALF_ELF: return Stream.of("elf", "human");
		case HALF_ORC: return Stream.of("human");
		default: return Stream.of(r.toString().toLowerCase());
		}
	}
	public String getFirstName(int gender, Race r) {
		return chooseFromFiles(names, () -> getRaceNames(r).map(s -> "names_"+s+"_"+(gender == 0? "m" : "f")));
	}
	public String getLastName(Town t, Race r) {
		return chooseFromFiles(names, () -> getRaceNames(r).flatMap(s -> s.equals("human")?
						Stream.concat(Arrays.stream(t.locales()),
								Stream.of("misc")) : Stream.of(s)
					).map(s -> "surnames_"+s));
	}
	
	public Item getCraft() {
		return chooseFromFile(items, "craft_items");
	}
	public Item getSmith() {
		return chooseFromFiles(items, () -> Stream.of("weapons", "smith_items"));
	}
	public Monster getMonster(Town t, double cr) {
		String file = chooseFile(monsters, () -> Arrays.stream(t.locales()));
		return chooseFromFile(monsters, file, cr, Monster::getCR);
	}
	private <T> T chooseFromFiles(HashMap<String, T[]> sources, Supplier<Stream<String>> keys) {
		return chooseFromFile(sources, chooseFile(sources, keys));
	}
	private <T> String chooseFile(HashMap<String, T[]> sources, Supplier<Stream<String>> keys) {
		return Weight.weightedChoice(f -> sources.get(f).length, 
				() -> keys.get().filter(sources::containsKey));
	}
	private <T> T chooseFromFile(HashMap<String, T[]> sources, String key) {
		return Weight.choose(sources.get(key));
	}
	private <T> T chooseFromFile(HashMap<String, T[]> sources, String key, double limit, ToDoubleFunction<T> func) {
		return Weight.choose(sources.get(key), limit, func);
	}
}
