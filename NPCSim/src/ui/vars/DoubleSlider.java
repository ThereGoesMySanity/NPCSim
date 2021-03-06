package ui.vars;

import ui.vars.VarsPanel.Resetable;
import util.Variables.Doubles;

import javax.swing.*;

public class DoubleSlider extends JSlider implements Resetable {
    private final int resolution;
    private final double max;
    private final Doubles var;
    private final JLabel label;

    public DoubleSlider(JLabel l, Doubles d, double val, int res) {
        super(0, res, res / 2);
        resolution = res;
        label = l;
        setMajorTickSpacing(resolution / 10);
        setPaintTicks(true);
        max = val * 2;
        var = d;
    }

    public double getDoubleValue() {
        return getValue() * max / resolution;
    }

    public Doubles getVar() {
        return var;
    }

    public JLabel getLabel() {
        return label;
    }

    @Override
    public void reset() {
        setValue(resolution / 2);
    }
}
