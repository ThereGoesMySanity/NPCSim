package util;

import java.util.Arrays;

import static main.Main.rand;

public class Dice {
    public static class Die {
        int sign, num, die;

        public Die(int s, int n, int d) {
            num = n;
            die = d;
        }

        public int roll() {
            int sum = 0;
            for (int i = 0; i < num; i++) {
                sum += rand.nextInt(die) + 1;
            }
            return sum * sign;
        }

        public static Die parseChunk(String s) {
            int sign = 1;
            if (s.charAt(0) < '0') {
                s = s.substring(1);
                if (s.charAt(0) == '-') {
                    sign = -1;
                }
            }
            String[] ss = s.split("d");
            int num = ss[0].isEmpty() ? 1 : Integer.parseInt(ss[0]);
            return new Die(sign, num, Integer.parseInt(ss[1]));
        }
    }

    private Die[] dice;
    private int constant;

    public Dice(String s) {
        dice = new Die[(int) s.chars().filter(c -> c == 'd').count()];
        s = '+' + s.replace(" ", "") + '+';
        int index = 0, last = 0;
        boolean d = false;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == 'd') d = true;
            else if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                if (d) dice[index++] = Die.parseChunk(s.substring(last, i));
                else constant += Integer.parseInt(s.substring(last, i));
                last = i;
                d = false;
            }
        }
    }

    public int roll() {
        return Arrays.stream(dice).mapToInt(Die::roll).sum() + constant;
    }
}
