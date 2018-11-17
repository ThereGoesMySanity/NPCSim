package ui.other;

import people.Person;
import ui.map.DetailsListener.DLListener;
import ui.map.DetailsPanel;
import util.Convert;
import util.TreePath;

import javax.swing.*;
import java.awt.*;

public class SearchPanel extends JPanel implements DLListener {
    private Person person1, person2, selectedPerson;
    private final JTextField p1;
    private final JTextField p2;
    private final JList<Person> path;

    public SearchPanel() {
        GridBagLayout gbl = new GridBagLayout();
        gbl.rowWeights = new double[]{0, 0, 1};
        gbl.columnWeights = new double[]{1, 0, 1, 0};
        this.setLayout(gbl);
        p1 = new JTextField();
        p1.setEditable(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(p1, gbc);
        JButton b1 = new JButton("Set");
        b1.addActionListener(a -> setPerson1());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(b1, gbc);

        p2 = new JTextField();
        p2.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 2;
        gbc.gridy = 0;
        add(p2, gbc);
        JButton b2 = new JButton("Set");
        b2.addActionListener(a -> setPerson2());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        add(b2, gbc);

        JButton exec = new JButton("Find");
        exec.addActionListener(a -> exec());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        add(exec, gbc);

        path = new JList<>();
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        add(path, gbc);
    }
    private void setPerson1() {
        person1 = selectedPerson;
        p1.setText(person1.toString());
    }
    private void setPerson2() {
        person2 = selectedPerson;
        p2.setText(person2.toString());
    }
    private void exec() {
        if(person1 != null && person2 != null)
            Convert.listToJList(TreePath.shortestPath(person1, person2, Integer.MAX_VALUE), path);
    }

    @Override
    public void onChange(DetailsPanel.DetailsObject o) {
        selectedPerson = (Person) o;
    }

    @Override
    public boolean listening(DetailsPanel.Type t) {
        return t == DetailsPanel.Type.PERSON;
    }
}
