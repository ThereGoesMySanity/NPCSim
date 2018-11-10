package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public class Convert {
    public static <K, V> void mapToTable(Map<K, V> map, JTable t, String[] columnNames) {
        Object[][] arr = new Object[map.size()][2];
        Iterator<Entry<K, V>> iter = map.entrySet().iterator();
        for (int i = 0; i < map.size(); i++) {
            Entry<K, V> e = iter.next();
            arr[i][0] = e.getKey();
            arr[i][1] = e.getValue();
        }
        t.setModel(new DefaultTableModel(arr, columnNames) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });
    }

    public static <T> void listToJList(List<T> list, JList<T> jlist) {
        jlist.setModel(new AbstractListModel<>() {
            @Override
            public int getSize() {
                return list.size();
            }

            @Override
            public T getElementAt(int index) {
                return list.get(index);
            }
        });
    }
    public static <T> void listToJList(List<T> list, JList<T> jlist, Function<T, String> map) {
        listToJList(list, jlist);
        jlist.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    return super.getListCellRendererComponent(list, map.apply((T) value), index, isSelected, cellHasFocus);
            }
        });
    }
}
