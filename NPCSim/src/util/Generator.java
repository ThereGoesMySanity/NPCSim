package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Generator {
    private final ArrayList<Map.Entry<String, Map.Entry<String, Integer>[]>> tables = new ArrayList<>(); //poor man's hashmap
    private boolean flat = false;
    Generator(Path p) {
        try {
            Iterator<String> iter = Files.lines(p).iterator();
            while(iter.hasNext()) {
                String[] header = iter.next().split(" ", 2);
                flat = header.length == 1 || header[1].isEmpty();
                Map.Entry<String, Integer>[] lines = new Map.Entry[Integer.parseInt(header[0])];
                for(int i = 0; i < lines.length && iter.hasNext(); i++) {
                    String[] ss = iter.next().split("\t");
                    lines[i] = Map.entry(ss[1], ss[0].isEmpty()? 1 : Integer.parseInt(ss[0]));
                }
                tables.add(Map.entry(header.length == 1 ? "" : header[1], lines));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String generate() {
        StringBuilder sb = new StringBuilder();
        tables.forEach(e -> sb.append(e.getKey())
                .append(flat?"":'\n')
                .append(Weight.weightedChoice(Map.Entry::getValue, e.getValue()).getKey())
                .append(flat?"":'\n'));
        return sb.toString();
    }
}
