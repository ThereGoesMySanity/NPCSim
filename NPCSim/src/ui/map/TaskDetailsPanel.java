package ui.map;

import people.Person;
import tasks.Task;
import util.Convert;

import javax.swing.*;
import java.awt.*;

public class TaskDetailsPanel extends DetailsPanel {
    private final JList<Person> people;
    private Task task;
    private final JTabbedPane tabbedPane;
    private int labelIndex;
    private final JPanel mainPanel;
    private JLabel nameLabel;

    /**
     * Create the panel.
     */
    TaskDetailsPanel(DetailsListener listener) {
        setLayout(new BorderLayout(0, 0));

        mainPanel = new JPanel();
        add(mainPanel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 0};
        gbl_panel.columnWeights = new double[]{1, 0};
        mainPanel.setLayout(gbl_panel);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        add(tabbedPane, BorderLayout.SOUTH);

        JScrollPane peopleScroll = new JScrollPane();
        tabbedPane.addTab("People", null, peopleScroll, null);

        people = new JList<>();
        people.addListSelectionListener(listener);
        peopleScroll.setViewportView(people);
    }

    @Override
    void refreshSub() {
        task.updatePane();
        nameLabel.setText(task.toString());
        Convert.listToJList(task.people(), people);
    }

    public void addLabel(JLabel... labels) {
        for(int i = 0; i < labels.length; i++) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = i;
            gbc.gridy = labelIndex;
            mainPanel.add(labels[i], gbc);
        }
        labelIndex++;
    }

    public void addTab(String title, Component comp) {
        tabbedPane.addTab(title, comp);
    }

    @Override
    public Task getObject() {
        return task;
    }

    @Override
    public Type getType() {
        return Type.TASK;
    }

    @Override
    public void setObject(DetailsObject o) {
        task = (Task) o;
        if (o == null) return;
        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.removeTabAt(i);
        }
        mainPanel.removeAll();
        labelIndex = 0;
        nameLabel = new JLabel(task.toString());
        addLabel(nameLabel);
        task.addToPane(this);
        refresh();
    }
}
