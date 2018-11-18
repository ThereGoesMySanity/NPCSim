package util;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

import static main.Main.rand;

public class Weight {
    public static <K> K weightedChoice(ToDoubleFunction<K> weights, Supplier<Stream<K>> items) {
        double total = items.get()
                .mapToDouble(weights)
                .filter(d -> d > 0).sum();
        return weightedChoice(weights, items.get(), total);
    }

    private static <K> K weightedChoice(ToDoubleFunction<K> weights, Stream<K> items, double total) {
        AtomicReference<Double> count = new AtomicReference<>(0.);
        Double end = rand.nextDouble() * total;
        return items.filter(i -> weights.applyAsDouble(i) > 0).dropWhile(var ->
                count.accumulateAndGet(weights.applyAsDouble(var), (a, b) -> a + b)
                        .compareTo(end) < 0)
                .findFirst().orElse(null);
    }

    public static <K> K weightedChoice(ToDoubleFunction<K> weights, K[] items) {
        return weightedChoice(weights, () -> Arrays.stream(items));
    }

    public static <K> K weightedChoice(ToDoubleFunction<K> weights, Collection<K> items) {
        return weightedChoice(weights, items::stream);
    }

    public static <K> K choose(List<K> items) {
        return items.get(rand.nextInt(items.size()));
    }

    public static <K> K choose(K[] items) {
        return items[rand.nextInt(items.length)];
    }

    static <K, T extends Comparable<T>> K chooseSorted(K[] items, T lower, T upper, Function<K, T> func) {
        int i = 0;
        while (lower != null && func.apply(items[i]).compareTo(lower) <= 0) i++;
        int j = i;
        while(func.apply(items[j]).compareTo(upper) <= 0) j++;
        return choose(items, i, j + 1);
    }
    public static <K> K choose(K[] items, int min, int max) {
        return items[rand.nextInt(max - min) + min];
    }

    public static <K> K choose(Set<K> worked) {
        int num = rand.nextInt(worked.size());
        return choose(worked.iterator(), num);
    }

    public static <K> K choose(Supplier<Stream<K>> str) {
        int size = (int) str.get().count();
        if(size == 0) return null;
        else return choose(str.get().iterator(), rand.nextInt(size));
    }

    private static <K> K choose(Iterator<K> iter, int num) {
        if(!iter.hasNext()) return null;
        for (int i = 0; i < num; i++) iter.next();
        return iter.next();
    }
}
