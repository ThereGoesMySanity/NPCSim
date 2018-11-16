package ui.vars;

import util.Variables;
import util.Variables.Doubles;
import util.Variables.Ints;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VarsPanel extends JPanel implements ChangeListener, ActionListener {
    @FunctionalInterface
    public interface Resetable {
        void reset();
    }

    private final Variables vars;
    private final ArrayList<Resetable> sliders = new ArrayList<>();

    /**
     * Create the panel.
     */
    public VarsPanel(Variables vars) {
        super(new BorderLayout());
        this.vars = vars;
        int intsSize = Variables.Ints.values().length;
        int doublesSize = Variables.Doubles.values().length;
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagLayout layout = new GridBagLayout();
        layout.rowWeights = new double[]{0.0};
        layout.columnWeights = new double[]{0., 1.};
        layout.columnWidths = new int[]{0, 0};
        layout.rowHeights = new int[intsSize + doublesSize + 1];
        layout.rowWeights = new double[intsSize + doublesSize + 1];
        layout.rowWeights[intsSize + doublesSize] = 1;
        JPanel content = new JPanel(layout);
        scrollPane.setViewportView(content);

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new GridLayout(0, 1, 0, 0));

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(this);
        buttonPanel.add(btnReset);
        for (Doubles d : Variables.Doubles.values()) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = d.ordinal();
            gbc.anchor = GridBagConstraints.WEST;
            GridBagConstraints gbc_2 = new GridBagConstraints();
            gbc_2.gridx = 1;
            gbc_2.gridy = d.ordinal();
            gbc_2.fill = GridBagConstraints.HORIZONTAL;
            JLabel label = new JLabel();
            updateLabel(d, label);
            content.add(label, gbc);
            DoubleSlider ds = new DoubleSlider(label, d, vars.get(d), 100);
            sliders.add(ds);
            ds.addChangeListener(this);
            content.add(ds, gbc_2);
        }
        for (Ints i : Variables.Ints.values()) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = i.ordinal() + doublesSize;
            gbc.anchor = GridBagConstraints.WEST;
            GridBagConstraints gbc_2 = new GridBagConstraints();
            gbc_2.gridx = 1;
            gbc_2.gridy = i.ordinal() + doublesSize;
            gbc_2.fill = GridBagConstraints.HORIZONTAL;
            content.add(new JLabel(i.toString()), gbc);
            IntSlider js = new IntSlider(i, vars.get(i));
            sliders.add(js);
            js.addChangeListener(this);
            content.add(js, gbc_2);
        }
    }

    private void updateLabel(Doubles d, JLabel l) {
        l.setText(d.toString() + ": " + String.format("%.4f", vars.get(d)));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof DoubleSlider) {
            DoubleSlider ds = (DoubleSlider) e.getSource();
            vars.set(ds.getVar(), ds.getDoubleValue());
            updateLabel(ds.getVar(), ds.getLabel());
        } else if (e.getSource() instanceof IntSlider) {
            IntSlider is = (IntSlider) e.getSource();
            vars.set(is.getVar(), is.getValue());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sliders.forEach(Resetable::reset);
    }
}
