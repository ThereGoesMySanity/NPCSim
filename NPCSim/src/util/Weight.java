package util;
import static main.Main.rand;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;
public interface Weight {
	public static <K> K weightedChoice(ToDoubleFunction<K> weights, Supplier<Stream<K>> items) {
		double total = items.get()
				.filter(i -> weights.applyAsDouble(i) > 0)
				.mapToDouble(weights::applyAsDouble).sum();
		return weightedChoice(weights, items.get(), total);
	}
	public static <K> K weightedChoice(ToDoubleFunction<K> weights, Stream<K> items, double total) {
		AtomicReference<Double> count = new AtomicReference<Double>(0.);
		Double end = rand.nextDouble() * total;
		return items.filter(i -> weights.applyAsDouble(i) > 0).dropWhile(var -> 
					count.accumulateAndGet(weights.applyAsDouble(var), (a, b) -> a+b)
						.compareTo(end) < 0)
				.findFirst().orElse(null);
	}
	public static <K> K weightedChoice(ToDoubleFunction<K> weights, K[] items) {
		return weightedChoice(weights, () -> Arrays.stream(items));
	}
	public static <K> K weightedChoice(ToDoubleFunction<K> weights, Collection<K> items) {
		return weightedChoice(weights, () -> items.stream());
	}
	public static <K> K choose(List<K> items) {
		return items.get(rand.nextInt(items.size()));
	}
	public static <K> K choose(K[] items) {
		return items[rand.nextInt(items.length)];
	}
	public static <K> K choose(K[] items, double limit, ToDoubleFunction<K> func) {
		int i;
		for(i = 0; func.applyAsDouble(items[i]) <= limit; i++);
		return choose(items, i);
	}
	public static <K> K choose(K[] items, int limit) {
		if(limit == 0) return null;
		return items[rand.nextInt(limit)];
	}
	public static <K> K choose(Set<K> worked) {
		int num = rand.nextInt(worked.size());
		return choose(worked.iterator(), num);
	}
	public static <K> K choose(Supplier<Stream<K>> str) {
		int num = rand.nextInt((int) str.get().count());
		return choose(str.get().iterator(), num);
	}
	public static <K> K choose(Iterator<K> iter, int num) {
		for(int i = 0; i < num; i++) iter.next();
		return iter.next();
	}
}
