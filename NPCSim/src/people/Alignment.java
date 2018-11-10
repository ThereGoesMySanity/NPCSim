package people;

import util.Weight;

import java.io.Serializable;

public class Alignment implements Serializable {
    public enum LNC {LAWFUL, NEUTRAL, CHAOTIC}

    public enum GNE {GOOD, NEUTRAL, EVIL}

    private final LNC lnc;
    private final GNE gne;
    private String str;

    Alignment() {
        this(Weight.choose(LNC.values()), Weight.choose(GNE.values()));
    }

    private Alignment(LNC a, GNE b) {
        lnc = a;
        gne = b;
        setStr();
    }

    public boolean is(LNC a) {
        return lnc.equals(a);
    }
    public boolean is(GNE a) {
        return gne.equals(a);
    }

    private void setStr() {
        StringBuilder sb = new StringBuilder();
        switch (lnc) {
            case LAWFUL:
                sb.append("Lawful");
                break;
            case NEUTRAL:
                if (gne != GNE.NEUTRAL) sb.append("Neutral");
                else sb.append("True");
                break;
            case CHAOTIC:
                sb.append("Chaotic");
                break;
        }
        sb.append(" ");
        switch (gne) {
            case GOOD:
                sb.append("Good");
                break;
            case NEUTRAL:
                sb.append("Neutral");
                break;
            case EVIL:
                sb.append("Evil");
                break;
        }
        str = sb.toString();
    }

    @Override
    public String toString() {
        return str;
    }
}
