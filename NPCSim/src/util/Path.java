package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Path {
	@FunctionalInterface
	public interface Children<T extends Children<T>> {
		Stream<T> children();
	}
	public static class Node<T extends Children<T>> {
		private T value;
		private Node<T> parent;
		public Node(T v, Node<T> p) {
			value = v;
			parent = p;
		}
		public T value() {
			return value;
		}
		public Node<T> parent() {
			return parent;
		}
	}
	public static <T extends Children<T>> List<T> shortestPath(T start, T end) {
		Set<T> checked = new HashSet<>();
		List<Node<T>> next, current = new ArrayList<>();
		current.add(new Node<T>(start, null));
		Node<T> last = null;
		int len = 0;
		while(last == null) {
			next = new ArrayList<>();
			len++;
			for(Node<T> p : current) {
				if(p.value().equals(end)) {
					last = p;
					break;
				} else {
					checked.add(p.value());
					p.value().children()
							.filter(p1 -> !checked.contains(p1))
							.map(n -> new Node<T>(n, p))
							.forEach(next::add);
				}
			}
			current = next;
		}
		List<T> path = new ArrayList<>(len);
		while(last != null) {
			path.add(last.value());
			last = last.parent();
		}
		Collections.reverse(path);
		return path;
	}
}
