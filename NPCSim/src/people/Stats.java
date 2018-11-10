package people;

import javax.swing.table.AbstractTableModel;
import java.io.Serializable;

import static main.Main.rand;

public class Stats extends AbstractTableModel implements Serializable {
    public enum Stat {STR, DEX, CON, INT, WIS, CHA}
    private final int[] stats;

    public Stats() {
        stats = new int[6];
        for (Stat s : Stat.values()) {
            stats[s.ordinal()] = genStat();
        }
    }

    private int genStat() {
        return (int) (Math.round(rand.nextGaussian() * 2.5) + 10);
    }

    public int get(Stat s) {
        return stats[s.ordinal()];
    }

    int getMod(Stat s) {
        return stats[s.ordinal()] / 2 - 5;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 6; i++) {
            sb.append(Stat.values()[i]);
            sb.append(": ");
            sb.append(stats[i]);
            if (i < 5) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public void set(Stat s, int val) {
        stats[s.ordinal()] = val;
    }

    @Override
    public int getRowCount() {
        return stats.length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Stat.values()[rowIndex].toString();
            case 1:
                return stats[rowIndex];
            default:
                return null;
        }
    }
}
