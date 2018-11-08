package things;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item implements Comparable<Item> {
    private static Pattern p = Pattern.compile("(.*?)(?: \\((\\d+)\\))?\\t(\\d+)");
    private int value, quantity;
    private String name;

    public Item(String s, int q, int val) {
        name = s;
        value = val;
        quantity = q;
    }

    public int value() {
        return value;
    }

    public int quantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Item toItem(String s) {
        Matcher m = p.matcher(s);
        if (m.matches()) {
            String name = m.group(1);
            String quantity = m.group(2);
            return new Item(name, quantity != null ? Integer.parseInt(quantity) : 1, Integer.parseInt(m.group(3)));
        }
        return null;
    }

    @Override
    public int compareTo(Item o) {
        return value - o.value;
    }
}
