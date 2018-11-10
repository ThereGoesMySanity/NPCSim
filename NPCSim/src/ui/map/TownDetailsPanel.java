package ui.map;

import map.Town;
import tasks.Task;
import util.Convert;

import javax.swing.*;
import java.awt.*;

public class TownDetailsPanel extends JPanel implements DetailsPanel {
    private final JList<Task> taskList;
    private Town town;
    private final JList<Town> roadList;
    private final JLabel lblTownName;
    private final JLabel lblPopulation;
    private final JLabel lblFood;
    private final JLabel lblDanger;

    /**
     * Create the panel.
     *
     */
    TownDetailsPanel(MapPanel mapPanel) {
        setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        add(tabbedPane, BorderLayout.EAST);

        taskList = new JList<>();
        taskList.addListSelectionListener(mapPanel.listener);
        JScrollPane taskScroll = new JScrollPane(taskList);
        taskScroll.setPreferredSize(new Dimension(150, 100));
        tabbedPane.addTab("Tasks", taskScroll);

        roadList = new JList<>();
        roadList.addListSelectionListener(mapPanel.listener);
        JScrollPane roadScroll = new JScrollPane(roadList);
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

        JList<String> surnames = new JList<>();
        GridBagConstraints gbc_surnames = new GridBagConstraints();
        gbc_surnames.fill = GridBagConstraints.HORIZONTAL;
        gbc_surnames.insets = new Insets(0, 0, 5, 5);
        gbc_surnames.gridx = 0;
        gbc_surnames.gridy = 2;
        panel.add(surnames, gbc_surnames);

    }

    @Override
    public void setObject(DetailsObject t) {
        town = (Town) t;
        refresh();
    }

    @Override
    public void refresh() {
        if (town != null) {
            Convert.listToJList(town.getTasks(), taskList);
            Convert.listToJList(town.destinations(), roadList);
            lblDanger.setText("Danger: " + town.danger);
            lblFood.setText("Food: " + town.food);
            lblPopulation.setText("Population: " + town.residents.size());
            lblTownName.setText(town.toString());
        }
    }

    @Override
    public Town getObject() {
        return town;
    }

    @Override
    public Type getType() {
        return Type.TOWN;
    }

    @Override
    public Component toComponent() {
        return this;
    }

    @Override
    public DetailsPanel newInstance(MapPanel mapPanel) {
        return new TownDetailsPanel(mapPanel);
    }

}
