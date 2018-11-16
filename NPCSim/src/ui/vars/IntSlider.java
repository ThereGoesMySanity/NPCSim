package ui.vars;

import ui.vars.VarsPanel.Resetable;
import util.Variables.Ints;

import javax.swing.*;

public class IntSlider extends JSlider implements Resetable {
    private final int orig;
    private final Ints var;

    public IntSlider(Ints i, int value) {
        super(0, value * 2, value);
        setMajorTickSpacing(value / 10 + 1);
        setPaintTicks(true);
        setPaintLabels(true);
        orig = value;
        var = i;
    }

    public Ints getVar() {
        return var;
    }

    @Override
    public void reset() {
        setValue(orig);
    }
}
