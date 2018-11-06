package ui.vars;

import javax.swing.JLabel;
import javax.swing.JSlider;

import ui.vars.VariablePane.Resetable;
import util.Variables.Doubles;

public class DoubleSlider extends JSlider implements Resetable {
	private int resolution;
	private double max;
	private Doubles var;
	private JLabel label;
	public DoubleSlider(JLabel l, Doubles d, double val, int res) {
		super(0, res, res/2);
		resolution = res;
		label = l;
		setMajorTickSpacing(resolution/10);
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
		setValue(resolution/2);
	}
}
