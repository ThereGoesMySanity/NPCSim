package people;
import static main.Main.*;

public enum Race {
	HUMAN, ELF, HALF_ELF, DWARF, HALFLING, DRAGONBORN, HALF_ORC, TIEFLING;
	private static final double[] demographics = {
			//HUMAN	ELF		HALF_ELF	DWARF	HALFLING	DRAGONBORN	HALF_ORC	TIEFLING}
			80,		6,		6,			2,		3,			1,			1,			1
	};
	private static final int[] ages = {
			//HUMAN	ELF		HALF_ELF	DWARF	HALFLING	DRAGONBORN	HALF_ORC	TIEFLING}
			80,		750,	180,		350,	150,		80,			60,			85
	};
	public int getAge() {
		return ages[ordinal()];
	}
	public int getRandomAge() {
		return Math.max(15, rand.nextInt(getAge() - getAge() / 5));
	}
	public double getChance() {
		return demographics[ordinal()];
	}
	public static Race getRace(Race r, Race r2) {
		if(r == r2) return r;
		if(r == HUMAN && r2 == ELF
				|| r == ELF && r2 == HUMAN) return HALF_ELF;
		if(r == HALF_ELF && r2 == HUMAN
				|| r == HUMAN && r2 == HALF_ELF) 
			return rand.nextBoolean()? HALF_ELF : HUMAN;
		if(r == HALF_ELF && r2 == ELF
				|| r == ELF && r2 == HALF_ELF)
			return rand.nextBoolean()? HALF_ELF : ELF;
		if(r == HALF_ORC && r2 == HUMAN
				|| r == HUMAN && r2 == HALF_ORC)
			return rand.nextBoolean()? HALF_ORC : HUMAN;
		return null;
	}
}
