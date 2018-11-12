package util;

import map.Town;
import people.Race;
import things.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Tables {
    private final HashMap<String, String[]> names = new HashMap<>();
    private final HashMap<String, Item[]> items = new HashMap<>();
    public final MonsterGen monsterGen = new MonsterGen();
    public final HashMap<String, Generator> generators = new HashMap<>();

    public Tables() {
        try {
            parseInto("names", names, fileToArr(Function.identity(), String[]::new, false));
            parseInto("items", items, fileToArr(Item::toItem, Item[]::new, true));
            parseInto("generators", generators, Generator::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> void parseInto(String folder, HashMap<String, T> out,
                               Function<Path, T> func) throws IOException {
        Files.list(Paths.get("resources", folder))
                .flatMap(p -> {
                    try {
                        return p.toFile().isDirectory() ? Files.list(p) : Stream.of(p);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return Stream.empty();
                })
                .forEach(p -> {
                    String s = p.toFile().getName();
                    s = s.substring(0, s.length() - 4);
                    out.put(s, func.apply(p));
                });
    }
    private <T extends Comparable<T>> Function<Path, T[]> fileToArr(Function<String, T> converter,
                                                                    IntFunction<T[]> constructor, boolean sort) {
        return path -> {
            T[] res = null;
            try {
                res = Files.lines(path).map(converter).toArray(constructor);
                if (sort) Arrays.sort(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        };
    }


    private Stream<String> getRaceNames(Race r) {
        switch (r) {
            case HALF_ELF:
                return Stream.of("elf", "human");
            case HALF_ORC:
                return Stream.of("human");
            default:
                return Stream.of(r.toString().toLowerCase());
        }
    }

    public String chooseName(String file) {
        return chooseFromFile(names, file);
    }

    public String getFirstName(int gender, Race r) {
        return chooseFromFiles(names, () -> getRaceNames(r).map(s -> "names_" + s + "_" + (gender == 0 ? "m" : "f")));
    }

    public String getLastName(Town t, Race r) {
        return chooseFromFiles(names, () -> getRaceNames(r).flatMap(s -> s.equals("human") ?
                Stream.concat(Arrays.stream(t.locales()),
                        Stream.of("misc")) : Stream.of(s)
                ).map(s -> "surnames_" + s));
    }
    public String getLastName(String[] surnames, Race r) {
        return chooseFromFiles(names, () -> getRaceNames(r).flatMap(s -> s.equals("human") ?
                Arrays.stream(surnames) : Stream.of(s)
        ).map(s -> "surnames_" + s));
    }

    public Item getCraft() {
        return chooseFromFile(items, "craft_items");
    }

    public Item getSmith() {
        return chooseFromFiles(items, () -> Stream.of("weapons", "smith_items"));
    }

    public Generator getGenerator(String s) {
        return generators.get(s);
    }


    private static <T> T chooseFromFiles(HashMap<String, T[]> sources, Supplier<Stream<String>> keys) {
        return chooseFromFile(sources, chooseFile(sources, keys));
    }

    static <T> String chooseFile(HashMap<String, T[]> sources, Supplier<Stream<String>> keys) {
        return Weight.weightedChoice(f -> sources.get(f).length,
                () -> keys.get().filter(sources::containsKey));
    }

    private static <T> T chooseFromFile(HashMap<String, T[]> sources, String key) {
        return Weight.choose(sources.get(key));
    }
}
