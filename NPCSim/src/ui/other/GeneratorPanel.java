package ui.other;

import main.Main;
import util.Generator;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;

public class GeneratorPanel extends JPanel {


    public GeneratorPanel() {
        GridBagLayout gbl = new GridBagLayout();
        gbl.rowWeights = new double[]{0, 1};
        gbl.columnWeights = new double[]{1, 0};
        setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        JComboBox<Generator> select = new JComboBox<>(Main.tables.generators.values().toArray(Generator[]::new));
        gbc.fill = BOTH;
        add(select, gbc);
        gbc = new GridBagConstraints();
        JButton button = new JButton("Generate");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.fill = BOTH;
        select.addActionListener(a -> button.setEnabled(select.getSelectedIndex() >= 0));
        add(button, gbc);
        JTextArea jta = new JTextArea();
        jta.setEditable(false);
        button.addActionListener(a -> jta.setText(select.getItemAt(select.getSelectedIndex()).generate()));
        gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.fill = BOTH;
        add(jta, gbc);
    }
}
