package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Generator {
    private final ArrayList<Map.Entry<String, String[]>> tables = new ArrayList<>(); //poor man's hashmap
    Generator(Path p) {
        try {
            Iterator<String> iter = Files.lines(p).iterator();
            while(iter.hasNext()) {
                String[] header = iter.next().split(" ", 2);
                String[] lines = new String[Integer.parseInt(header[0])];
                for(int i = 0; i < lines.length && iter.hasNext(); i++) {
                    lines[i] = iter.next();
                }
                tables.add(Map.entry(header[1], lines));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String generate() {
        StringBuilder sb = new StringBuilder();
        tables.forEach(e -> sb.append(e.getKey())
                .append("\n")
                .append(Weight.choose(e.getValue()))
                .append('\n'));
        return sb.toString();
    }
}
