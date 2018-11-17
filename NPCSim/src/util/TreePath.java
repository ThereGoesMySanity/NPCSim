package util;

import java.util.*;
import java.util.stream.Stream;

public class TreePath {
    @FunctionalInterface
    public interface Children<T extends Children<T>> {
        Stream<T> children();
    }

    public static class Node<T extends Children<T>> {
        private final T value;
        private final Node<T> parent;

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

    public static <T extends Children<T>> List<T> shortestPath(T start, T end, int limit) {
        Set<T> checked = new HashSet<>();
        List<Node<T>> next, current = new ArrayList<>();
        current.add(new Node<>(start, null));
        Node<T> last = null;
        int length = 0;
        while (last == null) {
            next = new ArrayList<>();
            for (Node<T> p : current) {
                if (p.value().equals(end)) {
                    last = p;
                    break;
                } else {
                    checked.add(p.value());
                    p.value().children()
                            .filter(p1 -> !checked.contains(p1))
                            .map(n -> new Node<>(n, p))
                            .forEach(next::add);
                }
            }
            length++;
            if(next.size() == 0 || length == limit) {
                break;
            }
            current = next;
        }
        List<T> path = new ArrayList<>();
        while (last != null) {
            path.add(last.value());
            last = last.parent();
        }
        Collections.reverse(path);
        return path;
    }
}
