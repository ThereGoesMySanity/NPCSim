package things;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import util.Dice;
import util.Weight;

public class Monster implements Comparable<Monster>{
	private static final int[] levels = {10, 200, 450, 700, 1100, 1800, 2300, 2900,
			3900, 5000, 5900, 7200, 8400, 10000, 11500, 13000,
			15000, 18000, 20000, 22000, 25000, 33000, 41000, 50000,
			62000, 75000, 90000, 105000, 120000, 135000, 155000
	};
	float cr;
	int hp;
	String name;
	String[] env;
	Dice[] attacks;
	public Monster(String s, float cr, int hp, String[] env, Dice[] attacks) {
		name = s;
		this.cr = cr;
		this.hp = hp;
		this.env = env;
		this.attacks = attacks;
	}
	public Stream<String> getEnvs() {
		return Arrays.stream(env);
	}
	public float getCR() {
		return cr;
	}
	public int getHP() {
		return hp;
	}
	public int getXP() {
		if(cr % 1 == 0) {
			return levels[(int)cr];
		}
		return (int) (100 * cr);
	}
	public int attack() {
		return Weight.choose(attacks).roll();
	}
	public static Monster toMonster(String[] ss, String[] headers) {
		String[] env = IntStream.range(13, ss.length)
				.filter(i -> ss[i].equals("YES"))
				.mapToObj(i -> headers[i].toLowerCase()).toArray(String[]::new);
		float cr = Float.parseFloat(ss[12]);
		int hp = Integer.parseInt(ss[6]);
		Dice[] attacks = Arrays.stream(ss, 8, 11)
				.filter(s1 -> !(s1.equals("N/A")
						   || s1.isEmpty()))
				.map(Dice::new).toArray(Dice[]::new);
		return new Monster(ss[0], cr, hp, env, attacks);
	}
	@Override
	public int compareTo(Monster o) {
		return Double.compare(cr, o.cr);
	}
	@Override
	public String toString() {
		return name + " (CR " + cr + ")";
	}
}
