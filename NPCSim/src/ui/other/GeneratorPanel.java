package ui.other;

import main.Main;
import util.Generator;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GeneratorPanel extends JPanel {


    public GeneratorPanel() {
        GridBagLayout gbl = new GridBagLayout();
        gbl.rowWeights = new double[]{0, 1};
        setLayout(gbl);

        JPanel listPanel = new JPanel(new GridLayout(1, Main.tables.generators.size()));
        add(new JScrollPane(listPanel));
        JTextArea jta = new JTextArea();
        for(Map.Entry<String, Generator> entry : Main.tables.generators.entrySet()) {
            JButton b = new JButton(entry.getKey());
            b.addActionListener(a -> jta.setText(entry.getValue().generate()));
            listPanel.add(b);
        }
        jta.setEditable(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        add(jta, gbc);
    }
}
