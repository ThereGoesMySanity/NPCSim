package util;

import map.Town;
import things.Monster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class MonsterGen {
    private HashMap<String, Monster[]> monsters = new HashMap<>();
    public MonsterGen() {
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
        Arrays.stream(headers, 13, headers.length).map(String::toLowerCase)
                .forEach(h -> mons.put(h, new ArrayList<Monster>()));
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
    public Monster getMonster(Town t, float limit) {
        String env = Tables.chooseFile(monsters, () -> Arrays.stream(t.locales()));
        return Weight.chooseSorted(monsters.get(env), limit, Monster::getCR);
    }
}
