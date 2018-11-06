package util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Convert {
	public static <K,V> void mapToTable(Map<K,V> map, JTable t, String[] columnNames) {
		Object[][] arr = new Object[map.size()][2];
		Iterator<Entry<K,V>> iter = map.entrySet().iterator();
		for(int i = 0; i < map.size(); i++) {
			Entry<K,V> e = iter.next();
			arr[i][0] = e.getKey();
			arr[i][1] = e.getValue();
		}
		t.setModel(new DefaultTableModel(arr, columnNames) {
			@Override
			public boolean isCellEditable(int r, int c) { return false; }
		});
	}
	public static <T> void listToJList(List<T> list, JList<T> jlist) {
		jlist.setModel(new AbstractListModel<T>() {
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
}
