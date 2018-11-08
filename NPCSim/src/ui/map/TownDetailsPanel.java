package ui.map;

import map.Town;
import tasks.Task;
import ui.MainWindow;
import util.Convert;

import javax.swing.*;
import java.awt.*;

public class TownDetailsPanel extends JPanel {
    private JList<Task> taskList;
    private Town town;
    private JList<Town> roadList;
    private JLabel lblTownName;
    private JLabel lblPopulation;
    private JLabel lblFood;
    private JLabel lblDanger;
    private JList<String> surnames;
    private JScrollPane roadScroll;
    private JScrollPane taskScroll;

    /**
     * Create the panel.
     *
     * @param mw
     */
    public TownDetailsPanel(MainWindow mw) {
        setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        add(tabbedPane, BorderLayout.EAST);

        taskList = new JList<Task>();
        taskList.addListSelectionListener(e -> mw.addTaskPane(taskList.getSelectedValue()));
        taskScroll = new JScrollPane(taskList);
        taskScroll.setPreferredSize(new Dimension(150, 100));
        tabbedPane.addTab("Tasks", taskScroll);

        roadList = new JList<Town>();
        roadScroll = new JScrollPane(roadList);
        roadScroll.setPreferredSize(new Dimension(150, 100));
        tabbedPane.addTab("Roads", roadScroll);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{96, 0, 0};
        gbl_panel.rowHeights = new int[]{15, 15, 15, 1, 0};
        gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);

        lblTownName = new JLabel("Town Name");
        GridBagConstraints gbc_lblTownName = new GridBagConstraints();
        gbc_lblTownName.anchor = GridBagConstraints.WEST;
        gbc_lblTownName.insets = new Insets(0, 0, 5, 5);
        gbc_lblTownName.gridx = 0;
        gbc_lblTownName.gridy = 0;
        panel.add(lblTownName, gbc_lblTownName);

        lblPopulation = new JLabel("Population:");
        GridBagConstraints gbc_lblPopulation = new GridBagConstraints();
        gbc_lblPopulation.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblPopulation.insets = new Insets(0, 0, 5, 0);
        gbc_lblPopulation.gridx = 1;
        gbc_lblPopulation.gridy = 0;
        panel.add(lblPopulation, gbc_lblPopulation);

        lblFood = new JLabel("Food:");
        GridBagConstraints gbc_lblFood = new GridBagConstraints();
        gbc_lblFood.anchor = GridBagConstraints.WEST;
        gbc_lblFood.insets = new Insets(0, 0, 5, 5);
        gbc_lblFood.gridx = 0;
        gbc_lblFood.gridy = 1;
        panel.add(lblFood, gbc_lblFood);

        lblDanger = new JLabel("Danger:");
        GridBagConstraints gbc_lblDanger = new GridBagConstraints();
        gbc_lblDanger.anchor = GridBagConstraints.WEST;
        gbc_lblDanger.insets = new Insets(0, 0, 5, 0);
        gbc_lblDanger.gridx = 1;
        gbc_lblDanger.gridy = 1;
        panel.add(lblDanger, gbc_lblDanger);

        surnames = new JList<String>();
        GridBagConstraints gbc_surnames = new GridBagConstraints();
        gbc_surnames.fill = GridBagConstraints.HORIZONTAL;
        gbc_surnames.insets = new Insets(0, 0, 5, 5);
        gbc_surnames.gridx = 0;
        gbc_surnames.gridy = 2;
        panel.add(surnames, gbc_surnames);

    }

    public void setTown(Town t) {
        town = t;
        update();
    }

    public void update() {
        if (town != null) {
            Convert.listToJList(town.getTasks(), taskList);
            Convert.listToJList(town.destinations(), roadList);
            lblDanger.setText("Danger: " + town.danger);
            lblFood.setText("Food: " + town.food);
            lblPopulation.setText("Population: " + town.residents.size());
            lblTownName.setText(town.toString());
        }
    }
}
