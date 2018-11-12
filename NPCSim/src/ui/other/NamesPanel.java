package ui.other;

import people.Race;

import javax.swing.*;
import java.awt.*;

import static main.Main.tables;

public class NamesPanel extends JPanel {
    String[] surnames = {
            "city",
            "desert",
            "farm",
            "forest",
            "misc",
            "mountain",
            "river",
            "scholar",
            "sea",
            "smith",
            "soldier",
            "swamp",
    };
    public NamesPanel() {
        GridBagLayout gbl = new GridBagLayout();
        gbl.rowWeights = new double[]{0, 1};
        setLayout(gbl);

        JComboBox<Race> jcb = new JComboBox<>(Race.values());
        add(jcb);
        JTextArea jta = new JTextArea();
        jta.setEditable(false);
        JButton b = new JButton("Generate");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(b, gbc);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(jta, gbc);
        b.addActionListener(a -> {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < 10; i++)
                sb.append(tables.getFirstName((int)(Math.random() * 2), (Race) jcb.getSelectedItem())).append(' ')
                    .append(tables.getLastName(surnames, (Race) jcb.getSelectedItem())).append('\n');
            jta.setText(sb.toString());
        });
    }
}
